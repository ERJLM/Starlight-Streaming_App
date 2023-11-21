package org.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.apache.tomcat.jni.Proc;
import org.example.Chunk;
import org.example.DatabaseManager;
import org.example.FileManager;
import org.example.FileUploader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@WebServlet(urlPatterns = "/uploadServlet")
@MultipartConfig(
        fileSizeThreshold = 1024*1024*1000, //1000 MB
        maxFileSize = 1024*1024*1000, //1000 MB
        maxRequestSize = 1024*1024*1000 //1000 MB
)
public class SimpleServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Define Resolutions
        String[] resolutions = {"360","1080"};

        //Define default path
        String default_path = "C:\\Upload\\";
        String storage_volume_path = "http://172.20.10.4:80/";
        String storage_volume_servlet = "http://localhost:6666/storageVolumeServlet";
        String playlist_file = "playlist.m3u8";

        //Receive movie
        Part name = request.getPart("Name");
        Part movie = request.getPart("Movie");
        String file_path = default_path+LocalDateTime.now().getNano()+"_"+movie.getSubmittedFileName();
        file_path = file_path.replace(' ','_');
        movie.write(file_path);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(name.getInputStream(), StandardCharsets.UTF_8));
        String file_name = bufferedReader.readLine();

        //Prepare Environment
        String dir_path = default_path+"TMP_"+LocalDateTime.now().getNano();
        File dir = FileManager.makeDir(dir_path);

        for(String resolution:resolutions)
        {
            //Generate chunks with FFMPEG
            String command = "ffmpeg -nostats -loglevel 0 -i "+file_path+" -vf scale=-1:"+resolution+" -f hls -hls_time 3 -hls_playlist_type vod -hls_flags independent_segments -hls_segment_type mpegts -hls_segment_filename "+dir_path+"\\data%03d.ts "+dir_path+"\\playlist.m3u8";
            Process process = Runtime.getRuntime().exec(command);
            try
            {
                process.waitFor();
            }
            catch (Exception exception)
            {
                System.out.println(exception.getMessage());
                throw new RuntimeException(exception);
            }

            //Partial Movie Insertion in Database
            int movie_id = DatabaseManager.partialMovieInsertion(file_name+" ("+resolution+"p)");
            String movie_path = "Video_"+movie_id;

            //Upload chunks and playlist file to the Storage Volume (NGINX)
            FileUploader.upload(storage_volume_servlet,movie_path,dir.listFiles());

            //Complete Movie Insertion
            String link = storage_volume_path+movie_path+"/"+playlist_file;
            DatabaseManager.completeMovieInsertion(movie_id,link, Chunk.getChunks(movie_id,dir.listFiles()));

            //Clean TMP DIRECTORY
            FileManager.deleteFiles(dir.listFiles());
        }

        //Remove Original file and TMP DIRECTORY
        FileManager.deleteFile(new File(file_path));
        FileManager.deleteFile(dir);

        System.out.println("DONE!"+file_path);
        response.getWriter().print("OK");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();

        writer.println("<html><title>Welcome, everyone!</title><body>");
        writer.println("<h1>Have a Great Day!</h1>");
        writer.println("</body></html>");
    }
}
