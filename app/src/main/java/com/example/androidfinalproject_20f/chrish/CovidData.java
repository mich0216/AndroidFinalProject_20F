package com.example.androidfinalproject_20f.chrish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CovidData {
    private String province;
    private int caseNumber;
    private long databaseId;
    private String date;
    private String country;

    public CovidData(String province, int caseNumber, String date, String country) {
        this(province, caseNumber,date, country, 0);
    }


    public CovidData(String province, int caseNumber,String date, String country, long dId) {
        this.province = province;
        this.caseNumber = caseNumber;
        this.date = date;
        this.country = country;
        databaseId=dId;
    }

    public void setDatabaseId(Long databaseId) { this.databaseId = databaseId;
    }


    public Long getDatabaseId() { return databaseId; }


    public int getCaseNumber() {
        return caseNumber;
    }

    public void setCaseNumber(int caseNumber) {
        this.caseNumber = caseNumber;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}