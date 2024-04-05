# Starlight

## Project within the scope of the Mobile Device Programming course by Eliandro Melo and Patrick Daniel.

### Android App
##### Important points:
###### We use:
- Exoplayer for video player.
- NanoHTTPD as an embedded server for the app.
- Retrofit to make app requests to the Application Server.
- The SHA256 algorithm to calculate and verify the hashes of movie chunks.

### Cloud Services

Services developed in a Windows environment (11) and tested in a Linux environment (Ubuntu 22.04 LTS).

#### Application-Server-API-Starlight (Request_solver):
Server (Jetty) with the REST API (using Jersey) on port 8080 of VM1 (34.170.81.106) in the Cloud, responsible for handling requests involving data in the database.

#### Application-Server-File-Uploader-Starlight (File_Receiver_Uploader):
Server (Tomcat) with a Servlet on port 9090 of VM1 (34.170.81.106) in the Cloud, responsible for handling video uploads (that is, receiving the videos, running FFMPEG, generating SHA-256 hashes, inserting them into the DB and sending the chunks to the Storage Volume "on VM2").

#### Storage-Volume-Starlight (Storage_Volume_File_Receiver):
Server (Tomcat) with a servlet on port 6666 of VM2 (34.39.11.61) in the Cloud, responsible for receiving the video chunks and placing them in the correct Storage Volume directory (NGINX).

#### Database-Starlight:
SQL database on VM1 with 3 tables (user_tbl, movie_tbl and chunk_tbl).

 
###### All modules were compiled with Maven.
