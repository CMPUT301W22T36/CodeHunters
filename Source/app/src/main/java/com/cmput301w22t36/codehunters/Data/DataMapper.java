package com.cmput301w22t36.codehunters.Data;

import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DataMapper<D extends Data> {

    public abstract class CompletionHandler {
        public abstract void handleSuccess(D data);
        public abstract void handleError();
    }

    protected FirebaseFirestore db;

    public DataMapper() {
        this.db = FirebaseFirestore.getInstance();
    }

    public abstract void get(int i, CompletionHandler ch);
    public abstract void set(D data);
    public abstract void update(D data);
    public abstract void delete(D data);
}

