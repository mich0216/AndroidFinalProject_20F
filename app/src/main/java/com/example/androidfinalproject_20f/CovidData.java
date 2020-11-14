package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CovidData {
    private String province;
    private int caseNumber;
    private long databaseId;
    private String date;

    public CovidData(String province, int caseNumber, String date) {
        this(province, caseNumber,date, 0);
    }


    public CovidData(String province, int caseNumber,String date, long dId) {
        this.province = province;
        this.caseNumber = caseNumber;
        this.date = date;
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
}