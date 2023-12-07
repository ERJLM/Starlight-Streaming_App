package com.example.hellotoast;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView backgroundImage = findViewById(R.id.imageView);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.reverse_fade);
        backgroundImage.startAnimation(slideAnimation);
        // Create a new thread to delay the transition to the main activity
        new Thread(() -> {
            try {
                Thread.sleep(2000); // 2 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }).start();
    }
}