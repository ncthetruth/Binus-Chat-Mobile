package com.example.binucchatmobile.model;

import com.google.firebase.Timestamp;

public class UserModel {
    private String nim;
    private String username;
    //    private Timestamp createdTimestamp;
    private String userId;
    private String email;
    private String binusianId;
//    private String fcmToken;

    public UserModel() {
    }

    public UserModel(String nim, String username,String userId, String email, String binusianId) {
        this.nim = nim;
        this.username = username;
        this.userId = userId;
        this.email = email;
        this.binusianId = binusianId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBinusianId() {
        return binusianId;
    }

    public void setBinusianId(String binusianId) {
        this.binusianId = binusianId;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
