package com.example.prashantbhardwaj.teqnitask;

/**
 * Created by prashantbhardwaj on 11/08/17.
 */

public class SuperHeroes {
    private String imageUrl;
    private String name;
    private String date;
    private String timeDuration;
    private String tag;
    private String postid;

    //Getters and Setters
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeDuration() {
        return timeDuration;
    }

    public void setTimeDuration(String timeDuration) {
        this.timeDuration = timeDuration;
    }

    public String getTag(){ return tag; }

    public void setTag(String tag) { this.tag = tag; }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }
}
