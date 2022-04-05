package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.Comment;
import com.cmput301w22t36.codehunters.Data.FSAccessException;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/** Utility for operating on Comments in Firestore
 */
public class CommentMapper extends DataMapper<Comment> {
    /** Constants for fields in Firestore
     */
    enum Fields {
        USERREF("userRef"),
        HASHREF("hashRef"),
        COMMENT("comment"),
        TIMESTAMP("timestamp");

        private final String field;

        Fields(final String field) {
            this.field = field;
        }

        @NonNull
        @Override
        public String toString() {
            return field;
        }
    }

    public CommentMapper() {
        super();
        collectionRef = db.collection("comments");
    }

    /** Gets all comments associated with a particular hash.
     * @param hash Hash of QR code.
     * @param ch Handles completion of operation.
     */
    public void getCommentsForHash(String hash, CompletionHandler<ArrayList<Comment>> ch) {
        collectionRef.whereEqualTo(Fields.HASHREF.toString(), hash)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents= task.getResult().getDocuments();
                        ArrayList<Comment> comments = new ArrayList<>();
                        for (DocumentSnapshot docSnap : documents) {
                            if (docSnap.getData() != null) {
                                Comment c = mapToData(docSnap.getData());
                                c.setId(docSnap.getId());
                                comments.add(c);
                            }
                        }
                        ch.handleSuccess(comments);
                    } else {
                        ch.handleError(new FSAccessException("Failed to retrieve comments."));
                    }
                });
    }


    /** Converts a comment to a map.
     * @param data Comment to be converted to map.
     * @return Mapped comment.
     */
    @Override
    protected Map<String, Object> dataToMap(Comment data) {
        Map<String, Object> map = new HashMap<>();
        map.put("userRef", data.getUserRef());
        map.put("hashRef", data.getHashRef());
        map.put("comment", data.getComment());
        map.put("timestamp", data.getTimestamp());
        return map;
    }

    /** Converts map to comment.
     * @param dataMap Map to be converted.
     * @return Comment generated from map.
     */
    @Override
    protected Comment mapToData(@NonNull Map<String, Object> dataMap) {
        // NOTE: Does not set the documentId!
        Comment c = new Comment();
        c.setUserRef(Objects.requireNonNull((String) dataMap.get(Fields.USERREF.toString())));
        c.setHashRef(Objects.requireNonNull((String) dataMap.get(Fields.HASHREF.toString())));
        c.setComment((String) dataMap.get(Fields.COMMENT.toString()));

        Long timestamp = (Long)dataMap.get(Fields.TIMESTAMP.toString());
        timestamp = timestamp == null ? 0 : timestamp;
        c.setTimestamp(timestamp);
        return c;
    }
}
