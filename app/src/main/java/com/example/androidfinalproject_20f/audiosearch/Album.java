package com.example.androidfinalproject_20f.audiosearch;
/**
 * @author Vettivale Ponnampalam
 * Course Code 2335 -20
 * Final Team Project
 * Album.java
 * */
public class Album {

    /**
     * id of the album
     */
    private long id;

    /**
     * title of the album
     */
    private String title;

    /**
     * The Album constractor
     * @param id the album id
     * @parm title the album title*/
    public Album(long id, String title) {
        this.id = id;
        this.title = title;
    }

    /**
     * the gtter for album id
     * @return id album id
     * */
    public long getId() {
        return id;
    }

    /**
     * he setter for album id
     * @param id the album id
     *
     * */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * the getter for album title
     * @return  album title
     */
    public String getTitle() {
        return title;
    }

    /**
     * the setter for title
     * @param title
     * */
    public void setTitle(String title) {
        this.title = title;
    }
}