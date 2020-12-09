package com.example.androidfinalproject_20f.chrish;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.androidfinalproject_20f.R;

import java.util.ArrayList;
/**
 * @author Chrishanthi Michael
 * CST 2335-020
 * CovidResultByDateFragment is the activity page for loading fragment
 * CovidResultByDateFragment extends the Fragment class
 */


public class CovidResultByDateFragment extends Fragment {
    /**
     * the variable db as SQLiteDatabase object
     */
    SQLiteDatabase db;
    /**
     * the variable datelist as an ArrayList contains CovidData objects
     */
    ArrayList<CovidData> dateList = new ArrayList<>();
    /**
     * the variable covidDataAdaptor is a CovidDataAdaptor object
     */
    CovidDataAdaptor covidDataAdaptor;
    /**
     * Variable resultbyDate as String
     */
    String resultByDate;

    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_covid_result_by_date, container, false);

        resultByDate = getArguments().getString("DATE");
        CovidDataOpener dbOpener = new CovidDataOpener(getContext());
        Button button = (Button) result.findViewById(R.id.deletebutton);
        button.setOnClickListener(click -> {
            this.deleteResultDateFromDB(resultByDate);
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        this.queryDataFromDatabase(resultByDate);
        ListView myList = result.findViewById(R.id.searchListView);
        myList.setAdapter(covidDataAdaptor = new CovidDataAdaptor());

        covidDataAdaptor.notifyDataSetChanged();
        return result;

    }
    // query the database for a given date
    private void queryDataFromDatabase(String resultByDate)
    {
        //get a database connection:
        CovidDataOpener dbOpener = new CovidDataOpener(getContext());
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {CovidDataOpener.COL_ID, CovidDataOpener.COL_DATE, CovidDataOpener.COL_CASES, CovidDataOpener.COL_COUNTRY, CovidDataOpener.COL_PROVINCE};

        //query all the results from the database:
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
    }
    // delete the Covid Data object from the database
    private void deleteResultDateFromDB(String resultByDate){
        CovidDataOpener dbOpener = new CovidDataOpener(getContext());
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer
        db.delete(CovidDataOpener.TABLE_NAME,CovidDataOpener.COL_DATE+ "= ?", new String[]{resultByDate});
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
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