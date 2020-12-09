package com.example.androidfinalproject_20f.chrish;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/***
 * @author Chrishanthi Michael
 * CST 2335 -20
 *  This class CovidCasesData is responsible to get the user input such as Country,
 *  Start Date, End Date from the user and put the data from the URL as JSON File
 *  format the data and display it as a List View.
 *  The user also can select the view to see more details about the data
 *  When the user selct on View History button, it will take them to another view.
 *
 */
public class CovidCasesData extends AppCompatActivity {
    /**
     * the variable list as an ArrayList contains CovidData objects
     */
    private ArrayList<CovidData> list  = new ArrayList<>();
    /**
     * the variable covidDataAdaptor is a CovidDataAdaptor object
     */
    private CovidDataAdaptor covidDataAdaptor;
    /**
     * the variable country as String
     */
    private String country;
    /**
     * the variable db as SQLiteDatabase object
     */
    SQLiteDatabase db;
    /**
     * the variable progressBar as ProgressBar object
     */
    ProgressBar progressBar;


    @Override
    //program starts here
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView loads objects onto the screen.
        setContentView(R.layout.activity_covid_cases_data);

        // this gets the toolbar from the layout
        Toolbar tBar = (Toolbar) findViewById(R.id.covidToolbar);

        //This loads the toolbar, which calls onCreateOptionMev
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


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        Button historyButton = findViewById(R.id.historyButton);
        Intent goToViewHistoryPage = new Intent(this, ViewHistory.class);
        historyButton.setOnClickListener(click ->
        {
            startActivity(goToViewHistoryPage);
        });


        ListView myList = findViewById(R.id.searchListView);
        myList.setAdapter(covidDataAdaptor = new CovidDataAdaptor());
        // set on item long click listener to display the province, case number, date with database id with a title
        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            CovidData selectedData = list.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.additionalDetail))

                    .setMessage(getResources().getString(R.string.cProvince)+((CovidData)list.get(position)).getProvince()+"\n"+
                            getResources().getString(R.string.cConfirmedCase)+((CovidData)list.get(position)).getCaseNumber()+"\n"+
                                    getResources().getString(R.string.cDate)+((CovidData)list.get(position)).getDate().substring(0,10)+"\n"+
                                    getResources().getString(R.string.thedatabaseid)+((CovidData)list.get(position)).getDatabaseId())
                    .setNeutralButton((getResources().getString(R.string.cOK)),(click, arg)->{})
                    .create().show();

            return true;
        });

        Intent fromMain = getIntent();
        country = fromMain.getStringExtra("country");
        String startDate =fromMain.getStringExtra("startDate");
        String endDate = fromMain.getStringExtra("endDate");

        TextView countryNameView = findViewById(R.id.countryEntered);
        countryNameView.setText(country);
        TextView startDateView = findViewById(R.id.startDateEntered);
        startDateView.setText(startDate);
        TextView endDateView = findViewById(R.id.endDateEntered);
        endDateView.setText(endDate);

        CovidDataQuery cdq = new CovidDataQuery();
        cdq.execute("https://api.covid19api.com/country/"+country.trim().toUpperCase()+"/status/confirmed/live?from="+startDate.trim()+"T00:00:00Z&to="+endDate.trim()+"T00:00:00Z");
        covidDataAdaptor.notifyDataSetChanged();
        Snackbar.make(myList,  getResources().getString(R.string.covidSnackBarMsg), Snackbar.LENGTH_LONG).show();
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.covidmenu, menu);
        return true;
    }



    /**
     * this method is responsible to setup the database opener and initalize the database
     * */
    public void setUpDatabaseOpener(){
        CovidDataOpener dbOpener = new CovidDataOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

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
                        .setMessage(getResources().getString(R.string.C_caseData_instruction))
                        .setNeutralButton(getResources().getString(R.string.cOK),(click, arg)->{})
                        .create().show();
                break;
        }
        return true;

    }

    /**
     * CovidDataQuery class is extended from AsyncTask
     *
     * */
    private class CovidDataQuery extends AsyncTask< String, Integer, String> {
        // do the task in background
        public String doInBackground(String... args)
        {
            try {
                //encode the string url; may need to be commented later
                // String encodeURL = URLEncoder.encode(args[0], "UTF-8");
                //create a URL object of what server to contact:
                URL covidDataURL = new URL(args[0]);
                //open the connection
                HttpURLConnection covidDataURLConnection = (HttpURLConnection) covidDataURL.openConnection();
                //wait for data:
                InputStream uvResponse = covidDataURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                Thread.sleep(500); // sleep for 500 milli seconds
                publishProgress(25); // set the progressbar value
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONArray covidDataArray = new JSONArray(result);

               setUpDatabaseOpener();
                Thread.sleep(500); // sleep for 500 milli seconds
                publishProgress(50);  // set the progressbar value
                for(int j=0; j<covidDataArray.length(); j++){
                    JSONObject covidObject = covidDataArray.getJSONObject(j);
                    String province = covidObject.getString("Province");
                    int caseNumber = covidObject.getInt("Cases");

                    String date = covidObject.getString("Date");

                    if(!province.trim().isEmpty()) {
                        CovidData cd = new CovidData(province, caseNumber, date, country,j);
                        list.add(cd);

                        ContentValues newRowValue = new ContentValues();

                        //Now provide a value for every database column defined in MyOpener.java:
                        //put string name in the NAME column:
                        newRowValue.put(CovidDataOpener.COL_COUNTRY, country);
                        newRowValue.put(CovidDataOpener.COL_PROVINCE, province);
                        newRowValue.put(CovidDataOpener.COL_CASES, caseNumber);
                        newRowValue.put(CovidDataOpener.COL_DATE, date.substring(0,10)); // trim the date for yyyy-mm-dd format when I write it to the db

                        //Now insert in the database:
                        long newId = db.insert(CovidDataOpener.TABLE_NAME, null, newRowValue);
                    }
                }
                Thread.sleep(500); // sleep for 500 milli seconds
                publishProgress(100);  // set the progressbar value
            }
            catch (Exception e)
            {
            }
            return "Done";
        }

        // onProgressUpdate method, it sets the progress bar visibility to true and set the value
        public void onProgressUpdate(Integer... args)
        {
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(args[0]);
        }

        // this method will be executed at the end of the task
        // this will set the progress bar to invisible
        // and notify the data change
        public void onPostExecute(String fromDoInBackground)
        {
            Log.i("CovidDataQuery ", fromDoInBackground);
             progressBar.setVisibility(View.INVISIBLE);
             covidDataAdaptor.notifyDataSetChanged();


        }
    }


    // delete the Covid Data object from the database
    protected void deleteMessage(CovidData c)
    {
        db.delete(CovidDataOpener.TABLE_NAME, CovidDataOpener.COL_ID + "= ?", new String[] {Long.toString(c.getDatabaseId())});
        Log.e("Deleted Database Object", Long.toString(c.getDatabaseId()));
    }


    /**
     * The CovidDataAdaptor class extended from BaseAdapter
     * */
    private class CovidDataAdaptor extends BaseAdapter {

        // return the size of the list
        @Override
        public int getCount() {
            return list.size();
        }

        // return the object from the list based on the position
        @Override
        public CovidData getItem(int position) {
            return list.get(position);
        }

        // create the view for CovidData
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

        // get the database id of the system
        @Override
        public long getItemId(int position) {
            return getItem(position).getDatabaseId();
        }


    }
}