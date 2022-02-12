package com.example.testfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Class for one row in table
public class Row {
    public String number;
    public String content;
    public ImageView imageView;

    public Row(String number, String content, ImageView imageView) {
        this.number = number;
        this.content = content;
        this.imageView = imageView;
    }

    public Row() {
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ImageView getImageView() { return imageView; }

    public void setImageView(ImageView imageView) { this.imageView = imageView; }
}
