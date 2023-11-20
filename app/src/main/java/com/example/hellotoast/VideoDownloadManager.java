package com.example.hellotoast;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class VideoDownloadManager {

    public static String downloadVideo(Context context, String videoUrl, String title) {
        Log.d("Httpd", "Attempting to download video");
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(videoUrl));
        Log.d("Httpd", "Request made");
        request.setTitle(title);
        request.setDescription("Downloading video");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        // Set the destination path for the downloaded video
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/video/" + title);

        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
        }
        return Environment.DIRECTORY_DOWNLOADS.toString() + "/video/" + title;
    }
}

