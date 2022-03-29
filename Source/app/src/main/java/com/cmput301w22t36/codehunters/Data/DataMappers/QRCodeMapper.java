package com.cmput301w22t36.codehunters.Data.DataMappers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.cmput301w22t36.codehunters.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
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
import java.util.concurrent.CountDownLatch;

public class QRCodeMapper extends DataMapper<QRCodeData> {

    // Constants for document fields.
    enum Fields {
        USERREF("userRef"),
        SCORE("score"),
        CODE("code"),
        LAT("lat"),
        LON("lon"),  // TODO: CONVERT TO GEOPOINT!
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

    public void queryQRCodes(User user, CompletionHandler<ArrayList<QRCodeData>> lch) {
        String userRef = "/users/" + user.getId();
        collectionRef.whereEqualTo("userRef", userRef)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents= task.getResult().getDocuments();
                        ArrayList<QRCodeData> qrData = new ArrayList<QRCodeData>();
                        for (DocumentSnapshot docSnap : documents) {
                            qrData.add(mapToData(docSnap.getData()));
                        }
                        lch.handleSuccess(qrData);
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
                                ArrayList<QRCodeData> qrData = new ArrayList<QRCodeData>();
                                CountDownLatch latch = new CountDownLatch(documents.size());
                                for (DocumentSnapshot docSnap : documents) {
                                    QRCodeData qrCode = mapToData(docSnap.getData());
                                    if (qrCode.getPhotourl() != null) {
                                        // Async get photo from database.
                                        getImage(qrCode.getPhotourl(), new CompletionHandler<Bitmap>() {
                                            @Override
                                            public void handleSuccess(Bitmap bMap) {
                                                qrCode.setPhoto(bMap);
                                                qrData.add(qrCode);
                                                latch.countDown();
                                            }
                                            @Override
                                            public void handleError(Exception e) {
                                                latch.countDown();
                                            }
                                        });
                                    }
                                    else {
                                        qrData.add(qrCode);
                                        latch.countDown();
                                    }
                                }
                                try {
                                    latch.await();
                                    lch.handleSuccess(qrData);
                                }
                                catch (Exception e) {
                                    lch.handleError(e);
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
    public void getAllCodes(CompletionHandler<ArrayList<QRCodeData>> ch) {
        // get literally every QRcode :(
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // build up a hashmap of all the unique codes, first come first serve
                    HashMap<String, QRCodeData> uniqueCodes = new HashMap<>();
                    for (DocumentSnapshot code : task.getResult()) {
                        QRCodeData thisCode = mapToData(code.getData());
                        // nullify specific fields
                        thisCode.setUserRef(null);
                        thisCode.setId(null);
                        if (!uniqueCodes.containsKey(thisCode.getHash())) {
                            uniqueCodes.put(thisCode.getHash(), thisCode);
                        }
                    }
                    ArrayList<QRCodeData> codes = new ArrayList<QRCodeData>(uniqueCodes.values());
                    ch.handleSuccess(codes);
                } else {
                    ch.handleError(new FSAccessException("I guess firestore didn't feel like it"));
                }
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
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                // check if the query was successful
                if (task.isSuccessful()) {
                    // get the results and then turn them into a list
                    QuerySnapshot results = task.getResult();
                    ArrayList<QRCodeData> matchedCodes = new ArrayList<>();

                    for (DocumentSnapshot document : results) {
                        matchedCodes.add(mapToData(document.getData()));
                    }
                    ch.handleSuccess(matchedCodes);

                } else {
                    ch.handleError(new FSAccessException("Firestore fetch unsuccessful"));
                }
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
        }).addOnFailureListener(e -> {
            ch.handleError(e);
        });
    }

    /**
     * Used to upload image to Firebase Storage.
     * @param bmap Image bitmap.
     * @param ch Returns path to image on Firestore.
     */
    public void putImage(Bitmap bmap, CompletionHandler<String> ch) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageRef.putBytes(data)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ch.handleSuccess(storageRef.getPath());
                    } else {
                        ch.handleError(new FSAccessException("Could not store image"));
                    }
                });
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

    @Override
    protected QRCodeData mapToData(@NonNull Map<String, Object> dataMap) {
        // Integer type is nullable (opposed to int). Explicitly states that Integer must not be null (fail-fast).
        QRCodeData qrCodeData = new QRCodeData();
        qrCodeData.setUserRef(Objects.requireNonNull(String.valueOf(dataMap.get(Fields.USERREF.toString()))));
        qrCodeData.setScore(Objects.requireNonNull((int)(long) dataMap.get(Fields.SCORE.toString())));
        qrCodeData.setHash(Objects.requireNonNull((String) dataMap.get(Fields.CODE.toString())));
        qrCodeData.setLat(((Number) dataMap.get(Fields.LAT.toString())).doubleValue());
        qrCodeData.setLon(((Number) dataMap.get(Fields.LON.toString())).doubleValue());
        qrCodeData.setPhotourl((String) dataMap.get(Fields.PHOTOURL.toString()));
        return qrCodeData;
    }
}
