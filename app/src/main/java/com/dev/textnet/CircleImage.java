package com.dev.textnet;

/**
 * Created by Dell on 2017/08/07.
 */

public class CircleImage {
    private String CircleImage;
    private int color;
    private Boolean New;

    private CircleImage(){


    }

    public Boolean getNew() {
        return New;
    }

    public void setNew(Boolean aNew) {
        New = aNew;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public CircleImage(String CircleImage) {
    this.CircleImage=CircleImage;
    }

    public String getCircleImage() {
        return CircleImage;
    }

    public void setCircleImage(String circleImage) {
        CircleImage = circleImage;
    }
}
