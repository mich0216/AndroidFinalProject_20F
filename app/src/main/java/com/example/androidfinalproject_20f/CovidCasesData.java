package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_cases_data);

        ListView myList = findViewById(R.id.listView);
        myList.setAdapter(covidDataAdaptor = new CovidDataAdaptor());

        Intent fromMain = getIntent();
        String country = fromMain.getStringExtra("country");
        String startDate =fromMain.getStringExtra("startDate");
        String endDate = fromMain.getStringExtra("endDate");

        TextView countryNameView = findViewById(R.id.countryEntered);
        countryNameView.setText(country);
        TextView startDateView = findViewById(R.id.startDateEntered);
        startDateView.setText(startDate);
        TextView endDateView = findViewById(R.id.endDateEntered);
        endDateView.setText(endDate);

        CovidDataQuery cdq = new CovidDataQuery();
        cdq.execute("haaa");

    }

    private class CovidDataQuery extends AsyncTask< String, Integer, String> {

        public String doInBackground(String... args)
        {
            try {
                //encode the string url; may need to be commented later
                // String encodeURL = URLEncoder.encode(args[0], "UTF-8");
                //create a URL object of what server to contact:
                // process JSON for UV code
                URL covidDataURL = new URL("https://api.covid19api.com/country/CANADA/status/confirmed/live?from=2020-10-14T00:00:00Z&to=2020-10-15T00:00:00Z");
                //open the connection
                HttpURLConnection covidDataURLConnection = (HttpURLConnection) covidDataURL.openConnection();
                //wait for data:
                InputStream uvResponse = covidDataURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(uvResponse, "UTF-8"), 8);

                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();
                JSONArray covidDataArray = new JSONArray(result);
                for(int j=0; j<covidDataArray.length(); j++){
                    JSONObject covidObject = covidDataArray.getJSONObject(j);
                    String province = covidObject.getString("Province");
                    int caseNumber = covidObject.getInt("Cases");
                    String date = covidObject.getString("Date");
                    CovidData cd = new CovidData(province, caseNumber, date, j);
                    list.add(cd);
                }


            }
            catch (Exception e)
            {

            }

            return "Done";
        }

        public boolean fileExistance(String fileName){
            File file = getBaseContext().getFileStreamPath(fileName);
            return file.exists();
        }

        public void onProgressUpdate(Integer... args)
        {
            //progressBar.setVisibility(View.VISIBLE);
            //progressBar.setProgress(args[0]);
        }

        public void onPostExecute(String fromDoInBackground)
        {
            //Log.i("HTTP", fromDoInBackground);
            // weatherIcon.setImageBitmap(weatherImage);
            // currentTemp.setText(currentTemp.getText()+ currTemp +"℃");
            // minTemp.setText(minTemp.getText()+minimumTemp+"℃");
            // maxTemp.setText(maxTemp.getText()+ maximumTemp+"℃");
            // uvRating.setText( uvRating.getText()+uvRate);
            // progressBar.setVisibility(View.INVISIBLE);


        }
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
            caseNumber.setText(getItem(position).getCaseNumber());

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getDatabaseId();
        }
    }
}