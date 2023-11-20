package com.example.hellotoast;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;


// MovieApi.java
     interface MovieApi {
        @GET("request/listar_movies")
        Call<List<Movie>> getMovies();

        @POST("request/apagar_movies")
        @FormUrlEncoded
        Call<Boolean> del_movie(
                @Field("id") int id
        );

       // @GET("movies/{id}")
       // Call<Movie> getMovieById(@Path("id") int movieId);

        // Other movie-related endpoints
    }

    // UserApi.java
     interface UserApi {
        @GET("request/listar_users")
        Call<List<User>> getUsers();

        @POST("request/login")
        @FormUrlEncoded
        Call<Login_Request> login(
                @Field("ip") String ip,
                @Field("username") String username,
                @Field("password") String password
        );

        @POST("request/Adicionar_user")
        @FormUrlEncoded
        Call<addUser_Request> add_user(
                @Field("username") String username,
                @Field("password") String password,
                @Field("isAdmin") boolean isAdmin
        );

        @POST("request/Apagar_user")
        @FormUrlEncoded
        Call<Boolean> del_user(
                @Field("id") int id
                );

        @POST("request/Seed")
        @FormUrlEncoded
        Call<Seed_Request> seed(
                @Field("id_User") int id_User,
                @Field("id_Movie") int id_Movie
        );

       // @GET("users/{id}")
       // Call<User> getUserById(@Path("id") int userId);

        // Other user-related endpoints
    }

class addUser_Request {
    private boolean confirm;
    private int id;

    addUser_Request(boolean confirm, int id) {
        this.confirm = confirm;
        this.id = id;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public int getId() {
        return id;
    }
}

class Seed_Request{
    private boolean confirm;
    private String link_de_download;
    private int num_of_chunks;

    Seed_Request(boolean confirm, String linkDeDownload, int numOfChunks) {
        this.confirm = confirm;
        link_de_download = linkDeDownload;
        num_of_chunks = numOfChunks;
    }

    public boolean isConfirm() {
        return confirm;
    }

    public String getLink_de_download() {
        return link_de_download;
    }

    public int getNum_of_chunks() {
        return num_of_chunks;
    }
}



class Login_Request {
    private Boolean valid;
    private Boolean admin;
    private int id;


    Login_Request(Boolean valid, Boolean admin, int id) {
        this.valid = valid;
        this.admin = admin;
        this.id = id;
    }

    public boolean isValid() {
        return valid;
    }


    public boolean isAdmin() {
        return admin;
    }


    public int getId() {
        return id;
    }
}

class myContainer{
    Login_Request myLogin;

    void myContainer(Login_Request myLogin){
        this.myLogin = myLogin;
    }

    public Login_Request getMyLogin() {
        return myLogin;
    }

    public void setMyLogin(Login_Request myLogin) {
        this.myLogin = myLogin;
    }
}






