package com.example.hellotoast;

import com.example.hellotoast.R;

import java.io.Serializable;

public class Movie implements Serializable {

    private int id;
    private String title;
    private String description;
    private Boolean seeded;
    private int coverPhoto = R.drawable.starlight_logo2;
    private String streamingLink;


    public Movie(int id,Boolean seeded, String title, String streamingLink) {
        this.id = id;
        this.title = title;
        this.streamingLink = streamingLink;
        this.seeded = seeded;
    }


    public Movie(int id, String title,Boolean seeded, int coverPhoto, String streamingLink) {
        this.id = id;
        this.title = title;
        this.coverPhoto = coverPhoto;
        this.streamingLink = streamingLink;
        this.seeded = seeded;
    }


    public Movie(int id, String title, String description, int coverPhoto, String streamingLink) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coverPhoto = coverPhoto;
        this.streamingLink = streamingLink;
        this.seeded = false;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isSeeded() {
        return seeded;
    }

    public void setSeeded(Boolean seeded) {
        this.seeded = seeded;
    }

    public int getCoverPhoto() {
        return coverPhoto;
    }

    public void setCoverPhoto() {
        this.coverPhoto = R.drawable.starlight_logo2;
    }

    public String getStreamingLink() {
        return streamingLink;
    }

    public void setStreamingLink(String streamingLink) {
        this.streamingLink = streamingLink;
    }
}