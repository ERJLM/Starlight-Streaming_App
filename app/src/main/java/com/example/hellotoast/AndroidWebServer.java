package com.example.hellotoast;

import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Url;

/**
 * Created by Mikhael LOPEZ on 14/12/2015.
 */
public class AndroidWebServer extends NanoHTTPD implements Serializable {

    private int movie_id;
    private String movie_url;

    private String[] expectedHash= new String[1];
    private static boolean[] check = new boolean[1];

    private static AndroidWebServer singleton = null;
    private static HashMap<String, String> SHAMap = new HashMap<>();

    public AndroidWebServer() {
        super(8080);
        singleton = this;
    }

    public AndroidWebServer(int port) {
        super(port);
        singleton = this;
    }

    public static AndroidWebServer getInstance()
    {
        return singleton;
    }
    @Override
    public Response serve(IHTTPSession session)
    {
        Log.w("URI: ",session.getUri());
        Log.w("Headers: ",session.getHeaders().toString());
        Log.w("Params: ",session.getParms().toString());

        return answer(session.getUri());
    }

    public Response answer(String uri)
    {
        Response res = null;
        if(uri.contains("ping")){
            res = newFixedLengthResponse(Response.Status.OK, "*/*", null,0);
        }
        else if(uri.contains("server")){
            uri = uri.replace("/server", "");
            File file = new File("/sdcard/Download/video"+uri);

            try {

                if (uri.contains(".m3u8"))
                    res = newFixedLengthResponse(Response.Status.OK, "application/vnd.apple.mpegurl", new FileInputStream(file), (int) file.length());
                else
                    res = newFixedLengthResponse(Response.Status.OK, "video/mp2t", new FileInputStream(file), (int) file.length());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            return res;
        }
        else {
            try {
                Log.i("AWS SERA Q ENTROU", "step0");
                URL url = new URL(movie_url.replace("/playlist.m3u8", "/server")  + uri);
                Log.i("AWS movieUrl", movie_url.replace("/playlist.m3u8", "/server")  + uri);
                Log.i("AWS My URI", "http://localhost:8080/server" + uri);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");

                int responseCode = connection.getResponseCode();
                Log.i("AWS SERA Q ENTROU", "Before Response");
                if(responseCode == HttpURLConnection.HTTP_OK){
                    String filename =  uri.substring(uri.lastIndexOf('/')+1);
                    Log.i("AWS ENTROU", "Response OK");
                    check[0] = false;
                    Log.i("AWS ENTROU MovieId", String.valueOf(movie_id));
                    Log.i("AWS ENTROU MovieUrl", movie_url);
                    InputStream inStream =connection.getInputStream();
                    File file = new File("/sdcard/Download" + uri);
                    FileUtils.copyInputStreamToFile(inStream, file);
                    if(!SHAMap.containsKey(filename)) {
                        requestListHash(movie_id, filename);
                        //while (!check[0]) ;
                    }
                    if(!SHA256.getHash(file).equals(SHAMap.get(filename))){
                        return res;
                    }
                    if (uri.contains(".m3u8"))
                        res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, "application/vnd.apple.mpegurl", new FileInputStream(file), (int) file.length());
                    else
                        res = newFixedLengthResponse(Response.Status.OK, "video/mp2t", new FileInputStream(file), (int) file.length());
                }

            } catch (Exception e) {
                Log.d("AWS ERROR", e.toString());
                throw new RuntimeException(e);
            }

        }
        return res;
    }

    public void setMovie_info(int id, String url) {
        this.movie_id = id;
        this.movie_url = url;
    }


    private void requestListHash(int id, String filename) {
        Call<List<Hash>> call =RetrofitClient.getMovieApi().get_hashes(id, filename);
        String TAG = "RequestListHash";
        Log.d(TAG, "Entered Request");

        try {
            retrofit2.Response<List<Hash>> response = call.execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                Log.d(TAG, "Response was successful");
                for (Hash h : response.body()) {
                    String name = h.getFilename();
                    String fileHash = h.getHash();
                    if(fileHash.trim() == null) Log.i(TAG, "Hash é nula");
                    SHAMap.put(name, fileHash);
                    Log.d(TAG + "hash", fileHash);
                    Log.d(TAG + "name", name);
                    //Log.w("ExpectedHashForChunk", expectedHash[0]);
                }

                check[0] = true;
            }   else {
                Log.e(TAG, "Failed to retrieve list of hash");
            }
        } catch (IOException e) {
            Log.e(TAG, "Error in Retrofit request", e);
        }

    }



}
