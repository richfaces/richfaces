package org.ircclient.controller;

public class Message {
    private String text;
    private String author;
    private String timestamp;

    public Message(String text, String author, String timestamp) {
        super();
        this.text = text;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
