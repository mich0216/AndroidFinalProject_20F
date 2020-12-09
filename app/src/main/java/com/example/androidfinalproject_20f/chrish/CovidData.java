package com.example.androidfinalproject_20f.chrish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 * @author Chrishanthi Michael
 * CST 2335-020
 * CovidData is the activity page
 */
public class CovidData {
   /**
    * the variable province as a String
    */
    private String province;
    /**
     * the variable caseNumber as a String
     */
    private int caseNumber;
    /**
     * the variable databaseId as a long
     */
    private long databaseId;
    /**
     * variable date as a string
     */
    private String date;
    /**
     * variable country as a string
     */
    private String country;

    /**
     * The constructor for CovidData
     * @param province the province
     * @param caseNumber the case naumber for the provice
     * @param date the date for the covid data
     * @param country the country for the covid data
     */
    public CovidData(String province, int caseNumber, String date, String country) {
        this(province, caseNumber,date, country, 0);
    }

    /**
     * The constructor for CovidData
     * @param province the province
     * @param caseNumber the case naumber for the provice
     * @param date the date for the covid data
     * @param country the country for the covid data
     * @param dId the databaseid for the datebase row
     */
    public CovidData(String province, int caseNumber,String date, String country, long dId) {
        this.province = province;
        this.caseNumber = caseNumber;
        this.date = date;
        this.country = country;
        databaseId=dId;
    }

    /**
     * the setter for the database id
     * @param databaseId is a long
     */
    public void setDatabaseId(Long databaseId) { this.databaseId = databaseId;
    }
    /**
     * the getter for the database id
     * @return a Long for the database id
     */
    public Long getDatabaseId() { return databaseId; }

    /**
     * the getter for the caseNumber
     * @return a int for the casenumber
     */

    public int getCaseNumber() {
        return caseNumber;
    }

    /**
     * setter fir the casenumber
     * @param caseNumber is a int
     */
    public void setCaseNumber(int caseNumber) {
        this.caseNumber = caseNumber;
    }

    /**
     * getter for the province
     * @return a string for the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * setter for the province
     * @param province is a string
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * getter for the date
     * @return date as a string
     */
    public String getDate() {
        return date;
    }

    /**
     * setter for the date
     * @param date is a string
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * getter for the country name
     * @return country as a string
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter for the country
     * @param country is a string
     */
    public void setCountry(String country) {
        this.country = country;
    }
}