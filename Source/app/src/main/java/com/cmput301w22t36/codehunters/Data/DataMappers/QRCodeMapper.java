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
import com.google.firebase.firestore.GeoPoint;
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

 /** Utility for operating on QR codes in Firestore
 */
public class QRCodeMapper extends DataMapper<QRCodeData> {

     /** Constants for fields in Firestore
      */
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

     /** Create new QR code on database. Will upload photo.
      * @param data QR code to create. Should have no id.
      * @param ch Handles completion of task. Data will have an id at this point.
      */
    @Override
    public void create(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        if (data.getPhoto() != null) {
            this.putImage(data.getPhoto(), this.new CompletionHandler<String>() {
                @Override
                public void handleSuccess(String photoUrl) {
                    data.setPhotoUrl(photoUrl);
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

     /** Modifies (or creates) QR code in database.
      * @param data QR code to be modified. id is required.
      * @param ch Handles completion of task.
      */
    @Override
    public void set(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .set(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

     /** Updates an existing QR code in database.
      * @param data QR code to be updated. Changes photo.
      * @param ch Handles completion of task.
      */
    public void update(QRCodeData data, CompletionHandler<QRCodeData> ch) {
        if (data.getPhoto() != null) {
            putImage(data.getPhoto(), this.new CompletionHandler<String>() {
                @Override
                public void handleSuccess(String photoUrl) {
                    data.setPhotoUrl(photoUrl);
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

     /** Gets all QR codes scanned by particular user.
      * @param user User to use in query.
      * @param lch Handles completion of task.
      */
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
                                if (qrCode.getPhotoUrl() != null) {
                                    getImage(qrCode.getPhotoUrl(), new CompletionHandler<Bitmap>() {
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


     /** Gets all codes associated with a particular user. Looks up device id first.
      * @param udid Device id to be used in query.
      * @param lch Handles completion of task.
      */
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
                                        if (qrCode.getPhotoUrl() != null) {
                                            // Async get photo from database.
                                            getImage(qrCode.getPhotoUrl(), new CompletionHandler<Bitmap>() {
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

    /** Gets all QR codes from the database. Prunes duplicate scans. Removes any identifiers so that QRCode is generic.
     * @param ch Handles completion of task.
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
                            if (uniqueQr != null && uniqueQr.getPhotoUrl() == null) {
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

    /**
     * Gets a list of every QRCode with a unique location, prioritizing ones with a photo upon
     * duplicates.
     * @param ch Handles completion of the request
     */

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getAllCodesLocations(CompletionHandler<ArrayList<QRCodeData>> ch) {
        // get literally every QRcode :(
        // For hashMap
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // build up a hashmap of all the unique codes, first come first serve
                HashMap<GeoPoint, QRCodeData> uniqueLocations = new HashMap<>();
                for (DocumentSnapshot docSnap : task.getResult()) {
                    if (docSnap.getData() != null) {
                        QRCodeData qrCode = mapToData(docSnap.getData());
                        // nullify specific fields
                        qrCode.setId(docSnap.getId());
                        GeoPoint thisLocation = new GeoPoint(qrCode.getLat(), qrCode.getLon());
                        if (!uniqueLocations.containsKey(thisLocation)) {
                            uniqueLocations.put(thisLocation, qrCode);
                        } else {
                            // Replace original if it doesn't have an image.
                            QRCodeData uniqueQr = uniqueLocations.get(thisLocation);
                            if (uniqueQr != null && uniqueQr.getPhotoUrl() == null) {
                                GeoPoint uniqueQrLocation = new GeoPoint(uniqueQr.getLat(), uniqueQr.getLon());
                                uniqueLocations.replace(uniqueQrLocation, qrCode);
                            }
                        }
                    }
                }

                ArrayList<QRCodeData> codes = new ArrayList<>(uniqueLocations.values());
                ch.handleSuccess(codes);
            } else {
                ch.handleError(new FSAccessException("Failed to retrieve qrcodes from Firestore"));
            }
        });
    }


     /** Finds QR codes in common between a user and a given set of QR codes.
     * @param userToSearch User to match codes with.
     * @param matchList QR codes to match.
     * @param ch Handles completion of task.
     */
    public void getMatchingCodes(User userToSearch, ArrayList<? extends QRCodeData> matchList,
                                 CompletionHandler<ArrayList<QRCodeData>> ch) {

        if (matchList.size() < 1) {
            ch.handleSuccess(new ArrayList<>());
        } else {

            // Make a map of the codes for quick lookup
            HashMap<String, QRCodeData> codeHashes = new HashMap<>();
            for (QRCodeData code : matchList) {
                codeHashes.put(code.getHash(), code);
            }

            // make a query where we filter to codes from the given user and then
            // to the codes in the justTheCodes list.
            collectionRef.whereEqualTo(Fields.USERREF.toString(), "/users/"+userToSearch.getId())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot results = task.getResult();
                    ArrayList<QRCodeData> matchedCodes= new ArrayList<>();

                    for (DocumentSnapshot docSnap : results) {
                        if (docSnap.getData() != null) {
                            QRCodeData newCode = mapToData(docSnap.getData(), docSnap);
                            if (codeHashes.containsKey(newCode.getHash())) {
                                matchedCodes.add(newCode);
                            }
                        }
                    }
                    ch.handleSuccess(matchedCodes);

                } else {
                    ch.handleError(new FSAccessException("Firestore fetch unsuccessful"));
                }
            });
        }
    }

    /** Downloads image from Firebase Storage.
     * @param path File identifier.
     * @param ch Handles completion of task.
     */
    public void getImage(String path, CompletionHandler<Bitmap> ch) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference pathRef = storageRef.child(path);

        // Change this var to alter the max image size.
        final long MAX_DOWN = 1024 * 1024;
        pathRef.getBytes(MAX_DOWN).addOnSuccessListener(bytes -> {
            Bitmap bMap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            ch.handleSuccess(bMap);
        }).addOnFailureListener(ch::handleError);
    }

    /** Uploads image to Firebase Storage.
     * @param bMap Image to upload.
     * @param ch Returns file identifier.
     */
    public void putImage(Bitmap bMap, CompletionHandler<String> ch) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        String fileName = Long.toString(System.currentTimeMillis());
        StorageReference fileRef = storageRef.child(fileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnSuccessListener(task -> ch.handleSuccess(fileName));

        uploadTask.addOnFailureListener(ch::handleError);
    }


     /** Converts QR code to map.
      * @param data QR code to convert.
      * @return Map of QR code.
      */
    @Override
    protected Map<String, Object> dataToMap(QRCodeData data) {
        Map<String, Object> qrCodeMap = new HashMap<>();
        qrCodeMap.put(Fields.USERREF.toString(), data.getUserRef());
        qrCodeMap.put(Fields.SCORE.toString(), data.getScore());
        qrCodeMap.put(Fields.CODE.toString(), data.getHash());
        qrCodeMap.put(Fields.LAT.toString(), data.getLat());
        qrCodeMap.put(Fields.LON.toString(), data.getLon());
        qrCodeMap.put(Fields.PHOTOURL.toString(), data.getPhotoUrl());
        return qrCodeMap;
    }

     /** Converts map to QR code, sets id.
      * @param dataMap Map to convert.
      * @param document Document used to add document id.
      * @return QR code with document id set.
      */
    protected QRCodeData mapToData(@NonNull Map<String, Object> dataMap, DocumentSnapshot document) {
        QRCodeData qrCodeData = mapToData(dataMap);
        qrCodeData.setId(document.getId());
        return qrCodeData;
    }

     /** Converts map to QR code.
      * @param dataMap Map to convert.
      * @return QR code converted from map.
      */
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

        qrCodeData.setPhotoUrl((String) dataMap.get(Fields.PHOTOURL.toString()));
        return qrCodeData;
    }
}

