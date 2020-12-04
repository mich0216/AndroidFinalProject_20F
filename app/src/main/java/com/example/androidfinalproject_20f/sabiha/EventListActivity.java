package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class EventListActivity extends AppCompatActivity {

    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String EVENT_START_DATE = "EVENT_START_DATE";
    public static final String EVENT_MIN_PRICE = "EVENT_MIN_PRICE";
    public static final String EVENT_MAX_PRICE = "EVENT_MAX_PRICE";
    public static final String EVENT_TICKETMASTER_URL = "EVENT_TICKETMASTER_URL";
    public static final String EVENT_IMAGE_URL = "EVENT_IMAGE_URL";
    /**
     * ListView to show a list of events
     */
    private ListView eventListView;
    /**
     * To display loading
     */
    private ProgressBar progressBar;
    /**
     * List to store all the events
     */
    private ArrayList<Event> list = new ArrayList<>();
    /**
     * Adapter object to be attached with ListView
     */
    private EventsAdaptor eventsAdaptor;

    private EventDetailsFragment dFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        String cityName = getIntent().getStringExtra("CITY_NAME");

        eventListView = findViewById(R.id.eventListView);
        progressBar = findViewById(R.id.progressBar);

        eventsAdaptor = new EventsAdaptor();
        eventListView.setAdapter(eventsAdaptor);

        String url = "https://app.ticketmaster.com/discovery/v2/events.json?apikey=dCz4IrRpL3b9AqwGrbdLbl1ZZupGAJP6&city=" + cityName + "&radius=100";

        MyHTTPRequest myHTTPRequest = new MyHTTPRequest();
        myHTTPRequest.execute(url);

        eventListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Event e = list.get(position);
            Bundle event  = new Bundle();
            event.putString(EVENT_NAME, e.getName());
            event.putString(EVENT_START_DATE, e.getStartDate());
            event.putDouble(EVENT_MIN_PRICE, e.getMinPrice());
            event.putDouble(EVENT_MAX_PRICE, e.getMaxPrice());
            event.putString(EVENT_TICKETMASTER_URL, e.getTicketMasterUrl());
            event.putString(EVENT_IMAGE_URL, e.getImageUrl());

            Intent newIntent = new Intent(this, SaveEventDetailActivity.class);
            newIntent.putExtras(event);
            startActivity(newIntent);
//            Event e = list.get(position);
//            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//            alertDialogBuilder.setTitle(e.getName())
//                    .setMessage(
//                            getString(R.string.start_date) + e.getStartDate() +
//                                    "\n" + getString(R.string.min_price) + e.getMinPrice() +
//                                    "\n" + getString(R.string.max_price) + e.getMaxPrice()
//                    )
//                    .setPositiveButton(R.string.open_url, (dialog, which) -> {
//
//                        // Reference: https://stackoverflow.com/a/3004542
//                        Intent i = new Intent(Intent.ACTION_VIEW);
//                        i.setData(Uri.parse(e.getTicketMasterUrl()));
//                        startActivity(i);
//
//                    })
//                    .create().show();
            return true;
        });

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        eventListView.setOnItemClickListener((adapterView, view, position, l) -> {

            Bundle dataToPass = new Bundle();
            dataToPass.putString(EVENT_NAME, (list.get(position).getName()));
            dataToPass.putString(EVENT_START_DATE, (list.get(position).getStartDate()));
            dataToPass.putDouble(EVENT_MIN_PRICE, (list.get(position).getMinPrice()));
            dataToPass.putDouble(EVENT_MAX_PRICE, (list.get(position).getMaxPrice()));
            dataToPass.putString(EVENT_TICKETMASTER_URL, (list.get(position).getTicketMasterUrl()));
            dataToPass.putString(EVENT_IMAGE_URL, (list.get(position).getImageUrl()));
//            dataToPass.putLong(MESSAGE_ID, id);


            if (isTablet) {
                dFragment = new EventDetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else {
                Intent nextActivity = new Intent(EventListActivity.this, EventEmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

    }

    /**
     * Asynctask using url for searching the events and adding into the list
     */
    private class MyHTTPRequest extends AsyncTask<String, Integer, String> {
        //Type3                Type1
        public String doInBackground(String... args) {
            try {

                //create a URL object of what server to contact:
                URL url = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject jsonObjectResult = new JSONObject(result);

                JSONObject embeddedJsonObject = jsonObjectResult.getJSONObject("_embedded");

                JSONArray eventsJsonArray = embeddedJsonObject.getJSONArray("events");

                for (int i = 0; i < eventsJsonArray.length(); i++) {

                    JSONObject eventJsonObject = eventsJsonArray.getJSONObject(i);
                    Log.i("EventListActivity", eventJsonObject.getString("name"));


                    String eventName = eventJsonObject.getString("name");
                    String startDate = eventJsonObject.getJSONObject("dates").getJSONObject("start").getString("localDate");
                    double minPrice = eventJsonObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("min");
                    double maxPrice = eventJsonObject.getJSONArray("priceRanges").getJSONObject(0).getDouble("max");
                    String ticketMasterUrl = eventJsonObject.getString("url");
                    String imageUrl = eventJsonObject.getJSONArray("images").getJSONObject(0).getString("url");

                    Event e = new Event(eventName, startDate, minPrice, maxPrice, ticketMasterUrl, imageUrl);

                    list.add(e);

                }


            } catch (Exception e) {

            }

            return "Done";
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {

            progressBar.setVisibility(View.GONE);
            eventsAdaptor.notifyDataSetChanged();

            if (list.isEmpty()) {
                Snackbar.make(eventListView, R.string.no_events_found, Snackbar.LENGTH_SHORT).show();
            } else {
                Snackbar.make(eventListView, R.string.events_loaded, Snackbar.LENGTH_SHORT).show();
            }

        }
    }

    /**
     * Adaptor to handle events in the listview
     */
    private class EventsAdaptor extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Event getItem(int position) {
            return list.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = convertView;

            newView = inflater.inflate(R.layout.row_event, parent, false);
            TextView eventNameTextView = newView.findViewById(R.id.eventNameTextView);
            TextView eventPriceRangeTextView = newView.findViewById(R.id.eventPriceRangeTextView);

            eventNameTextView.setText(getItem(position).getName());
            eventPriceRangeTextView.setText(getItem(position).getMinPrice() + " - " + getItem(position).getMaxPrice());

            return newView;
        }

        @Override
        public long getItemId(int position) {
            return getItem(position).getId();
        }

    }
}