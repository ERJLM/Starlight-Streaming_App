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
                Log.i("HERIOO SERA Q ENTROU", "step0");
                URL url = new URL("http://localhost:8080/server" + uri);
                Log.i("HERIOO My URI", "http://localhost:8080/server" + uri);
                Log.i("HERIOO SERA Q ENTROU", "step1");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                Log.i("HERIOO SERA Q ENTROU", "step2");
                connection.setRequestMethod("GET");
                Log.i("HERIOO SERA Q ENTROU", "step3");

                int responseCode = connection.getResponseCode();
                Log.i("HERIOO SERA Q ENTROU", "HERR");
                if(responseCode == HttpURLConnection.HTTP_OK){
                    String filename =  uri.substring(uri.lastIndexOf('/')+1);
                    Log.i("HERIOO ENTROU", "HERR");
                    check[0] = false;
                    Log.i("HERIOO ENTROU Android", String.valueOf(movie_id));
                    Log.i("HERIOO ENTROU Android2", movie_url);
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
                Log.d("HERIOO3rror", e.toString());
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
        String TAG = "RequestFileHash";
        Log.d(TAG, "HASHSHSH");
        Log.i(TAG, String.valueOf(id));
        try {
            retrofit2.Response<List<Hash>> response = call.execute();

            if (response.isSuccessful()) {
                assert response.body() != null;
                for (Hash h : response.body()) {
                    String name = h.getFilename();
                    String fileHash = h.getHash();
                    //if(fileHash == null) Log.i(TAG, "Hash é nula");
                    SHAMap.put(name, fileHash);
                    Log.e(TAG, fileHash);
                    //Log.w("ExpectedHashForChunk", expectedHash[0]);
                }

                check[0] = true;
            }   else {
                Log.e(TAG, "Failed to retrieve file hash");
            }
        } catch (IOException e) {
            Log.e("RequestMovie", "Error in Retrofit request", e);
        }

    }



}
