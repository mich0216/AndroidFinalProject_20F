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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * @author  Chrishanthi Michael
 * CST 2335 -20
 *  This class ViewHistory is responsible to get the date input from the user selection and pass it to the another activity.
 * */
public class ViewHistory extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<String> dateList = new ArrayList<>();
    CovidDateListAdaptor covidDateListAdaptor;
    CovidResultByDateFragment dFragment;
    public static final String DATE = "DATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

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

        ListView myList = findViewById(R.id.searchListView);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded

        myList.setAdapter(covidDateListAdaptor = new CovidDateListAdaptor());
        this.loadDataFromDatabase();


    Intent resultByDate = new Intent(this, ResultByDate.class);
         myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Bundle dataToPass = new Bundle();
                dataToPass.putString(DATE, dateList.get(position));

                if(isTablet){
                    //dFragment = new CovidDetailsFragment();
                    dFragment = new CovidResultByDateFragment();
                    dFragment.setArguments(dataToPass);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragmentLocation, dFragment)
                            .commit();

                }
                else {
                    resultByDate.putExtra("DATE", dateList.get(position));
                    startActivity(resultByDate);
                }
            }
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




    private void loadDataFromDatabase()
    {
        //get a database connection:
        CovidDataOpener dbOpener = new CovidDataOpener(this);
       // db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db = dbOpener.getReadableDatabase();

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
       // String [] columns = {CovidDataOpener.COL_ID, CovidDataOpener.COL_DATE, CovidDataOpener.COL_CASES, CovidDataOpener.COL_COUNTRY, CovidDataOpener.COL_PROVINCE};
        String [] columns = {CovidDataOpener.COL_DATE};
        //query all the results from the database:
        Cursor results = db.query(true, CovidDataOpener.TABLE_NAME, columns, null, null, null, null, null, null);
        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateIndex = results.getColumnIndex(CovidDataOpener.COL_DATE);


        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
        //    long id = results.getLong(idColIndex);
        //    String country = results.getString(countryIndex);
        //    int cases = results.getInt(caseIndex);
        //    String province = results.getString(provinceIndex);
            String date = results.getString(dateIndex);
            //add the new Contact to the array list:
            //String province, int caseNumber,String date, String country, long dId
            //dateList.add(new CovidData(province, cases,date,country,id));
            dateList.add(date);
        }
//        printCursor(results, db.getVersion());
    }

    private void printCursor(Cursor c, int version){
        Log.i("database version number", String.valueOf(version));
        Log.i("Number of the columns ", String.valueOf(c.getColumnCount()));
        for(String col : c.getColumnNames()){
            Log.i("Name of the columns ", col);
        }
        Log.i("Number of rows ", String.valueOf(c.getCount()));

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String id = c.getString(c.getColumnIndex("_id"));
                String message = c.getString(c.getColumnIndex("MESSAGE"));
                String isReceived = c.getString(c.getColumnIndex("isReceived"));
                Log.i("The row value is", id+" "+ message+" "+ isReceived);
                c.moveToNext();
            }
        }

    }

    private class CovidDateListAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return dateList.size();
        }

        @Override
        public String getItem(int position) {
            return dateList.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =getLayoutInflater();
            View newView = convertView;
            newView =inflater.inflate(R.layout.datelist, parent, false);
            TextView date = newView.findViewById(R.id.dateList);
            date.setText(getItem(position));

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }
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
                        .setMessage(getResources().getString(R.string.CviewHis_instruction))
                        .setNeutralButton("OK",(click, arg)->{})
                        .create().show();
                break;
        }
        return true;

    }
}