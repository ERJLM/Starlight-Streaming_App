package com.example.hellotoast;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class MovieDownloadManager {

    public static String downloadVideo(Context context, String videoUrl, String title) {
        Log.d("MOVIEDOWNLOADMANAGER", "Attempting to download video");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
        Log.d("MOVIEDOWNLOADMANAGER", "Request made");
        request.setTitle(title);
        request.setDescription("Downloading video");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_HIDDEN);

        // Set the destination path for the downloaded video
        String path = "/sdcard/Download/video/" + title;
        deleteFile(path);
        Log.d("MOVIEDOWNLOADMANAGER", path);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/video/" + title);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
        return Environment.DIRECTORY_DOWNLOADS.toString() + "/video/" + title;
    }

    private static boolean deleteFile(String path){

        File folder1 = new File(path);
        return folder1.delete();


    }
}

