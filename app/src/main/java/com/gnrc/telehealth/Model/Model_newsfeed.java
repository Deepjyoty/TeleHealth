package com.gnrc.telehealth.Model;

import java.io.Serializable;

public class Model_newsfeed implements Serializable {

    String title, author, date, content_desc, video_url, thumbnail_url,content_type;

    public Model_newsfeed(String title, String author, String date, String content_desc, String video_url, String thumbnail_url, String content_type) {
        this.title = title;
        this.author = author;
        this.date = date;
        this.content_desc = content_desc;
        this.video_url = video_url;
        this.thumbnail_url = thumbnail_url;
        this.content_type = content_type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent_desc() {
        return content_desc;
    }

    public void setContent_desc(String content_desc) {
        this.content_desc = content_desc;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(String thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
}