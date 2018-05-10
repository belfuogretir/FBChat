package com.example.belfu.fbchat;

/**
 * Created by belfu on 14.02.2018.
 */

public class User {
    private String email,oneSignalId;
    public User(){}

    public User(String email, String oneSignalId){
        this.email = email;
        this.oneSignalId = oneSignalId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOneSignalId() {
        return oneSignalId;
    }

    public void setOneSignalId(String oneSignalId) {
        this.oneSignalId = oneSignalId;
    }
}
