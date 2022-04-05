package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** Utility for operating on Users in Firestore
 */
public class UserMapper extends DataMapper<User> {

    public UserMapper() {
        super();
        collectionRef = db.collection("users");
    }


    /** Gets User associated with device id.
     * @param udid Device id to use in query.
     * @param ch Handles completion of task.
     */
    public void queryUDID(String udid, CompletionHandler<User> ch) {
        collectionRef.whereArrayContains("udid", udid)
                .get()
                .addOnCompleteListener(task -> {
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
                        ch.handleError(new FSAccessException("Data retrieval failed"));
                    }
                });
    }

    /** Gets all users, ordered.
     * @param orderedBy Method to order users by.
     * @param ch Handles completion of task.
     */
    public void usersOrderedBy(String orderedBy, CompletionHandler<ArrayList<User>> ch) {
        collectionRef.orderBy(orderedBy).get()
                .addOnCompleteListener(task -> {
                    ArrayList<User> users = new ArrayList<>();
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            if (document.getData() != null) {
                                User newUser = mapToData(document.getData());
                                newUser.setId(document.getId());
                                users.add(newUser);
                            }
                        }
                        ch.handleSuccess(users);
                    } else {
                        ch.handleError(new FSAccessException("Firestore query not successful"));
                    }
                });
    }

    /** Get user with particular username. Throws exception in completion handler if username not found.
     * @param username Username to be used in query.
     * @param ch Handles completion of task.
     */
    public void queryUsername(String username, CompletionHandler<User> ch) {
        collectionRef.whereEqualTo("username", username).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        QuerySnapshot queryResult = task.getResult();
                        if (queryResult.getDocuments().size() > 1) {
                            ch.handleError(new FSAccessException("Username is not unique"));
                        } else if (queryResult.getDocuments().size() == 1) {
                            Map<String,Object> data = queryResult.getDocuments().get(0).getData();
                            if (data != null) {
                                User foundUser = mapToData(data);
                                foundUser.setId(queryResult.getDocuments().get(0).getId());
                                ch.handleSuccess(foundUser);
                            } else {
                                ch.handleError(new FSAccessException("Null data"));
                            }
                        } else {
                            ch.handleError(new FSAccessException("Username is not unique"));
                        }

                    } else {
                        ch.handleError(new FSAccessException("Firestore query not successful"));
                    }
                });
    }

    /** Check if given username is unique.
     * @param username Username to test.
     * @param ch Handles completion of task.
     */
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

    /** Converts User to a Map.
     * @param data User to convert to map.
     * @return Map of User.
     */
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

    /** Converts Map to User. Does not set document id.
     * @param dataMap Map to convert.
     * @return User instantiated with data from map.
     */
    @Override
    protected User mapToData(@NonNull Map<String, Object> dataMap) {
        // NOTE: Does not set the documentId!
        User user = new User();
        user.setUsername((String) dataMap.get("username"));
        user.setEmail((String) dataMap.get("email"));

        Long bestScore = (Long)dataMap.get("bestScore");
        user.setBestScore(bestScore != null ? bestScore.intValue() : 0);

        Long scanCount = (Long)dataMap.get("scanCount");
        user.setScanCount(scanCount != null ? scanCount.intValue() : 0);

        Long score = (Long)dataMap.get("score");
        user.setScore(score != null ? score.intValue() : 0);

        ArrayList<?> udidList = (ArrayList<?>)dataMap.get("udid");
        if (udidList != null) {
            //noinspection unchecked
            user.setUdid((ArrayList<String>) udidList);
        }
        user.setOwner(Boolean.valueOf(String.valueOf(dataMap.get("isOwner"))));
        return user;
    }
}
