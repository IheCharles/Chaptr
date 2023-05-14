package com.example.textnet;

/**
 * Created by Dell on 2017/06/21.
 */

public class Cards {
    private String imgURL;
    private String title;

    public Cards(String imgURL, String title) {
        this.imgURL = imgURL;
        this.title = title;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getTitle() {
        return title;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
