package com.cmput301w22t36.codehunters.Data;

import androidx.annotation.NonNull;

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

    public void get(String documentID, CompletionHandler ch) {
        collectionRef.document(documentID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot qrCodeDocument = task.getResult();
                        if (qrCodeDocument.exists()) {
                            // task was successful and the document was found.
                            Map<String, Object> dataMap = qrCodeDocument.getData();
                            // documentID is the same as uuid
                            try {
                                D data = mapToData(Objects.requireNonNull(dataMap));
                                data.setId(documentID);
                                ch.handleSuccess(data);
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

    public void set(D data, CompletionHandler ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .set(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    public void update(D data, CompletionHandler ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .update(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }
    public void delete(D data, CompletionHandler ch) {
        collectionRef.document(data.getId())
                .delete()
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    // Used as a generic way to load a map with fields from data.
    protected abstract Map<String, Object> dataToMap(D data);
    protected abstract D mapToData(@NonNull Map<String, Object> dataMap);
}
