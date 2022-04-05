package com.cmput301w22t36.codehunters.Data.DataMappers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class QRCodeMapper extends DataMapper<QRCodeData> {

    // Constants for document fields.
    enum Fields {
        USERREF("userRef"),
        SCORE("score"),
        CODE("code"),
        LAT("lat"),
        LON("lon"),
        PHOTOURL("photourl");

        private final String field;

        Fields(final String field) {
            this.field = field;
        }

        @NonNull
        @Override
        public String toString() {
            return field;
        }
    }


    public QRCodeMapper() {
        super();
        collectionRef = db.collection("qrcodes");

    }

    @Override
    public void create(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        if (data.getPhoto() != null) {
            this.putImage(data.getPhoto(), this.new CompletionHandler<String>() {
                @Override
                public void handleSuccess(String photoUrl) {
                    data.setPhotourl(photoUrl);
                    Map<String, Object> dataMap = dataToMap(data);
                    collectionRef.add(dataMap)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    DocumentReference docRef = task.getResult();
                                    data.setId(docRef.getId());
                                    ch.handleSuccess(data);
                                } else {
                                    ch.handleError(new FSAccessException("Data creation failed"));
                                }
                            });
                }
                @Override
                public void handleError(Exception e) {
                    ch.handleError(e);
                }
            });
        } else {
            super.create(data, ch);
        }

    }

    @Override
    public void set(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .set(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    public void update(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        if (data.getPhoto() != null) {
            putImage(data.getPhoto(), this.new CompletionHandler<String>() {
                @Override
                public void handleSuccess(String photoUrl) {
                    data.setPhotourl(photoUrl);
                    Map<String, Object> dataMap = dataToMap(data);
                    collectionRef.document(data.getId())
                            .update(dataMap)
                            .addOnCompleteListener(task -> ch.handleSuccess(data));
                }
                @Override
                public void handleError(Exception e) {
                    ch.handleError(e);
                }
            });
        }
        else {
            super.update(data, ch);
        }
    }

    public void queryQRCodes(User user, CompletionHandler<ArrayList<QRCodeData>> lch) {
        String userRef = "/users/" + user.getId();
        collectionRef.whereEqualTo("userRef", userRef)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents= task.getResult().getDocuments();
                        ArrayList<QRCodeData> qrData = new ArrayList<>();
                        for (DocumentSnapshot docSnap : documents) {
                            // Get photos for each qrcode.
                            if (docSnap.getData() != null){
                                QRCodeData qrCode = mapToData(docSnap.getData(), docSnap);
                                if (qrCode.getPhotourl() != null) {
                                    getImage(qrCode.getPhotourl(), new CompletionHandler<Bitmap>() {
                                        @Override
                                        public void handleSuccess(Bitmap bMap) {
                                            qrCode.setPhoto(bMap);
                                            qrData.add(qrCode);
                                            if (qrData.size() == documents.size()) {
                                                //noinspection StatementWithEmptyBody
                                                while (qrData.remove(null));
                                                lch.handleSuccess(qrData);
                                            }
                                        }

                                        @Override
                                        public void handleError(Exception e) {
                                            qrData.add(null);
                                            if (qrData.size() == documents.size()) {
                                                //noinspection StatementWithEmptyBody
                                                while (qrData.remove(null)) ;
                                                lch.handleSuccess(qrData);
                                            }
                                        }
                                    });
                                } else {
                                    // No photo associated with qrcode
                                    qrData.add(qrCode);
                                    if (qrData.size() == documents.size()) {
                                        //noinspection StatementWithEmptyBody
                                        while (qrData.remove(null)) ;
                                        lch.handleSuccess(qrData);
                                    }
                                }
                            }
                        }
                    } else {
                        lch.handleError(new FSAccessException("Username not unique or other error"));
                    }
                });
    }


    //Query to get codes for user
    public void queryUsersCodes(String udid, CompletionHandler<ArrayList<QRCodeData>> lch) {
        UserMapper um = new UserMapper();
        um.queryUDID(udid, um.new CompletionHandler<User>() {
            @Override
            public void handleSuccess(User data) {
                String userref = "/users/" + data.getId();
                collectionRef.whereEqualTo("userRef", userref)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && task.getResult().getDocuments().size() != 0) {
                                List<DocumentSnapshot> documents= task.getResult().getDocuments();
                                ArrayList<QRCodeData> qrData = new ArrayList<>();
                                for (DocumentSnapshot docSnap : documents) {
                                    if (docSnap.getData() != null) {
                                        QRCodeData qrCode = mapToData(docSnap.getData(), docSnap);
                                        if (qrCode.getPhotourl() != null) {
                                            // Async get photo from database.
                                            getImage(qrCode.getPhotourl(), new CompletionHandler<Bitmap>() {
                                                @Override
                                                public void handleSuccess(Bitmap bMap) {
                                                    qrCode.setPhoto(bMap);
                                                    qrData.add(qrCode);
                                                    if (qrData.size() == documents.size()) {
                                                        //noinspection StatementWithEmptyBody
                                                        while (qrData.remove(null)) ;
                                                        lch.handleSuccess(qrData);
                                                    }
                                                }

                                                @Override
                                                public void handleError(Exception e) {
                                                    qrData.add(null);
                                                    if (qrData.size() == documents.size()) {
                                                        //noinspection StatementWithEmptyBody
                                                        while (qrData.remove(null)) ;
                                                        lch.handleSuccess(qrData);
                                                    }
                                                }
                                            });
                                        } else {
                                            qrData.add(qrCode);
                                            if (qrData.size() == documents.size()) {
                                                //noinspection StatementWithEmptyBody
                                                while (qrData.remove(null)) ;
                                                lch.handleSuccess(qrData);
                                            }
                                        }
                                    }
                                }
                            } else {
                                lch.handleError(new FSAccessException("Username not unique or other error"));
                            }
                        });
            }
        });
    }

    /**\
     * Calls the given completion handler with a list of all unique QRCodes on the firebase. All of
     * the codes returned have had any entry-specific fields nullified (e.g. ID, userRef)
     * @param ch the completion handler
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getAllCodes(CompletionHandler<ArrayList<QRCodeData>> ch) {
        // get literally every QRcode :(
        // For hashMap
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // build up a hashmap of all the unique codes, first come first serve
                HashMap<String, QRCodeData> uniqueCodes = new HashMap<>();
                for (DocumentSnapshot docSnap : task.getResult()) {
                    if (docSnap.getData() != null) {
                        QRCodeData qrCode = mapToData(docSnap.getData());
                        // nullify specific fields
                        qrCode.setUserRef(null);
                        qrCode.setId(null);
                        if (!uniqueCodes.containsKey(qrCode.getHash())) {
                            uniqueCodes.put(qrCode.getHash(), qrCode);
                        } else {
                            // Replace original if it doesn't have an image.
                            QRCodeData uniqueQr = uniqueCodes.get(qrCode.getHash());
                            if (uniqueQr != null && uniqueQr.getPhotourl() == null) {
                                uniqueCodes.replace(qrCode.getHash(), qrCode);
                            }
                        }
                    }
                }

                ArrayList<QRCodeData> codes = new ArrayList<>(uniqueCodes.values());
                ch.handleSuccess(codes);
            } else {
                ch.handleError(new FSAccessException("Failed to retrieve qrcodes from Firestore"));
            }
        });
    }


     /** This method takes a user and a list of QR codes and finds all QR codes shared between the
     * given list and the codes that the given user have scanned. Upon success, the onSuccess method
     * is called with the resulting list as an argument.
     * @param userToSearch the user to search through
     * @param matchList the list of QRCodes to match with
     * @param ch the completion handler that will be called upon success
     */
    public void getMatchingCodes(User userToSearch, ArrayList<? extends QRCodeData> matchList,
                                 CompletionHandler<ArrayList<QRCodeData>> ch) {

        // Create list of the strings of the codes
        ArrayList<String> justTheCodes = new ArrayList<>();
        for (QRCodeData code : matchList) {
            justTheCodes.add(code.getHash());
        }

        // make a query where we filter to codes from the given user and then
        // to the codes in the justTheCodes list.
        collectionRef.whereEqualTo(Fields.USERREF.toString(), "/users/"+userToSearch.getId())
                .whereIn(Fields.CODE.toString(), justTheCodes)
                .get().addOnCompleteListener(task -> {
                    // check if the query was successful
                    if (task.isSuccessful()) {
                        // get the results and then turn them into a list
                        QuerySnapshot results = task.getResult();
                        ArrayList<QRCodeData> matchedCodes = new ArrayList<>();

                        for (DocumentSnapshot docSnap : results) {
                            if (docSnap.getData() != null) {
                                matchedCodes.add(mapToData(docSnap.getData(), docSnap));
                            }
                        }
                        ch.handleSuccess(matchedCodes);

                    } else {
                        ch.handleError(new FSAccessException("Firestore fetch unsuccessful"));
                    }
                });
    }

    /**
     * Used to download image from Firestore Storage.
     * @param path Path to image on Firebase Storage.
     * @param ch Returns image bitmap.
     */

    public void getImage(String path, CompletionHandler<Bitmap> ch) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference pathRef = storageRef.child(path);

        // Change this var to alter the max image size.
        final long MAX_DOWN = 1024 * 1024;
        pathRef.getBytes(MAX_DOWN).addOnSuccessListener(bytes -> {
            Bitmap bmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ch.handleSuccess(bmap);
        }).addOnFailureListener(ch::handleError);
    }

    /**
     * Used to upload image to Firebase Storage.
     * @param bmap Image bitmap.
     * @param ch Returns path to image on Firestore.
     */
    public void putImage(Bitmap bmap, CompletionHandler<String> ch) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName = Long.toString(System.currentTimeMillis());
        StorageReference fileRef = storageRef.child(fileName);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnSuccessListener(task -> ch.handleSuccess(fileName));

        uploadTask.addOnFailureListener(ch::handleError);
    }


    @Override
    protected Map<String, Object> dataToMap(QRCodeData data) {
        Map<String, Object> qrCodeMap = new HashMap<>();
        qrCodeMap.put(Fields.USERREF.toString(), data.getUserRef());
        qrCodeMap.put(Fields.SCORE.toString(), data.getScore());
        qrCodeMap.put(Fields.CODE.toString(), data.getHash());
        qrCodeMap.put(Fields.LAT.toString(), data.getLat());
        qrCodeMap.put(Fields.LON.toString(), data.getLon());
        qrCodeMap.put(Fields.PHOTOURL.toString(), data.getPhotourl());
        return qrCodeMap;
    }

    protected QRCodeData mapToData(@NonNull Map<String, Object> dataMap, DocumentSnapshot document) {
        QRCodeData qrCodeData = mapToData(dataMap);
        qrCodeData.setId(document.getId());
        return qrCodeData;
    }

    @Override
    protected QRCodeData mapToData(@NonNull Map<String, Object> dataMap) {
        // Integer type is nullable (opposed to int). Explicitly states that Integer must not be null (fail-fast).
        QRCodeData qrCodeData = new QRCodeData();
        qrCodeData.setUserRef(Objects.requireNonNull(String.valueOf(dataMap.get(Fields.USERREF.toString()))));

        Long score = (Long) dataMap.get(Fields.SCORE.toString());
        qrCodeData.setScore(score != null ? (int)(long)score : 0);

        qrCodeData.setHash(Objects.requireNonNull((String) dataMap.get(Fields.CODE.toString())));

        Double lat = (Double)dataMap.get(Fields.LAT.toString());
        qrCodeData.setLat(lat != null ? lat : 0);

        Double lon = (Double)dataMap.get(Fields.LON.toString());
        qrCodeData.setLon(lon != null ? lon : 0);

        qrCodeData.setPhotourl((String) dataMap.get(Fields.PHOTOURL.toString()));
        return qrCodeData;
    }
}

