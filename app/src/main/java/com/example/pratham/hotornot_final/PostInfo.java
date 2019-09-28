package com.example.pratham.hotornot_final;

public class PostInfo {
    String Url;
    String name;
    String username;
    String date;
    String UUId;
    String likes;
    String dislikes;
    String time;
    String score;

    public PostInfo(String url, String name, String username, String date, String UUID, String likes, String dislikes) {
        Url = url;
        this.name = name;
        this.username = username;
        this.date = date;
        this.UUId = UUID;
        this.likes = likes;
        this.dislikes = dislikes;
    }

    public PostInfo(String url
            , String name
            , String username
            , String date
            , String UUID
            , String likes
            , String dislikes
            , String time
            , String score) {
        Url = url;
        this.name = name;
        this.username = username;
        this.date = date;
        this.UUId = UUID;
        this.likes = likes;
        this.dislikes = dislikes;
        this.time = time;
        this.score =score;
    }
}