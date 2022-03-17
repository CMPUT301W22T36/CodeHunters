package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

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

        @Override
        public String toString() {
            return field;
        }
    }


    private CollectionReference qrCodesRef;

    public QRCodeMapper() {
        qrCodesRef = db.collection("qrcodes");

    }

    @Override
    public void get(String documentName, CompletionHandler ch) {
        qrCodesRef.document(documentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot qrCodeDocument = task.getResult();
                            if (qrCodeDocument.exists()) {
                                // task was successful and the document was found.
                                QRCodeData retrievedQRCode = new QRCodeData();
                                Map<String, Object> qrCodeData = qrCodeDocument.getData();
                                // documentID is the same as uuid
                                retrievedQRCode.setId(documentName);
                                retrievedQRCode.setUserRef((String) qrCodeData.get(Fields.USERREF.toString()));
                                retrievedQRCode.setScore((int) qrCodeData.get(Fields.SCORE.toString()));
                                retrievedQRCode.setCode((String) qrCodeData.get(Fields.CODE.toString()));
                                retrievedQRCode.setLat((int) qrCodeData.get(Fields.LAT.toString()));
                                retrievedQRCode.setLon((int) qrCodeData.get(Fields.LON.toString()));
                                retrievedQRCode.setPhotourl((String) qrCodeData.get(Fields.PHOTOURL.toString()));

                                ch.handleSuccess(retrievedQRCode);
                            } else {
                                ch.handleError();
                            }
                        } else {
                            ch.handleError();
                        }
                    }
                });
    }

    @Override
    public void set(QRCodeData data) {
        Map<String, Object> qrCodeData = this.dataToMap(data);
        qrCodesRef.document(data.getId()).set(qrCodeData);
    }

    @Override
    public void update(QRCodeData data) {
        Map<String, Object> qrCodeData = this.dataToMap(data);
        qrCodesRef.document(data.getId()).update(qrCodeData);
    }

    @Override
    public void delete(QRCodeData data) {
        qrCodesRef.document(data.getId()).delete();
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
}
