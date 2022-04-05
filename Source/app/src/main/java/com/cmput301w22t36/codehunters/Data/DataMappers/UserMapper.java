package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserMapper extends DataMapper<User> {

    public UserMapper() {
        super();
        collectionRef = db.collection("users");
    }


    // Gets user associated with UDID.
    public void queryUDID(String udid, CompletionHandler<User> ch) {
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
                            user.setId(doc.getId());
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

    public void usersOrderedBy(String orderedBy, CompletionHandler<ArrayList<User>> ch) {
        collectionRef.orderBy(orderedBy).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<User> users = new ArrayList<>();
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                User newUser = mapToData(document.getData());
                                newUser.setId(document.getId());
                                users.add(newUser);
                            }
                            ch.handleSuccess(users);
                        } else {
                            ch.handleError(new FSAccessException("Firestore query not successful"));
                        }
                    }
                });
    }

    /**
     * This queries the firestore for a user given the username. the given completion handler is
     * called with the user, if it's found, or null if not found.
     * @param username the username to seach the firebase for
     * @param ch the completion handler for the query
     */
    public void queryUsername(String username, CompletionHandler<User> ch) {
        collectionRef.whereEqualTo("username", username).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            User foundUser = null;
                            QuerySnapshot queryResult = task.getResult();
                            if (queryResult.getDocuments().size() > 1) {
                                ch.handleError(new FSAccessException("Username is not unique"));
                            } else if (queryResult.getDocuments().size() == 1) {
                                foundUser = mapToData(queryResult.getDocuments().get(0).getData());
                                foundUser.setId(queryResult.getDocuments().get(0).getId());
                                ch.handleSuccess(foundUser);
                            }else {
                                ch.handleError(new FSAccessException("Username is not unique"));
                            }

                        } else {
                            ch.handleError(new FSAccessException("Firestore query not successful"));
                        }
                    }
                });
    }

    public void usernameUnique(String username, CompletionHandler<User> ch) {
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
        map.put("bestScore", data.getBestScore());
        map.put("scanCount", data.getScanCount());
        map.put("score", data.getScore());
        map.put("isOwner", data.getOwner());
        map.put("udid", data.getUdid());
        return map;
    }

    @Override
    protected User mapToData(@NonNull Map<String, Object> dataMap) {
        // NOTE: Does not set the documentId!
        User user = new User();
        user.setUsername((String) dataMap.get("username"));
        user.setEmail((String) dataMap.get("email"));

        Integer bestScore = dataMap.get("bestScore") == null ? 0 : (int)(long)dataMap.get("bestScore");
        user.setBestScore(bestScore);

        Integer scanCount = dataMap.get("scanCount") == null ? 0 : (int)(long)dataMap.get("scanCount");
        user.setScanCount(scanCount);

        Integer score = dataMap.get("score") == null ? 0 : (int)(long)dataMap.get("score");
        user.setScore(score);

        user.setUdid((ArrayList<String>)dataMap.get("udid"));
        user.setOwner(Boolean.valueOf(String.valueOf(dataMap.get("isOwner"))));
        return user;
    }
}
