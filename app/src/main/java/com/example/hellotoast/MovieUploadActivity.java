package com.example.hellotoast;

import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class MovieUploadActivity extends AppCompatActivity {
    private EditText movie_nameEdt;
    String movieName;
    private User user;

    ActivityResultLauncher<PickVisualMediaRequest> launcher = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), new ActivityResultCallback<Uri>() {
        @Override
        public void onActivityResult(Uri uri) {
            if (uri == null) {
                Toast.makeText(MovieUploadActivity.this, "No video is selected", Toast.LENGTH_SHORT).show();

            } else {
                try {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MovieUploadActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to upload this movie?");

                    // Add buttons for confirmation
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            File file = new File(getFilePathFromContentUri(uri));

                            upload(file, movieName);
                            Toast.makeText(MovieUploadActivity.this, "Movie Uploaded", Toast.LENGTH_SHORT).show();
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
        user = (User)getIntent().getSerializableExtra("user");


       // String mimeType = "video/mp4";
        pickVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                movieName = movie_nameEdt.getText().toString();
                if(!movieName.trim().equals("")) {
                    launcher.launch(new PickVisualMediaRequest.Builder()
                            .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                            .build());
                }
                else Toast.makeText(MovieUploadActivity.this, "Insert a valid name for the movie", Toast.LENGTH_SHORT).show();

            }
        });

        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent to start CMSActivity
                Intent intent = new Intent(MovieUploadActivity.this, MovieSelectorActivity.class);
                intent.putExtra("user", user);
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


        //static String url = "http://localhost:9090/uploadServlet";
         String url = "http://34.170.81.106:9090/uploadServlet";
         String charset = "UTF-8";


        //"sdcard/Download/file.m3u8");
          String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
          String CRLF = "\r\n"; // Line separator required by multipart/form-data.
         public void upload(File binaryFile, String param) {
            new AsyncTask<String, Void, String>() {
                @Override
                protected String doInBackground(String... params) {
                    MyDialogFragment loadingDialog = new MyDialogFragment();
                    loadingDialog.show(getSupportFragmentManager(), "dialog");
                    Log.d("MOVIEUPLOAD", "Step 0");
                    try {
                        URLConnection connection = new URL(url).openConnection();

                        connection.setDoOutput(true);

                        connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

                        OutputStream output = connection.getOutputStream();

                        PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);


                        //Send normal param.
                        writer.append("--" + boundary).append(CRLF);
                        writer.append("Content-Disposition: form-data; name=\"Name\"").append(CRLF);
                        writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
                        writer.append(CRLF).append(param).append(CRLF).flush();

                        // Send binary file.
                        writer.append("--" + boundary).append(CRLF);

                        writer.append("Content-Disposition: form-data; name=\"Movie\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
                        writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
                        writer.append("Content-Transfer-Encoding: binary").append(CRLF);

                        writer.append(CRLF).flush();

                        Files.copy(binaryFile.toPath(), output);

                        output.flush(); // Important before continuing with writer!

                        writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

                        // End of multipart/form-data.
                        writer.append("--" + boundary + "--").append(CRLF).flush();


                        // Request is lazily fired whenever you need to obtain information about response.
                        int responseCode = ((HttpURLConnection) connection).getResponseCode();
                        System.out.println(responseCode); // Should be 200
                        if (loadingDialog != null && loadingDialog.isVisible()) {
                            loadingDialog.dismiss();
                        }
                        Log.w("MOVIEUPLOAD", String.valueOf(responseCode));
                    } catch (Exception e) {
                        Log.e("MOVIEUPLOAD", "ERRO: " + e.toString());
                        System.out.println(e.getMessage());
                    }
                    return null;
                }
            }.execute();
        }


}
