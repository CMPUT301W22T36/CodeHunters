package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

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

    public void queryQRCodes(User user, CompletionHandler ch) {

    }

    //Query to get codes for user
    public void query_usercodes (String udid, ListCompletionHandler lch) {
        UserMapper um = new UserMapper();
        um.queryUDID(udid, um.new CompletionHandler() {
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
        qrCodeData.setLat(Objects.requireNonNull((Double) dataMap.get(Fields.LAT.toString())));
        qrCodeData.setLon(Objects.requireNonNull((Double) dataMap.get(Fields.LON.toString())));
        qrCodeData.setPhotourl(Objects.requireNonNull((String) dataMap.get(Fields.PHOTOURL.toString())));
        return qrCodeData;
    }
}
