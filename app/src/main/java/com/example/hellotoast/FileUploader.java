package com.example.hellotoast;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

import android.os.AsyncTask;
import android.util.Log;
public class FileUploader {
    //static String url = "http://localhost:9090/uploadServlet";
    static String url = "http://172.20.10.4:9090/uploadServlet";
    static String charset = "UTF-8";


    //"sdcard/Download/file.m3u8");
    static String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    static String CRLF = "\r\n"; // Line separator required by multipart/form-data.

    static public void upload(File binaryFile, String param) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                Log.d("FILE_UPLOADER", "Step 0");
                try {
                    URLConnection connection = new URL(url).openConnection();
                    Log.d("FILE_UPLOADER", "Step 1");
                    connection.setDoOutput(true);
                    Log.d("FILE_UPLOADER", "Step 2");
                    connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    Log.d("FILE_UPLOADER", "Step 3");
                    OutputStream output = connection.getOutputStream();
                    Log.d("FILE_UPLOADER", "Step 4");
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
                    Log.d("FILE_UPLOADER", "Step 5");

                    //Send normal param.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"Name\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(param).append(CRLF).flush();

                    // Send binary file.
                    writer.append("--" + boundary).append(CRLF);
                    Log.d("FILE_UPLOADER", "Step 6");
                    writer.append("Content-Disposition: form-data; name=\"Movie\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
                    writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
                    writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                    Log.d("FILE_UPLOADER", "Step 7");
                    writer.append(CRLF).flush();
                    Log.d("FILE_UPLOADER", "Step 8");
                    Files.copy(binaryFile.toPath(), output);
                    Log.d("FILE_UPLOADER", "Step 9");
                    output.flush(); // Important before continuing with writer!
                    Log.d("FILE_UPLOADER", "Step 10");
                    writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.
                    Log.d("FILE_UPLOADER", "Step 11");
                    // End of multipart/form-data.
                    writer.append("--" + boundary + "--").append(CRLF).flush();
                    Log.d("FILE_UPLOADER", "Step 12");

                    // Request is lazily fired whenever you need to obtain information about response.
                    int responseCode = ((HttpURLConnection) connection).getResponseCode();
                    Log.d("FILE_UPLOADER", "Step 13");
                    System.out.println(responseCode); // Should be 200

                    Log.w("FILE_UPLOADER", String.valueOf(responseCode));
                } catch (Exception e) {
                    Log.e("FILE_UPLOADER", "ERRO: " + e.toString());
                    System.out.println(e.getMessage());
                }
                return null;
            }
        }.execute();
    }
}


