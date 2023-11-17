package com.example.hellotoast;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class UploadMovieActivity extends AppCompatActivity {
    private EditText movie_nameEdt;
    String movieName;

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri == null) {
                Toast.makeText(UploadMovieActivity.this, "No video is selected", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    File file = new File(getFilePathFromContentUri(uri));

                    FileUploader.upload(file, movieName);
                }
                catch(Exception e){

                }
            }
        }
    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Button pickVideo = findViewById(R.id.button_add);
        Button back_button = findViewById(R.id.button_back);
        movie_nameEdt = findViewById(R.id.idEdtMovie1);


       // String mimeType = "video/mp4";
        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieName = movie_nameEdt.getText().toString();
                launcher.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                        .build());

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(UploadMovieActivity.this, MovieSelectorActivity.class);
                startActivity(intent);
            }
        });
    }

    private String getFilePathFromContentUri(Uri contentUri) {
        String filePath = null;
        try {
            String originalExtension = getFileExtension(contentUri);

            InputStream inputStream = getContentResolver().openInputStream(contentUri);
            if (inputStream != null) {
                File tempFile = File.createTempFile("temp_video", "." + originalExtension, getCacheDir());
                FileUtils.copyInputStreamToFile(inputStream, tempFile);
                filePath = tempFile.getAbsolutePath();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension;

        if ("content".equals(uri.getScheme())) {
            extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        } else {
            extension = mimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());
        }

        return extension != null ? extension : "";
    }


}
