package com.dev.textnet;

/**
 * Created by Dell on 2018/02/22.
 */

public class SeachModel {


    private String username;
    private  String imageurl;
    private String status;
    private int PostCount;
    private int followerNo;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPostCount() {
        return PostCount;
    }

    public void setPostCount(int postCount) {
        PostCount = postCount;
    }

    public int getFollowerNo() {
        return followerNo;
    }

    public void setFollowerNo(int followerNo) {
        this.followerNo = followerNo;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
