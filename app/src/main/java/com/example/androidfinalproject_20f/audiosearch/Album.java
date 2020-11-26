package com.example.androidfinalproject_20f.audiosearch;

/**
 * @author Vettivale Ponnampalam
 * Course Code 2335 -20
 * Final Team Project
 * Album.java
 */
public class Album {

    /**
     * id of the album
     */
    private long id;

    /**
     * title of the album
     */
    private String title;
    private String year;
    private String description;
    private String genre;
    private String sale;

    /**
     * The Album constractor
     *
     * @parm title the album title
     */
    public Album(String title, String year, String description, String genre, String sale) {
        this.title = title;
        this.year = year;
        this.description = description;
        this.genre = genre;
        this.sale = sale;
    }

    /**
     * the gtter for album id
     *
     * @return id album id
     */
    public long getId() {
        return id;
    }

    /**
     * he setter for album id
     *
     * @param id the album id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * the getter for album title
     *
     * @return album title
     */
    public String getTitle() {
        return title;
    }

    /**
     * the setter for title
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * get the year of the album
     * @return year
     * */
    public String getYear() {
        return year;
    }
    /**
     * set the year of the album
     * @parm year String
     * */
    public void setYear(String year) {
        this.year = year;
    }
    /**
     * get the description of the album
     * @return String description of the album
     * */
    public String getDescription() {
        return description;
    }

    /**
     * set the description of the album
     * @param String description*/
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * get the Genere of the album
     * @return String genre of the album*/
    public String getGenre() {
        return genre;
    }

    /**
     * set the genere of the album
     * @parm String genere
     * */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * get the sale of the album
     * @return String sale
     * */
    public String getSale() {
        return sale;
    }

    /**
     * set the sale of the album
     * @parm String sale
     * */
    public void setSale(String sale) {
        this.sale = sale;
    }
}