package org.example;

import java.io.File;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;

public class FileUploader {
    static String charset = "UTF-8";
    static String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    static String CRLF = "\r\n"; // Line separator required by multipart/form-data.

    static public void upload(String url,String path,File[] files)
    {
        try
        {
            URLConnection connection = new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = connection.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);

            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"movie_path\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(path).append(CRLF).flush();

            for(File file:files)
            {
                writer.append("--" + boundary).append(CRLF);
                writer.append("Content-Disposition: form-data; name=\""+file.getName()+"\"; filename=\"" + file.getName() + "\"").append(CRLF);
                writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(file.getName())).append(CRLF);
                writer.append("Content-Transfer-Encoding: binary").append(CRLF);
                writer.append(CRLF).flush();
                Files.copy(file.toPath(), output);
                output.flush();
                writer.append(CRLF).flush();
            }

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();

            // Request is lazily fired whenever you need to obtain information about response.
            int responseCode = ((HttpURLConnection) connection).getResponseCode();
            System.out.println("Storage Volume Response: "+responseCode); // Should be 200*/
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
