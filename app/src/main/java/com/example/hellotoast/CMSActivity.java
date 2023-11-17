package com.example.hellotoast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CMSActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms);


        Button buttonMovies = findViewById(R.id.button_movies);
        Button buttonUsers = findViewById(R.id.button_users);



        buttonMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start ManageMoviesActivity
                Intent intent = new Intent(CMSActivity.this, MovieSelectorActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener for the "Manage Users" button
        buttonUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start ManageUsersActivity
                Intent intent = new Intent(CMSActivity.this, ManageUsersActivity.class);
                startActivity(intent);
            }
        });


    }
}
