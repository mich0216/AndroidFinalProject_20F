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
 * @author Chrishanthi Michael
 * CST 2335-020
 * WelcomePageCovid is the activity page where user inputs the country name and the dates
 */

public class WelcomePageCovid extends AppCompatActivity {

    /**
    * the variable pref the SharedPreference
    */
    SharedPreferences pref = null;
    /**
     * the variables country ,startDate, endDate as EditText
     * */
    EditText country ,startDate, endDate;
    /**
     *  the variables countryToSave, startDateToSave, endDateToSave as String
     */
    private String countryToSave, startDateToSave, endDateToSave;

    @Override
    //program starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView loads objects onto the screen. Before this function screen is empty.
        setContentView(R.layout.activity_welcome_page_covid);

        // this gets the toolbar from the layout
        Toolbar tBar = (Toolbar) findViewById(R.id.covidToolbar);

        //This loads the toolbar, which calls onCreateOptionMenu
        setSupportActionBar(tBar);

        // for Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.covidOpen, R.string.covidClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener (item -> {

            switch (item.getItemId()) {
                // when the menu item is selected go to view history page.
                case R.id.covidHistory:
                    Intent viewHistory = new Intent(this, ViewHistory.class);
                    startActivity(viewHistory);
                    break;
                // when the menu item is selected go to main activity page.
                case R.id.mainhome:
                    Intent mainPage = new Intent(this, MainActivity.class);
                    startActivity(mainPage);
                    break;
                // when the menu item is selected go to welcome page.
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
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //put a case for every id in  the covidmenu XML file, so that when it clicked it will go to the selected page.
        switch (item.getItemId()) {
            // when the menu item is selected go to view history page.
            case R.id.covidHistory:
                Intent viewHistory = new Intent(this, ViewHistory.class);
                startActivity(viewHistory);
                break;
            // when the menu item is selected got to main activity page.
            case R.id.mainhome:
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;
            // when the menu item is selected go to welcome page.
            case R.id.covidSearch:
                Intent covidSearch = new Intent(this, WelcomePageCovid.class);
                startActivity(covidSearch);
                break;
            // when the menu item is selected display a instruction to show how to use this page
            case R.id.covidHelpIcone:
                AlertDialog.Builder helpmenu =new AlertDialog.Builder(this);
                helpmenu.setTitle(getResources().getString(R.string.covidInstruction))
                        .setMessage(getResources().getString(R.string.cwInstuction))
                        .setNeutralButton(getResources().getString(R.string.cOK),(click, arg)->{})
                        .create().show();
                break;
        }
        return true;
    }


    /**
     * This method saves the information in Shared Preference
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