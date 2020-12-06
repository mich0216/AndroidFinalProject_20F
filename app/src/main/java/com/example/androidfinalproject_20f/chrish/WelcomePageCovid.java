package com.example.androidfinalproject_20f.chrish;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.navigation.NavigationView;

/**
 *
 */

public class WelcomePageCovid extends AppCompatActivity {


    SharedPreferences pref = null;
    EditText country ,startDate, endDate;
    private String countryToSave, startDateToSave, endDateToSave;

    @Override
    //Your program starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView loads objects onto the screen before the screen is empty.
        setContentView(R.layout.activity_welcome_page_covid);

        // this gets the toolbar from the layout
        Toolbar tBar = (Toolbar) findViewById(R.id.covidToolbar);

        //This loads the toolbar, which calls onCreateOptionMev
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.covidOpen, R.string.covidClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (item -> {

            switch (item.getItemId()) {
                //what to do when the menu item is selected:
                case R.id.covidHistory:
                    Intent viewHistory = new Intent(this, ViewHistory.class);
                    startActivity(viewHistory);
                    break;

                case R.id.mainhome:
                    Intent mainPage = new Intent(this, MainActivity.class);
                    startActivity(mainPage);
                    break;

                case R.id.covidSearch:
                    Intent covidSearch = new Intent(this, WelcomePageCovid.class);
                    startActivity(covidSearch);
                    break;

            }

            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });


        country =findViewById(R.id.countryName);
        startDate =findViewById(R.id.enterStartDate);
        endDate =findViewById(R.id.enterEndDate);

        pref = getSharedPreferences("CovidFile", Context.MODE_PRIVATE);
        String countryName = pref.getString("Country", " ");//
        country.setText(countryName);
        String startDateValue = pref.getString("StartDate", " ");//
        startDate.setText(startDateValue);
        String endDateValue = pref.getString("EndDate", " ");//
        endDate.setText(endDateValue);

        Button searchButton = findViewById(R.id.searchButton);

        // creating a transition to load covid case data page from welcome page
        Intent goToCovidData = new Intent(this, CovidCasesData.class);// this is to say we are going from this page to the covidCaseData page
        Toast.makeText(WelcomePageCovid.this,  getResources().getString(R.string.covidToastMessage),Toast.LENGTH_LONG).show();
        searchButton.setOnClickListener(click ->
                {

                goToCovidData.putExtra("country", country.getText().toString());// to pass the country name to the welcome page
                goToCovidData.putExtra("startDate", startDate.getText().toString());// to pass the from date to the welcome page
                goToCovidData.putExtra("endDate", endDate.getText().toString());// to pass the to date to the welcome page
                startActivity(goToCovidData); // this to start/go to the covid data page
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covidmenu, menu);
        //    MenuInflater inflater2 = getMenuInflater();
        // inflater.inflate(R.menu.nagvigationmenu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // String message = null;
        //Look at your menu XML file. Put a case for every id in that file:
        switch (item.getItemId()) {
            //what to do when the menu item is selected:
            case R.id.covidHistory:
                Intent viewHistory = new Intent(this, ViewHistory.class);
                startActivity(viewHistory);
                break;

            case R.id.mainhome:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;

            case R.id.covidSearch:
                Intent covidSearch = new Intent(this, WelcomePageCovid.class);
                startActivity(covidSearch);
                break;

            case R.id.covidHelpIcone:
                AlertDialog.Builder helpmenu =new AlertDialog.Builder(this);
                helpmenu.setTitle(getResources().getString(R.string.covidInstruction))
                        .setMessage(getResources().getString(R.string.cwInstuction))
                        .setNeutralButton("OK",(click, arg)->{})
                        .create().show();
                break;
        }
        return true;
    }


    /**
     *
     * @param countryToSave this is the country name user enters
     * @param startDateToSave this is the start date user the provides/enter
     * @param endDateToSave  this is the end date the user provides/enter
     */


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