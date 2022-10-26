package com.example.bookapp;

public class ModelPdf {
 //variables
    String uid,id,title,description,categoryId,url;
    long timestamp;

    //empty constructor
    public ModelPdf() {
    }

    public ModelPdf(String uid, String id, String title, String description, String categoryId, String url, long timestamp) {
        this.uid = uid;
        this.id = id;
        this.title = title;
        this.description = description;
        this.categoryId = categoryId;
        this.url = url;
        this.timestamp = timestamp;
    }

    //---------------Getters/Setters---------



}
