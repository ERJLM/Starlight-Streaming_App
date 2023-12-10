package com.example.hellotoast;

public class MovieSlide {

    private int Image ;
    private String Title;
    // Add more field depand on whay you wa&nt ...


    public MovieSlide(int image, String title) {
        Image = image;
        Title = title;
    }


    public int getImage() {
        return Image;
    }

    public String getTitle() {
        return Title;
    }

    public void setImage(int image) {
        Image = image;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
