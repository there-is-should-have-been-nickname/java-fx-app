package com.example.testfx;

//Class for one row in table
public class Row {
    public String number;
    public String content;

    public Row(String number, String content) {
        this.number = number;
        this.content = content;
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
}
