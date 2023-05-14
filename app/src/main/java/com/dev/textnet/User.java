package com.dev.textnet;

/**
 * Created by Dell on 2017/06/26.
 */

public class User {

    private String username;
    private String imageurl;
    private String status;


    private User(){


    }
    public User(String imageurl, String username,String status ) {
        this.status = status;
        this.imageurl = imageurl;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
