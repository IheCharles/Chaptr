package com.dev.textnet;



/**
 * Created by Dell on 2017/06/21.
 */

public class Cards {

    private int TextSize;
    private String Typeface;
    private String time;
    private int views;
    private String image;
    private int hearts;
    private int titlesize;
    private String title;
    private String username;
    private String Story;
    private String imageicon;
    private int textcolor;





    public int getTitlesize() {
        return titlesize;
    }

    public void setTitlesize(int titlesize) {
        this.titlesize = titlesize;
    }

    public int getTextSize() {
        return TextSize;
    }

    public void setTextSize(int textSize) {
        TextSize = textSize;
    }

    public String getTypeface() {
        return Typeface;
    }

    public void setTypeface(String typeface) {
        Typeface = typeface;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHearts() {
        return hearts;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public void setHearts(int hearts) {
        this.hearts = hearts;
    }

    public String getStory() {
        return Story;
    }

    public void setStory(String story) {
        Story = story;
    }

    private Cards(){


    }
    public Cards(String image, String title) {
        this.image = image;
        this.title = title;
        this.username = username;
        this.imageicon=imageicon;
        this.textcolor=textcolor;

    }

    public int getTextcolor() {
        return textcolor;
    }

    public void setTextcolor(int textcolor) {
        this.textcolor = textcolor;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageicon() {
        return imageicon;
    }

    public void setImageicon(String imageicon) {
        this.imageicon = imageicon;
    }
}
