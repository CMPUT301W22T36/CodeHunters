package com.cmput301w22t36.codehunters.Data;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DataMapper<D extends Data> {

    public class CompletionHandler {
        public void handleSuccess(D data) {}
        public void handleError(Exception e) {}
    }

    protected FirebaseFirestore db;

    public DataMapper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public abstract void create(D data, CompletionHandler ch);
    public abstract void get(String documentID, CompletionHandler ch);
    public abstract void set(D data, CompletionHandler ch);
    public abstract void update(D data, CompletionHandler ch);
    public abstract void delete(D data, CompletionHandler ch);
}
