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
        String documentName = data.getId(); // once again, we need to change the user class
        Map<String, Object> qrCodeData = new HashMap<>();
        qrCodeData.put(Fields.USERREF.toString(), data.getUserRef());
        qrCodeData.put(Fields.SCORE.toString(), data.getScore());
        qrCodeData.put(Fields.CODE.toString(), data.getCode());
        qrCodeData.put(Fields.LAT.toString(), data.getLat());
        qrCodeData.put(Fields.LON.toString(), data.getLon());
        qrCodeData.put(Fields.PHOTOURL.toString(), data.getPhotourl());
        qrCodesRef.document(documentName).set(qrCodeData);
    }

    @Override
    public void update(QRCodeData data) {

    }

    @Override
    public void delete(QRCodeData data) {

    }
}
