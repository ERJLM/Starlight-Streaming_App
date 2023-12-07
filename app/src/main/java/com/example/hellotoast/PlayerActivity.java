package com.example.hellotoast;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;


import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@UnstableApi
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
    private static boolean[] check = new boolean[1];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        button_download = (Button) findViewById(R.id.button_download);
        user = (User)getIntent().getSerializableExtra("user");
        Log.w("USEROLL", String.valueOf(user));
        movie = (Movie)getIntent().getSerializableExtra("movie");
        if(movie.isSeeded() || user.getSeeder() > 0) button_download.setVisibility(View.GONE);


        videoUrl = movie.getStreamingLink();
        Log.w("myLink22", videoUrl);

        if (videoUrl.contains(":8080")){
            Log.w("USEROLL", "YES1122");
            assert AndroidWebServer.getInstance() != null;
            Log.i("HERIOO ENTROU no Player", String.valueOf(movie.getId()));
            AndroidWebServer.getInstance().setMovie_info(movie.getId(), movie.getStreamingLink());
            videoUrl = "http://localhost:8080/playlist.m3u8";
        }
        Log.w("myLink22", videoUrl);
        button_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to be the seeder of this movie?");

                // Add buttons for confirmation
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        check[0] = false;
                        download_request();
                        //while(!check[0]){}
                    }
                });

                // Add button for cancellation
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // User clicked No, do nothing or handle accordingly
                    }
                });

                // Show the dialog
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });



        initializePlayer();
        Log.d("MyApp", "Activity created");
    }

    private void download_request(){
        Call<Seed_Request> call = RetrofitClient.getUserApi().seed(user.getId(), movie.getId());
        call.enqueue(new Callback<Seed_Request>(){
            @Override
            public void onResponse(Call<Seed_Request> call, Response<Seed_Request> response) {
                if (response.isSuccessful()) {
                    Seed_Request  result = response.body();
                    // Do something with the list of users...
                    if (result != null) {
                        check[0] = true;
                        download_link = result.getLink_de_download();
                        num_of_chunks = result.getNum_of_chunks();
                        confirm = result.isConfirm();
                        Log.w("RequestSeed", "Response was a Success");
                        Log.w("RequestSeedRes", download_link + "___" + num_of_chunks + "___" + confirm);
                        getResponse(download_link, num_of_chunks, confirm);
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

    private void getResponse(String downloadLink, int numOfChunks, boolean confirm) {

        if(confirm) {
            user.setSeeder(movie.getId());
            for (int i = 0; i < num_of_chunks; i++) {
                String title = "data" + String.format("%03d", i) + ".ts";
                String chunkUrl = download_link + "/" + title;
                String localFilePath = VideoDownloadManager.downloadVideo(PlayerActivity.this, chunkUrl, title );
                Log.w("RequestSeed", "chunk " + i + "downloaded with url: " + chunkUrl);
            }
            String movieUrl = download_link + "/playlist.m3u8";
            VideoDownloadManager.downloadVideo(PlayerActivity.this, movieUrl, "playlist.m3u8");
            Toast.makeText(this, "Movie was downloaded", Toast.LENGTH_SHORT).show();
            button_download.setVisibility(View.GONE);
        }
        else Toast.makeText(this, "Couldn't download movie", Toast.LENGTH_SHORT).show();

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
        Toast toast = Toast.makeText(this, "Welcome to Starlight",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void back(View view) {
        Intent intent = new Intent(PlayerActivity.this, MovieDetailActivity.class);
        intent.putExtra("movie",movie);
        intent.putExtra("user", user);
        startActivity(intent);
    }



}