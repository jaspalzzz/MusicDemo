package com.android.musicplayer;

import java.io.Serializable;

public class Audio implements Serializable {

    private String path;
    private String title;
    private String album;
    private String artist;

    public Audio(String data, String title, String album, String artist) {
        this.path = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String data) {
        this.path = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
