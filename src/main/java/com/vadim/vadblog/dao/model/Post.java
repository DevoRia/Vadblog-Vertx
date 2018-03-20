package com.vadim.vadblog.dao.model;

public class Post {

    private String id;
    private String author;
    private String text;
    private Long date;
    private Boolean visiable;

    public Post() { }

    public Post(String id, String author, String text, Long date, Boolean visiable) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.date = date;
        this.visiable = visiable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Boolean getVisiable() {
        return visiable;
    }

    public void setVisiable(Boolean visiable) {
        this.visiable = visiable;
    }
}
