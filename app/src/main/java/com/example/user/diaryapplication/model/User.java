package com.example.user.diaryapplication.model;

/**
 * Created by timi on 28/06/2018.
 */

public class User {
    private String user_id;
    private String userEmail;
    private String userName;

    public User() {
    }

    public User(String user_id, String userEmail, String userName) {
        this.user_id = user_id;
        this.userEmail = userEmail;
        this.userName = userName;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
