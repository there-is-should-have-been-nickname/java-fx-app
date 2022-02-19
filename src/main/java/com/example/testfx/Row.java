package com.example.testfx;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//Class for one row in table
public class Row<T> {
    public String extension;
    public String size;
    public T content;

    public Row(String extension, String size, T content) {
        this.extension = extension;
        this.size = size;
        this.content = content;
    }

    public Row() {
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String number) {
        this.extension = number;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String content) {
        this.size = content;
    }

    public T getContent() { return content; }

    public void setContent(T content) { this.content = content; }
}
