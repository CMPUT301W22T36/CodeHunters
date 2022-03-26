package com.cmput301w22t36.codehunters.Data.DataMappers;

import androidx.annotation.NonNull;

import com.cmput301w22t36.codehunters.Data.DataMapper;
import com.cmput301w22t36.codehunters.Data.DataTypes.Comment;
import com.cmput301w22t36.codehunters.Data.DataTypes.User;

import java.util.ArrayList;
import java.util.HashMap;
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
