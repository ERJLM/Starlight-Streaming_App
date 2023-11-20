package com.example.hellotoast;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;


import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.analytics.AnalyticsListener;
import androidx.media3.exoplayer.source.LoadEventInfo;
import androidx.media3.exoplayer.source.MediaLoadData;
import androidx.media3.ui.PlayerView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        button_download = (Button) findViewById(R.id.button_download);
        user = (User)getIntent().getSerializableExtra("user");
        Log.w("USEROLL", String.valueOf(user));
        //user = new User(8, "root", "root", true, "http://ipatoa:8080");
        movie = (Movie)getIntent().getSerializableExtra("movie");

        videoUrl = movie.getStreamingLink();
        button_download.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                download_request();

            }
        });



        initializePlayer();
        Log.d("MyApp", "Activity created");
    }

    private void download_request() {
        Call<Seed_Request> call = RetrofitClient.getUserApi().seed(user.getId(), movie.getId());
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

                for (int i = 0; i < num_of_chunks; i++) {
                    String title = "data" + String.format("%03d", i) + ".ts";
                    String chunkUrl = download_link + "/" + title;
                    String localFilePath = VideoDownloadManager.downloadVideo(PlayerActivity.this, chunkUrl, title );
                    Log.w("RequestSeed", "chunk " + i + "downloaded with url: " + chunkUrl);
                }
                String movieUrl = download_link + "/playlist.m3u8";
                VideoDownloadManager.downloadVideo(PlayerActivity.this, movieUrl, "playlist.m3u8");
                Toast.makeText(this, "Movie was downloaded", Toast.LENGTH_SHORT).show();
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

        //Setting up chunk verification while playing
     /*   player.addAnalyticsListener(new AnalyticsListener() {
           @Override
            public void onLoadStarted(
                    EventTime eventTime,
                    LoadEventInfo loadEventInfo,
                    MediaLoadData mediaLoadData) {
                // Check the hash when a chunk is loaded
                int chunkNumber = getChunkNumber(loadEventInfo.uri.toString());
                if (chunkNumber >= 0) {
                    String localFilePath = getLocalFilePath(chunkNumber);
                    if (localFilePath != null) {
                        boolean isHashValid = checkChunkHash(localFilePath, expectedHashForChunk(chunkNumber));
                        if (!isHashValid) {
                            // Handle invalid hash during playback, e.g., retry or notify the user
                            finish();
                            Toast.makeText(PlayerActivity.this,"Invalid hash for chunk: " + String.valueOf(chunkNumber),Toast.LENGTH_SHORT).show();
                            // You may also want to handle skipping or stopping playback
                        }
                    }
                }
            }
        }); */

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

    private int getChunkNumber(String url) {
        String[] parts = url.split("/");
        String lastPart = parts[parts.length - 1];
        if (lastPart.startsWith("data") && lastPart.endsWith(".ts")) {
            try {
                return Integer.parseInt(lastPart.substring(4, lastPart.length() - 3));
            } catch (NumberFormatException e) {
                return -1; // Invalid chunk number format
            }
        }
        return -1; // Not a chunk URL
    }

    private String getLocalFilePath(int chunkNumber) {
        // Modify this method to return the local file path based on your file storage strategy
        // Example: Assume chunks are saved in the app's cache directory
        String fileName = "data" + String.format("%03d", chunkNumber) + ".ts";
        return Environment.DIRECTORY_DOWNLOADS.toString() + "/video/" + fileName;
    }


    private static String expectedHashForChunk(int chunkNumber) {
        // Calculate and return the expected SHA-256 hash for the given chunk number
        // Pedir ao server o expected chunkNumber;
        return null;
    }

    private boolean checkChunkHash(String localFilePath, String expectedHash) {
        // Implement hash verification logic here using your preferred algorithm
        // Example: SHA-256
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] fileData = FileUtils.readFileToByteArray(new File(localFilePath));
            byte[] hashBytes = md.digest(fileData);
            String actualHash = bytesToHex(hashBytes);
            return actualHash.equals(expectedHash);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    private String bytesToHex(byte[] bytes) {
        StringBuilder hexStringBuilder = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexStringBuilder.append(String.format("%02x", b));
        }
        return hexStringBuilder.toString();
    }



}