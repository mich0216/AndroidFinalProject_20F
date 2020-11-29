package com.example.androidfinalproject_20f.chrish;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.androidfinalproject_20f.R;

import java.util.ArrayList;

public class CovidResultByDateFragment extends Fragment {
    SQLiteDatabase db;
    ArrayList<CovidData> dateList = new ArrayList<>();
    CovidDataAdaptor covidDataAdaptor;
    String resultByDate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_covid_result_by_date, container, false);

        // this gets the toolbar from the layout
        //Toolbar tBar = (Toolbar) result.findViewById(R.id.covidToolbar);
       // getActivity().setActionBar(tBar);
        //This loads the toolbar, which calls onCreateOptionMev
        //setSupportActionBar(tBar);

      // DrawerLayout drawer = result.findViewById(R.id.drawer_layout);
       //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
       //drawer, tBar, R.string.covidOpen, R.string.covidClose);
       //drawer.addDrawerListener(toggle);
       //toggle.syncState();


        resultByDate = getArguments().getString("DATE");
        CovidDataOpener dbOpener = new CovidDataOpener(getContext());
        //db = dbOpener.getWritableDatabase();
        this.queryDataFromDatabase(resultByDate);

        ListView myList = result.findViewById(R.id.searchListView);
        myList.setAdapter(covidDataAdaptor = new CovidDataAdaptor());
        //this.loadDataFromDatabase();
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