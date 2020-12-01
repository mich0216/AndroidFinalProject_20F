package com.example.androidfinalproject_20f.chrish;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
/**
 * @author  Chrishanthi Michael
 * CST 2335 -20
 *  This class ResultByDate is responsible to get the date input and query the database for that date and
 *  return the provices with the case number in a list view.
 * */
public class ResultByDate extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<CovidData> dateList = new ArrayList<>();
    CovidDataAdaptor covidDataAdaptor;
    String resultByDate;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_covid_details);

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


        Bundle data = getIntent().getExtras();
      CovidResultByDateFragment covidResultByDate = new CovidResultByDateFragment();
      covidResultByDate.setArguments(data);
      getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLocation, covidResultByDate).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covidmenu, menu);
        return true;
    }



    // query the database for a given date
    private void queryDataFromDatabase(String resultByDate)
    {
        //get a database connection:
        CovidDataOpener dbOpener = new CovidDataOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
         String [] columns = {CovidDataOpener.COL_ID, CovidDataOpener.COL_DATE, CovidDataOpener.COL_CASES, CovidDataOpener.COL_COUNTRY, CovidDataOpener.COL_PROVINCE};
        //String [] columns = {CovidDataOpener.COL_DATE};
        //query all the results from the database:
        //Cursor results = db.query(false, CovidDataOpener.TABLE_NAME, columns, CovidDataOpener.COL_DATE+"="+resultByDate, null, null, null, null, null);
       // Cursor results = db.query(false, CovidDataOpener.TABLE_NAME, columns, "DATE="+resultByDate, null, null, null, null, null);
        //Cursor results = db.rawQuery("SELECT DISTINCT * FROM COVIDDATA WHERE DATE is  ? ",new String[] {resultByDate});
        Cursor results = db.rawQuery("SELECT PROVINCE, DATE, _ID, COUNTRY, CASES FROM COVIDDATA WHERE DATE is ? GROUP by PROVINCE" ,new String[] {resultByDate});
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateIndex = results.getColumnIndex(CovidDataOpener.COL_DATE);
        int idColIndex = results.getColumnIndex(CovidDataOpener.COL_ID);
        int countryIndex = results.getColumnIndex(CovidDataOpener.COL_COUNTRY);
        int caseIndex = results.getColumnIndex(CovidDataOpener.COL_CASES);
        int provinceIndex = results.getColumnIndex(CovidDataOpener.COL_PROVINCE);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
                long id = results.getLong(idColIndex);
                String country = results.getString(countryIndex);
                int cases = results.getInt(caseIndex);
                String province = results.getString(provinceIndex);
                String date = results.getString(dateIndex);
            //add the new Contact to the array list:
            //String province, int caseNumber,String date, String country, long dId
            dateList.add(new CovidData(province, cases,date,country,id));
            //dateList.add(date);

        }
        //notify dataset changed
       // covidDataAdaptor.notifyDataSetChanged();
//        printCursor(results, db.getVersion());
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
        }
        return true;

    }

    // The CovidDataAdaptor is extended from the BaseAdapter
    private class CovidDataAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return dateList.size();
        }

        @Override
        public CovidData getItem(int position) {
            return dateList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =getLayoutInflater();
            View newView = convertView;
            newView =inflater.inflate(R.layout.datalayout, parent, false);
            TextView province = newView.findViewById(R.id.provinceName);
            TextView caseNumber = newView.findViewById(R.id.caseNumber);
            province.setText(getItem(position).getProvince());
            caseNumber.setText(String.valueOf(getItem(position).getCaseNumber()));

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getDatabaseId();
        }



    }
}