package com.cmput301w22t36.codehunters.Data.DataMappers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
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
                            if (task.isSuccessful() && task.getResult().getDocuments().size() == 0) {
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
        qrCodeMap.put(Fields.CODE.toString(), data.getCode());
        qrCodeMap.put(Fields.LAT.toString(), data.getLat());
        qrCodeMap.put(Fields.LON.toString(), data.getLon());
        qrCodeMap.put(Fields.PHOTOURL.toString(), data.getPhotourl());
        return qrCodeMap;
    }

    @Override
    protected QRCodeData mapToData(@NonNull Map<String, Object> dataMap) {
        // Integer type is nullable (opposed to int). Explicitly states that Integer must not be null (fail-fast).
        QRCodeData qrCodeData = new QRCodeData();
        qrCodeData.setUserRef(Objects.requireNonNull((String) dataMap.get(Fields.USERREF.toString())));
        qrCodeData.setScore(Objects.requireNonNull((Integer) dataMap.get(Fields.SCORE.toString())));
        qrCodeData.setCode(Objects.requireNonNull((String) dataMap.get(Fields.CODE.toString())));
        qrCodeData.setLat((Double) dataMap.get(Fields.LAT.toString()));
        qrCodeData.setLon((Double) dataMap.get(Fields.LON.toString()));
        qrCodeData.setPhotourl((String) dataMap.get(Fields.PHOTOURL.toString()));
        return qrCodeData;
    }
}
