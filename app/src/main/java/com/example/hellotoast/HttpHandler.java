package com.example.hellotoast;
import android.os.AsyncTask;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHandler {

    public interface HttpCallback {
        void onResponse(String response);
        void onError(Exception e);
    }

    public static void sendGetRequest(String urlString, HttpCallback callback) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... params) {
                try {
                    URL url = new URL(params[0]);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set up HTTP GET request
                    connection.setRequestMethod("GET");
                    connection.connect();

                    // Read response from the server
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    // Close the connection
                    connection.disconnect();

                    return response.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                if (result != null) {
                    callback.onResponse(result);
                }
            }
        }.execute(urlString);
    }
}

