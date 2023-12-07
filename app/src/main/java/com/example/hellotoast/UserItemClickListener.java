package com.example.hellotoast;

import android.widget.ImageView;

public interface UserItemClickListener {

    void onMovieClick(User user, ImageView movieImageView); // we will need the imageview to make the shared animation between the two activity

}
