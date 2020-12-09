package com.example.androidfinalproject_20f.audiosearch;

public class Song {

    private String SongName;
    private String artistName;

    public Song(String songName, String artistName) {
        SongName = songName;
        this.artistName = artistName;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
