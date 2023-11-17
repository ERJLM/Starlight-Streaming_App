package com.example.hellotoast;

import java.io.Serializable;

public class Movie implements Serializable {

    private int id;
    private String title;
    private String description;
    private boolean seeded;
    private String coverPhoto;
    private String streamingLink;


    public Movie(int id, String title, String streamingLink) {
        this.id = id;
        this.title = title;
        this.streamingLink = streamingLink;
        this.seeded = false;
    }


    public Movie(int id, String title, String coverPhoto, String streamingLink) {
        this.id = id;
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.streamingLink = streamingLink;
        this.seeded = false;
    }


    public Movie(int id, String title, String description, String coverPhoto, String streamingLink) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.streamingLink = streamingLink;
        this.seeded = false;
    }


    public String getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto(String coverPhoto) {
        this.coverPhoto = coverPhoto;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }



    public String getStreamingLink() {
        return streamingLink;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStreamingLink(String streamingLink) {
        this.streamingLink = streamingLink;
    }

    public int getId() {
        return id;
    }

    public boolean isSeeded() {
        return seeded;
    }

    public void setSeeded(boolean seeded) {
        this.seeded = seeded;
    }
}