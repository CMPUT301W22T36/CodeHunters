package com.cmput301w22t36.codehunters.Data;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataTypes.QRCodeData;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

public abstract class DataMapper<D extends Data> {

    public class CompletionHandler {
        public void handleSuccess(D data) {
        }

        public void handleError(Exception e) {
        }
    }

    protected FirebaseFirestore db;
    protected CollectionReference collectionRef;

    public DataMapper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public void create(D data, CompletionHandler ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
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

    public abstract void get(String documentID, CompletionHandler ch);
    public abstract void set(D data, CompletionHandler ch);
    public abstract void update(D data, CompletionHandler ch);
    public abstract void delete(D data, CompletionHandler ch);

    protected abstract Map<String, Object> dataToMap(D data);
    protected abstract D mapToData(@NonNull Map<String, Object> dataMap);
}
