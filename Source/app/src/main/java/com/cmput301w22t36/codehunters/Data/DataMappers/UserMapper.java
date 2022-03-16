package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserMapper extends DataMapper<User> {
    private CollectionReference usersRef;
    private CollectionReference devicesRef;

    public UserMapper() {
        super();
        usersRef = db.collection("users");
        devicesRef = db.collection("devices");
    }

    @Override
    public void get(String documentName, CompletionHandler ch) {
        usersRef.document(documentName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot deviceDocument = task.getResult();
                            if (deviceDocument.exists()) {
                                User retrievedUser = new User();
                                Map<String, Object> userData = deviceDocument.getData();
                                retrievedUser.setUsername((String) userData.get("username"));
                                retrievedUser.setEmail((String) userData.get("email"));
                                // retrievedUser.setId(documentName); // we'll have to modify the user class to get this to work
                                // retrievedUser.setUdid(udid); // we'll have to modify the user class to get this to work
                                ch.handleSuccess(retrievedUser);
                            }
                        } else {
                            ch.handleError();
                        }
                    }
                });
    }

    @Override
    public void set(User data) {
        String documentName = data.getId(); // once again, we need to change the user class
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", data.getUsername());
        userData.put("email", data.getEmail());
        usersRef.document(documentName).set(userData);
    }

    @Override
    public void update(User data) {
        // this might end up basically just being the same as set?
    }

    @Override
    public void delete(User data) {

    }

    // Gets user associated with device id.
    public void queryUDID(String udid, CompletionHandler ch) {

        // find the document for this UDID
        devicesRef.document(udid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot deviceDocument = task.getResult();
                    if (deviceDocument.exists()) {
                        String uuid = deviceDocument.getString("uuid");
                        // now that we have the uuid for this udid, we can get the user
                        get(uuid, ch);
                    } else {
                        ch.handleError();
                    }

                } else {
                    // ERROR
                    ch.handleError();
                }
            }
        });
        return;
    }

}
