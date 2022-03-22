package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class UserMapper extends DataMapper<User> {
    private final CollectionReference usersRef;

    public UserMapper() {
        super();
        usersRef = db.collection("users");
    }

    @Override
    public void get(String documentID, CompletionHandler ch) {
        // get the document for this UUID. I think that we'll have the UUID be the first UDID for
        // that account. That seems the most simple to me.
        usersRef.document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDoc = task.getResult();
                            if (userDoc.exists()) {
                                // task was successful and the document was found.
                                User retrievedUser = mapToData(userDoc.getData());
                                ch.handleSuccess(retrievedUser);
                            } else {
                                ch.handleError(new FSAccessException("Document doesn't exist"));
                            }
                        } else {
                            ch.handleError(new FSAccessException("Data retrieval failed"));
                        }
                    }
                });
    }

    @Override
    public void create(User data, CompletionHandler ch) {
        // TODO!
    }

    @Override
    public void set(User data, CompletionHandler ch) {
        String documentName = data.getId(); // once again, we need to change the user class
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", data.getUsername());
        userData.put("email", data.getEmail());
        // TODO: userData.put("udid", data.getUdid());
        usersRef.document(documentName).set(userData);
    }

    @Override
    public void update(User data, CompletionHandler ch) {
        // this might end up basically just being the same as set?
    }

    @Override
    public void delete(User data, CompletionHandler ch) {

    }

    // Gets user associated with device id.
    public void queryUDID(String udid, CompletionHandler ch) {
        // TODO: FIX THIS (wrong docRef).
        // find the document for this UDID
        usersRef.whereArrayContains("udid", udid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot userDoc = task.getResult().getDocuments().get(0);
                    if (userDoc.exists()) {
                        //String uuid = userDoc.getString("uuid");
                        // now that we have the uuid for this udid, we can get the user
                        //get(uuid, ch);
                        User user = mapToData(userDoc.getData());
                        ch.handleSuccess(user);
                    } else {
                        ch.handleError(new FSAccessException("Document doesn't exist"));
                    }
                } else {
                    // ERROR
                    ch.handleError(new FSAccessException("Data retrieval failed"));
                }
            }
        });
    }

    public void usernameUnique(String username, CompletionHandler ch) {
        // If unique, ch.handleSuccess will run; otherwise ch.handleError .
        usersRef.whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() == null) {
                        ch.handleSuccess(null);
                    } else {
                        ch.handleError(new FSAccessException("Username not unique or other error"));
                    }
                });
    }

    @NonNull
    private User mapToData(@NonNull Map<String, Object> dataMap) {
        User user = new User();
        user.setId(Objects.requireNonNull((String) dataMap.get("DocumentId")));
        user.setUsername((String) dataMap.get("username"));
        user.setEmail((String) dataMap.get("email"));
        return user;
    }
}
