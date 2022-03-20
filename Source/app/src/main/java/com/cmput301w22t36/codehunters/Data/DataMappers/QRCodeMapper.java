package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
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


    private final CollectionReference qrCodesRef;

    public QRCodeMapper() {
        super();
        qrCodesRef = db.collection("qrcodes");

    }

    @Override
    public void get(String documentID, CompletionHandler ch) {
        qrCodesRef.document(documentID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot qrCodeDocument = task.getResult();
                        if (qrCodeDocument.exists()) {
                            // task was successful and the document was found.
                            Map<String, Object> qrCodeData = qrCodeDocument.getData();
                            // documentID is the same as uuid
                            try {
                                QRCodeData retrievedQRCode = mapToData(Objects.requireNonNull(qrCodeData));
                                ch.handleSuccess(retrievedQRCode);
                            } catch (NullPointerException e) {
                                ch.handleError(e);
                            }
                        } else {
                            ch.handleError(new FSAccessException("Document doesn't exist."));
                        }
                    } else {
                        ch.handleError(new FSAccessException("Data retrieval failed"));
                    }
                });
    }

    @Override
    public void set(QRCodeData data, CompletionHandler ch) {
        Map<String, Object> qrCodeData = this.dataToMap(data);
        qrCodesRef.document(data.getId()).set(qrCodeData)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    @Override
    public void update(QRCodeData data, CompletionHandler ch) {
        Map<String, Object> qrCodeData = this.dataToMap(data);
        qrCodesRef.document(data.getId())
                .update(qrCodeData)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    @Override
    public void delete(QRCodeData data, CompletionHandler ch) {
        qrCodesRef.document(data.getId()).delete()
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    // Used as a generic way to load a map with fields from data.
    private Map<String, Object> dataToMap(QRCodeData data) {
        Map<String, Object> qrCodeMap = new HashMap<>();
        qrCodeMap.put(Fields.USERREF.toString(), data.getUserRef());
        qrCodeMap.put(Fields.SCORE.toString(), data.getScore());
        qrCodeMap.put(Fields.CODE.toString(), data.getCode());
        qrCodeMap.put(Fields.LAT.toString(), data.getLat());
        qrCodeMap.put(Fields.LON.toString(), data.getLon());
        return qrCodeMap;
    }

    @NonNull
    private QRCodeData mapToData(@NonNull Map<String, Object> dataMap) {
        // Integer type is nullable (opposed to int). Explicitly states that Integer must not be null (fail-fast).
        QRCodeData qrCodeData = new QRCodeData();
        qrCodeData.setId(Objects.requireNonNull((String) dataMap.get("DocumentId")));
        qrCodeData.setUserRef(Objects.requireNonNull((String) dataMap.get(Fields.USERREF.toString())));
        qrCodeData.setScore(Objects.requireNonNull((Integer) dataMap.get(Fields.SCORE.toString())));
        qrCodeData.setCode(Objects.requireNonNull((String) dataMap.get(Fields.CODE.toString())));
        qrCodeData.setLat(Objects.requireNonNull((Integer) dataMap.get(Fields.LAT.toString())));
        qrCodeData.setLon(Objects.requireNonNull((Integer) dataMap.get(Fields.LON.toString())));
        qrCodeData.setPhotourl(Objects.requireNonNull((String) dataMap.get(Fields.PHOTOURL.toString())));
        return qrCodeData;
    }
}
