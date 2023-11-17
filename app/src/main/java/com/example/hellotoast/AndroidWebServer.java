package com.example.hellotoast;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

/**
 * Created by Mikhael LOPEZ on 14/12/2015.
 */
public class AndroidWebServer extends NanoHTTPD {

    public AndroidWebServer(int port) {
        super(port);
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
        File file = new File("/sdcard/Download/video"+uri);
        Response res = null;

        try
        {
            if(uri.contains(".m3u8"))
                res = newFixedLengthResponse(Response.Status.PARTIAL_CONTENT, "application/vnd.apple.mpegurl", new FileInputStream(file), (int)file.length());
            else
                res = newFixedLengthResponse(Response.Status.OK, "video/mp2t", new FileInputStream(file), (int) file.length());
        }
        catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        return res;
    }


}
