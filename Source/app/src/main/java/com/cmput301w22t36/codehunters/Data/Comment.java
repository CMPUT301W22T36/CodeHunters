package com.cmput301w22t36.codehunters.Data;

public class Comment {
    private String user;
    private String message;

    public Comment(String user, String message) {
        this.user = user;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
