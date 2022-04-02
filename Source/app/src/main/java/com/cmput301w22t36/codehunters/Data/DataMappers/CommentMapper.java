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

public class CommentMapper extends DataMapper<Comment> {
    // Constants for document fields.
    enum Fields {
        USERREF("userRef"),
        QRCODEREF("qrCodeRef"),
        COMMENT("comment");

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

    public void getCommentsForQrCode(String qrId, CompletionHandler<ArrayList<Comment>> ch) {
        String qrRef = "/users/" + qrId;
        collectionRef.whereEqualTo("qrCodeRef", qrId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> documents= task.getResult().getDocuments();
                        ArrayList<Comment> comments = new ArrayList<Comment>();
                        for (DocumentSnapshot docSnap : documents) {
                            Comment c = mapToData(docSnap.getData());
                            c.setId(docSnap.getId());
                            comments.add(c);
                        }
                        ch.handleSuccess(comments);
                    } else {
                        ch.handleError(new FSAccessException("Username not unique or other error"));
                    }
                });
    }


    @Override
    protected Map<String, Object> dataToMap(Comment data) {
        Map<String, Object> map = new HashMap<>();
        map.put("userRef", data.getUserRef());
        map.put("qrCodeRef", data.getQrCodeRef());
        map.put("comment", data.getComment());
        return map;
    }

    @Override
    protected Comment mapToData(@NonNull Map<String, Object> dataMap) {
        // NOTE: Does not set the documentId!
        Comment c = new Comment();
        c.setUserRef(Objects.requireNonNull((String) dataMap.get(Fields.USERREF.toString())));
        c.setQrCodeRef(Objects.requireNonNull((String) dataMap.get(Fields.QRCODEREF.toString())));
        c.setComment(Objects.requireNonNull((String) dataMap.get(Fields.COMMENT.toString())));
        return c;
    }
}
