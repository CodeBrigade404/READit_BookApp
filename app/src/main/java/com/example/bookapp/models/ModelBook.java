package com.example.bookapp.models;

public class ModelBook {

    String id,book,uid;
    long timestamp;

    public ModelBook() {
    }

    public ModelBook(String id, String book, String uid, long timestamp) {
        this.id = id;
        this.book = book;
        this.uid = uid;
        this.timestamp = timestamp;
    }

    /*Getters/Setters*/

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
