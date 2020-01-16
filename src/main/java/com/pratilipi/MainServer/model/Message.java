package com.pratilipi.MainServer.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
public class Message {

    private String id;
    private String from_user;
    private String to_user;
    private String text;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime timeStamp;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTo_user() {
        return to_user;
    }

    public void setTo_user(String to_user) {
        this.to_user = to_user;
    }

    public String getFrom_user() {
        return from_user;
    }

    public void setFrom_user(String from_user) {
        this.from_user = from_user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getTimestamp() {
        return timeStamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timeStamp = timestamp;
    }
}
