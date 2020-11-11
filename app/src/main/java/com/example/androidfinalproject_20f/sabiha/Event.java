package com.example.androidfinalproject_20f.sabiha;

public class Event {

    /**
     * ID to be stored in database
     */
    private long id;

    /**
     * Name of the event
     */
    private String name;

    /**
     * starting date of the event
     */
    private String startDate;

    /**
     * Minimum price of the event
     */
    private double minPrice;

    /**
     * Maximum price of the event
     */
    private double maxPrice;

    /**
     * URL of the event
     */
    private String ticketMasterUrl;

    /**
     * URL of the image of the event
     */
    private String imageUrl;

    public Event(String name, String startDate, double minPrice, double maxPrice, String ticketMasterUrl, String imageUrl) {
        this.name = name;
        this.startDate = startDate;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.ticketMasterUrl = ticketMasterUrl;
        this.imageUrl = imageUrl;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getTicketMasterUrl() {
        return ticketMasterUrl;
    }

    public void setTicketMasterUrl(String ticketMasterUrl) {
        this.ticketMasterUrl = ticketMasterUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
