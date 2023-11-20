package com.example.hellotoast;

import android.app.ActivityOptions;
import android.content.Intent;


import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieSelectorActivity extends AppCompatActivity implements MovieItemClickListener {


    private List<Slide> lstSlides;
    private ViewPager sliderpager;
    private TabLayout indicator;
    private RecyclerView MoviesRV;
    private User user;
    private MovieAdapter movieAdapter;
    private List<Movie> lstMovies;
    private String videourl = "http://www.alunos.dcc.fc.up.pt/~up202000411/file.m3u8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_selector);
        Button button_upload = findViewById(R.id.button_upload);
        Button back = findViewById(R.id.back_fab);
        user = (User)getIntent().getSerializableExtra("user");


        button_upload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MovieSelectorActivity.this, MovieUploadActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                //FileUploader.upload();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(MovieSelectorActivity.this, CMSActivity.class);
                startActivity(intent);
            }
        });

        // Setup Slider
        sliderpager = findViewById(R.id.slider_pager);
        indicator = findViewById(R.id.indicator);


        // Prepare list of slides
        lstSlides = new ArrayList<>();
        lstSlides.add(new Slide(R.drawable.slide1, "Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide2, "Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide1, "Slide Title \nmore text here"));
        lstSlides.add(new Slide(R.drawable.slide2, "Slide Title \nmore text here"));

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

       /* Call<List<Movie>> call = RetrofitClient.getMovieApi().getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    // Do something with the list of movies...
                } else {
                    // Handle error...
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                // Handle failure...
            }
        });*/

        // Prepare list of movies
        lstMovies = new ArrayList<>();


        // Create and set MovieAdapter
        movieAdapter = new MovieAdapter(this, lstMovies, MovieSelectorActivity.this);


        requestMovies();

        Log.w("RequestMovies", "Movies: " + movieAdapter.getMovies().toString());

        //lstMovies.add(new Movie(0,"Moana",  videourl));
        //lstMovies.add(new Movie(0,"Black Panther",  videourl));
        //lstMovies.add(new Movie(0,"The Martian",  videourl));
        //lstMovies.add(new Movie(0,"The Martian", videourl));
        //lstMovies.add(new Movie(0,"The Martian", videourl));
        //lstMovies.add(new Movie(0,"The Martian", videourl));

        // Set RecyclerView layout manager
        MoviesRV.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onMovieClick(Movie movie, ImageView movieImageView) {
        // Handle movie item click

        // Create intent to launch MovieDetailActivity
        Intent intent = new Intent(MovieSelectorActivity.this, MovieDetailActivity.class);

        // Send movie information to MovieDetailActivity
        intent.putExtra("movie", movie);
        intent.putExtra("user", user);
        // Create shared element transition animation
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MovieSelectorActivity.this,
                movieImageView, "sharedName");

        // Start MovieDetailActivity with shared element transition animation
        startActivity(intent, options.toBundle());

        // Toast message for debugging
        Toast.makeText(this, "Item clicked: " + movie.getTitle(), Toast.LENGTH_SHORT).show();

    }

        //Slider timer for the slider that shows movies
        class SliderTimer extends TimerTask {


            @Override
            public void run() {

                MovieSelectorActivity.this.runOnUiThread(new Runnable() {
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

    private void requestMovies() {
        Call<List<Movie>> call = RetrofitClient.getMovieApi().getMovies();
        call.enqueue(new Callback<List<Movie>>() {
            @Override
            public void onResponse(Call<List<Movie>> call, Response<List<Movie>> response) {
                if (response.isSuccessful()) {
                    List<Movie> movies = response.body();
                    // Do something with the list of users...
                    if (movies != null) {
                        // Update the adapter with the new list of users
                        movieAdapter.setMovies(movies);
                        MoviesRV.setAdapter(movieAdapter);
                        Log.w("RequestMovies", "Response received: " + movies.toString());
                    }
                } else {
                    Log.w("RequestMovies", "Response is null");
                }
            }

            @Override
            public void onFailure(Call<List<Movie>> call, Throwable t) {
                t.toString();
                Log.e("RequestMovies", t.toString());
            }
        });
    }

}