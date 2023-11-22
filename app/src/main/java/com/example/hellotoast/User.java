package com.example.hellotoast;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String username;
    @SerializedName("password")
    private String password;
    @SerializedName("network_address")
    private String network_address;
    @SerializedName("manager")
    private boolean manager;
    @SerializedName("seeder")
    private int seeder;

    User(int id, String name, String password, boolean manager, String network_address){
        this.id = id;
        this.username = name;
        this.password = password;
        this.manager = manager;
        this.network_address = network_address;
        this.seeder = 0;
    }

    User(int id, String name, String password, boolean manager){
        this.id = id;
        this.username = name;
        this.password = password;
        this.manager = manager;
        this.network_address = null;
        this.seeder = 0;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAdmin() {
        return manager;
    }

    public int isSeeder() {
        return seeder;
    }

    public String getIp() {
        return network_address;
    }



    public void setSeeder(int isSeeder) {
        this.seeder = isSeeder;
    }


    public int getId() {
        return id;
    }

    public int getSeeder() {
        return seeder;
    }

    public void setIp(String network_address) {
        this.network_address = network_address;
    }
}
