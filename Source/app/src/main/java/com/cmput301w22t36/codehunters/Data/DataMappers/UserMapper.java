package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserMapper extends DataMapper<User> {
    private CollectionReference usersRef;

    public UserMapper() {
        super();
        usersRef = db.collection("users");
    }

    @Override
    public void get(int i, CompletionHandler ch) {
        return;
    }

    @Override
    public void set(User data) {

    }

    @Override
    public void update(User data) {

    }

    @Override
    public void delete(User data) {

    }

    // Gets user associated with device id.
    public void queryUDID(String udid, CompletionHandler ch) {
        usersRef.whereArrayContains("udids", udid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // PROCESS DATA
                    User user = new User();

                    ch.handleSuccess(user);
                } else {
                    // ERROR
                    ch.handleError();
                }
            }
        });
        return;
    }

}
