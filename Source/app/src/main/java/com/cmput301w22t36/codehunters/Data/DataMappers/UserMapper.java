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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class UserMapper extends DataMapper<User> {

    public UserMapper() {
        super();
        collectionRef = db.collection("users");
    }

    @Override
    public void get(String documentID, CompletionHandler ch) {
        // get the document for this UUID. I think that we'll have the UUID be the first UDID for
        // that account. That seems the most simple to me.
        collectionRef.document(documentID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot userDoc = task.getResult();
                            if (userDoc.exists()) {
                                // task was successful and the document was found.
                                User retrievedUser = mapToData(userDoc.getData());
                                retrievedUser.setId(documentID);
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
    public void set(User data, CompletionHandler ch) {
        String documentName = data.getId(); // once again, we need to change the user class
        Map<String, Object> userData = dataToMap(data);
        collectionRef.document(documentName).set(userData);
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
        collectionRef.whereArrayContains("udid", udid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> userDocs = task.getResult().getDocuments();
                    if (userDocs.size() > 0 && userDocs.get(0).exists()) {
                        DocumentSnapshot doc = userDocs.get(0);
                        Map<String, Object> userMap = doc.getData();
                        if (userMap == null) { ch.handleError(new FSAccessException("Data null")); }
                        else {
                            User user = mapToData(userMap);
                            user.setId(doc.getReference().toString());
                            ch.handleSuccess(user);
                        }
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
        collectionRef.whereEqualTo("username", username)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().getDocuments().size() == 0) {
                        ch.handleSuccess(null);
                    } else {
                        ch.handleError(new FSAccessException("Username not unique or other error"));
                    }
                });
    }

    @Override
    protected Map<String, Object> dataToMap(User data) {
        Map<String, Object> map = new HashMap<>();
        map.put("username", data.getUsername());
        map.put("email", data.getEmail());
        map.put("udid", data.getUdid());
        return map;
    }

    @Override
    protected User mapToData(@NonNull Map<String, Object> dataMap) {
        // NOTE: Does not set the documentId!
        User user = new User();
        user.setUsername((String) dataMap.get("username"));
        user.setEmail((String) dataMap.get("email"));
        user.setUdid((ArrayList<String>)dataMap.get("udid"));
        return user;
    }
}
