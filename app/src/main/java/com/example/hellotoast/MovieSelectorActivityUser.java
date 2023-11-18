package com.example.hellotoast;

import android.app.ActivityOptions;
import android.content.Intent;


import android.os.Build;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MovieSelectorActivityUser extends AppCompatActivity implements MovieItemClickListener {


    private List<Slide> lstSlides;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV;
    private String videourl = "http://www.alunos.dcc.fc.up.pt/~up202000411/file.m3u8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selector_user);



        // Setup Slider
        sliderpager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);


        // Prepare list of slides
        lstSlides = new ArrayList<>();
        lstSlides.add(new Slide(R.drawable.slide1, "Wolverine \nNext Year"));
        lstSlides.add(new Slide(R.drawable.slide2, "Detectives \nNext Year"));
        lstSlides.add(new Slide(R.drawable.slide1, "Wolverine \nNext Year"));
        lstSlides.add(new Slide(R.drawable.slide2, "Detectives \nNext Year"));

        // Create and set SliderPagerAdapter
        SliderPagerAdapter adapter = new SliderPagerAdapter(this, lstSlides);
        sliderpager.setAdapter(adapter);

        // Setup timer for automatic slide transitions
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 4000, 6000);

        // Connect sliderpager and indicator
        indicator.setupWithViewPager(sliderpager, true);

        // Setup Movies RecyclerView
        MoviesRV = findViewById(R.id.Rv_movies);


        // Prepare list of movies
        List<Movie> lstMovies = new ArrayList<>();
        lstMovies.add(new Movie(0,false,"Moana",  videourl));


        // Create and set MovieAdapter
        MovieAdapter movieAdapter = new MovieAdapter(this, lstMovies, MovieSelectorActivityUser.this);
        MoviesRV.setAdapter(movieAdapter);

        // Set RecyclerView layout manager
        MoviesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        // Handle movie item click

        // Create intent to launch MovieDetailActivity
        Intent intent = new Intent(MovieSelectorActivityUser.this, MovieDetailActivity.class);

        // Send movie information to MovieDetailActivity
        intent.putExtra("movie", movie);
        //startActivity(intent);
        // Create shared element transition animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieSelectorActivityUser.this,
                movieImageView, "sharedName");

        // Start MovieDetailActivity with shared element transition animation
        startActivity(intent, options.toBundle());

        // Toast message for debugging
        Toast.makeText(this, "Item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();

    }

    class SliderTimer extends TimerTask {


        @Override
        public void run() {

            MovieSelectorActivityUser.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (sliderpager.getCurrentItem() < lstSlides.size() - 1) {
                        sliderpager.setCurrentItem(sliderpager.getCurrentItem() + 1);
                    } else
                        sliderpager.setCurrentItem(0);
                }
            });


        }
    }

}