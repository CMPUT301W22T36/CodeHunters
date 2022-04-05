package com.cmput301w22t36.codehunters.Data;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.Objects;

/** Utility to be inherited by classes that operate on a particular datatype in Firestore.
 * @author Connor
 * @param <D> Data type to be used in mapper.
 */
public abstract class DataMapper<D extends Data> {

    /** Handles completion of a method. Success and error methods should be overridden for functionality.
     * Not abstract for the case where handling completion is unimportant.
     * @param <T> Type to be returned in the case of success.
     */
    @SuppressWarnings("InnerClassMayBeStatic")
    public class CompletionHandler<T> {
        /** Handles the successful completion of a task.
         * @param data Returned data from successful task completion.
         */
        public void handleSuccess(T data) {
        }

        /** Handles unsuccessful completion of a task.
         * @param e Error that occurred during task execution.
         */
        public void handleError(Exception e) {
        }
    }


    protected FirebaseFirestore db;
    protected CollectionReference collectionRef;

    public DataMapper() {
        this.db = FirebaseFirestore.getInstance();
    }

    /** Creates a new instance of generic data on the database.
     * @param data Generic data to be created on the database. id should be null.
     * @param ch Handles completion of task.
     */
    public void create(D data, CompletionHandler<D> ch) {
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

    /** Gets an instance of data on the database. id required for operation.
     * @param documentID id of data instance on database.
     * @param ch Handles completion of task.
     */
    public void get(String documentID, CompletionHandler<D> ch) {
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

    /** Updates or creates data on database.
     * @param data Data to be used in task. id required.
     * @param ch Handles completion.
     */
    public void set(D data, CompletionHandler<D> ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .set(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    /** Updates data instance on database. If data doesn't exist, causes error.
     * @param data Data to be used in task. id required.
     * @param ch Handles completion of task.
     */
    public void update(D data, CompletionHandler<D> ch) {
        Map<String, Object> dataMap = this.dataToMap(data);
        collectionRef.document(data.getId())
                .update(dataMap)
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    /** Deletes instance of data on database. id required.
     * @param data Data to be used in task.
     * @param ch Handles completion of task.
     */
    public void delete(D data, CompletionHandler<D> ch) {
        collectionRef.document(data.getId())
                .delete()
                .addOnCompleteListener(task -> ch.handleSuccess(data));
    }

    /** Converts data to map.
     * @param data Data to be converted to map.
     * @return Map of data.
     */
    protected abstract Map<String, Object> dataToMap(D data);

    /** Converts map to data
     * @param dataMap Map to be converted to data.
     * @return Data instantiated from map.
     */
    protected abstract D mapToData(@NonNull Map<String, Object> dataMap);
}
