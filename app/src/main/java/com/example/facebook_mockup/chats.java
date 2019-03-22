package com.example.facebook_mockup;


import com.google.firebase.Timestamp;

import java.util.Date;

public class chats implements Comparable<chats> {
    private String sender;
    private String content;
    private Date timestamp;

    public Date getTimestamp() {
        return timestamp;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public chats(String sender, String content, long timestamp) {
        this.sender = sender;
        this.content = content;
        this.timestamp = new Date(timestamp);
    }

    public chats(String sender, String content) {
        this.sender = sender;
        this.content = content;
        this.timestamp = new Date();
    }

    @Override
    public int compareTo(chats o) {
        return this.timestamp.compareTo(o.getTimestamp());
    }
}
