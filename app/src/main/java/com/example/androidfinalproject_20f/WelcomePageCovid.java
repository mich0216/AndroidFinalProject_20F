package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class WelcomePageCovid extends AppCompatActivity {


    SharedPreferences pref = null;
    EditText country ,startDate, endDate;
    private String countryToSave, startDateToSave, endDateToSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page_covid);

        country =findViewById(R.id.countryName);
        startDate =findViewById(R.id.enterStartDate);
        endDate =findViewById(R.id.enterEndDate);

        pref = getSharedPreferences("CovidFile", Context.MODE_PRIVATE);
        String countryName = pref.getString("Country", " ");//???
        country.setText(countryName);
        String startDateValue = pref.getString("StartDate", " ");//???
        startDate.setText(startDateValue);
        String endDateValue = pref.getString("EndDate", " ");//???
        endDate.setText(endDateValue);

        Button searchButton = findViewById(R.id.searchButton);

        Intent goToCovidData = new Intent(this, CovidCasesData.class);// this is to say we are going from this page to the covidCaseData page
        searchButton.setOnClickListener(click ->
                {
                goToCovidData.putExtra("country", country.getText().toString());// to pass the country name to the welcome page
                goToCovidData.putExtra("startDate", startDate.getText().toString());// to pass the from date to the welcome page
                goToCovidData.putExtra("endDate", endDate.getText().toString());// to pass the to date to the welcome page
                startActivity(goToCovidData); // this to start/go to the covid data page
                });
    }

    private void saveSharedPrefs(String countryToSave, String startDateToSave, String endDateToSave) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("Country", countryToSave);
        editor.putString("StartDate", startDateToSave);
        editor.putString("EndDate", endDateToSave);
        editor.commit();
    }
    @Override
    protected void onPause() {
        super.onPause();
        countryToSave = country.getText().toString();
        startDateToSave= startDate.getText().toString();
        endDateToSave = endDate.getText().toString();
        saveSharedPrefs(countryToSave, startDateToSave, endDateToSave);

    }

    }