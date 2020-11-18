package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class CovidCasesData extends AppCompatActivity {

    private ArrayList<CovidData> list  = new ArrayList<>();
    private CovidDataAdaptor covidDataAdaptor;
    private String country;
    SQLiteDatabase db;
    ProgressBar progressBar;

    public void writeToDb(){
        CovidDataOpener dbOpener = new CovidDataOpener(this);
        db = dbOpener.getWritableDatabase(); //This calls onCreate() if you've never built the table before, or onUpgrade if the version here is newer

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_cases_data);

        Button historyButton = findViewById(R.id.historyButton);
        Intent goToViewHistoryPage = new Intent(this, ViewHistory.class);
        historyButton.setOnClickListener(click ->
        {
            startActivity(goToViewHistoryPage);
        });

      //  progressBar = findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);

        ListView myList = findViewById(R.id.searchListView);
        myList.setAdapter(covidDataAdaptor = new CovidDataAdaptor());

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            CovidData selectedData = list.get(position);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(getResources().getString(R.string.additionalDetail))

                    .setMessage("The province is "+((CovidData)list.get(position)).getProvince()+"\n"+
                            "The Confirmed Case Number : "+((CovidData)list.get(position)).getCaseNumber()+"\n"+
                            "The date is : "+((CovidData)list.get(position)).getDate().substring(0,10)+"\n"+
                            "The database ID is "+((CovidData)list.get(position)).getDatabaseId())
                    .setNeutralButton("OK",(click, arg)->{})
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
        //covidDataAdaptor.notifyDataSetChanged();
    }

    private class CovidDataQuery extends AsyncTask< String, Integer, String> {

        public String doInBackground(String... args)
        {
            try {
                //encode the string url; may need to be commented later
                // String encodeURL = URLEncoder.encode(args[0], "UTF-8");
                //create a URL object of what server to contact:
                // process JSON for UV code
                URL covidDataURL = new URL(args[0]);
                //open the connection
                HttpURLConnection covidDataURLConnection = (HttpURLConnection) covidDataURL.openConnection();
                //wait for data:
                InputStream uvResponse = covidDataURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);
                //Thread.sleep(500);
               // publishProgress(25);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONArray covidDataArray = new JSONArray(result);

               writeToDb();
              //   Thread.sleep(500);
                // publishProgress(50);
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
            //    Thread.sleep(500);
          //      publishProgress(100);

            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }
            return "Done";
        }

        public boolean fileExistance(String fileName){
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }

        public void onProgressUpdate(Integer... args)
        {
         //   progressBar.setVisibility(View.VISIBLE);
         //   progressBar.setProgress(args[0]);
        }

        public void onPostExecute(String fromDoInBackground)
        {
           //Log.i("CovidDataQuery ", "all good");
            // weatherIcon.setImageBitmap(weatherImage);
            // currentTemp.setText(currentTemp.getText()+ currTemp +"℃");
            // minTemp.setText(minTemp.getText()+minimumTemp+"℃");
            // maxTemp.setText(maxTemp.getText()+ maximumTemp+"℃");
            // uvRating.setText( uvRating.getText()+uvRate);
         //   progressBar.setVisibility(View.INVISIBLE);


        }
    }


    protected void deleteMessage(CovidData c)
    {
        db.delete(CovidDataOpener.TABLE_NAME, CovidDataOpener.COL_ID + "= ?", new String[] {Long.toString(c.getDatabaseId())});
        Log.e("Deleted Database Object", Long.toString(c.getDatabaseId()));
    }


    private class CovidDataAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public CovidData getItem(int position) {
            return list.get(position);
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