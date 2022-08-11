package com.example.examfeverr;

import java.util.Date;

public class ChatModel {

    String message;
    String userid;
    String username;
    long timestamp;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ChatModel() {
    }

    public ChatModel(String message, String userid, String username, long timestamp) {
        this.message = message;
        this.userid = userid;
        this.username = username;
        this.timestamp = timestamp;
    }




}
