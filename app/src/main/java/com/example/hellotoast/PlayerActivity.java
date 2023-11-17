package com.example.hellotoast;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;


import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class PlayerActivity extends AppCompatActivity {
    private ExoPlayer player;
    //AppServer appServer;
    String videoUrl ;
    Button button_download;
    private TextView mShowCount;
    private boolean confirm;
    private String download_link;
    private int num_of_chunks;
    private User user;
    private Movie movie;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        button_download = (Button) findViewById(R.id.button_download);
        user = (User)getIntent().getSerializableExtra("user");
        movie = (Movie)getIntent().getSerializableExtra("movie");

        videoUrl = movie.getStreamingLink();
        button_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                download_request();

                String videoTitle = "movie";
                //String file = "file.m3u8";
                VideoDownloadManager.downloadVideo(PlayerActivity.this, videoUrl, videoTitle);
                Log.d("BUTTONS", "User tapped the button");
            }
        });



        initializePlayer();
        Log.d("MyApp", "Activity created");
    }

    private void download_request() {
        Call<Seed_Request> call = RetrofitClient.getUserApi().seed(user.getIp(), user.getId(), movie.getId());
        call.enqueue(new Callback<Seed_Request>() {
            @Override
            public void onResponse(Call<Seed_Request> call, Response<Seed_Request> response) {
                if (response.isSuccessful()) {
                    Seed_Request  result = response.body();
                    // Do something with the list of users...
                    if (result != null) {
                         download_link = result.getLink_de_download();
                         num_of_chunks = result.getNum_of_chunks();
                         confirm = result.isConfirm();
                    }
                } else {
                    Log.w("RequestSeed", "Response is null");
                }
            }

            @Override
            public void onFailure(Call<Seed_Request> call, Throwable t) {
                t.toString();
                Log.e("RequestSeed", t.toString());
            }
        });
    }

    @Override
    protected void onStop(){
        super.onStop();
        //appServer.stop();
        player.setPlayWhenReady(false);
        player.release();
        player = null;
    }
    private void initializePlayer() {
        PlayerView playerView = findViewById(R.id.playerView);
        player = new ExoPlayer.Builder(PlayerActivity.this).build();
        playerView.setPlayer(player);
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }

    public void showNetflix(View view) {
        Toast toast = Toast.makeText(this, R.string.netflix_message,
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void back(View view) {
        Intent intent = new Intent(PlayerActivity.this, MovieSelectorActivity.class);
        //intent.putExtra("username", userName);
        startActivity(intent);
    }

    public void changeVideo(View view) {
        videoUrl = "https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8";
        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.setPlayWhenReady(true);
    }



}