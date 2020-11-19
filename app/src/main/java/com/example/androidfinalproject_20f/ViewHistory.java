package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.transform.Result;

/**
 * @author  Chrishanthi Michael
 * CST 2335 -20
 *  This class ViewHistory is responsible to get the date input from the user selection and pass it to the another activity.
 * */
public class ViewHistory extends AppCompatActivity {
    SQLiteDatabase db;
    ArrayList<String> dateList = new ArrayList<>();
    CovidDateListAdaptor covidDateListAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history);

        ListView myList = findViewById(R.id.searchListView);
        myList.setAdapter(covidDateListAdaptor = new CovidDateListAdaptor());
        this.loadDataFromDatabase();
        Intent resultByDate = new Intent(this, ResultByDate.class);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                resultByDate.putExtra("date",dateList.get(position));
                startActivity(resultByDate);
            }
        });
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

}