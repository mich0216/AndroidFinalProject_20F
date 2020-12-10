package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class EventDatabaseListActivity extends AppCompatActivity {

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
     * Declared progressBar to display loading
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
    /**
     * Fragment object to be attached with ListView for Tablet
     */
    private EventDetailsFragment dFragment;
    /**
     * MyOpener object declared
     */
    private EventMyOpener db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        db = new EventMyOpener(this);

        String cityName = getIntent().getStringExtra("CITY_NAME");

        eventListView = findViewById(R.id.eventListView);
        progressBar = findViewById(R.id.progressBar);


        eventsAdaptor = new EventsAdaptor();
        eventListView.setAdapter(eventsAdaptor);

        eventListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Event e = list.get(position);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle(e.getName())
                    .setMessage(
                            R.string.eventListView
                    )
                    .setPositiveButton(R.string.yes, (dialog, which) -> {
                        db.deleteEvent(e.getTicketMasterUrl());
                        list.remove(e);
                        eventsAdaptor.notifyDataSetChanged();
                    })
                    .setNegativeButton(R.string.no, (dialog, which) -> {
                    })

                    .create().show();
            return true;
        });

        // load events detail from database

        boolean isTablet = findViewById(R.id.fragmentLocation) != null;

        eventListView.setOnItemClickListener((adapterView, view, position, l) -> {

            Event e = list.get(position);

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
                Intent nextActivity = new Intent(EventDatabaseListActivity.this, SaveEventDetailActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }

        });

    }

    public void loadDataFromDatabase() {
        progressBar.setVisibility(View.INVISIBLE);

        EventMyOpener dbOpener = new EventMyOpener(this);
        SQLiteDatabase db = dbOpener.getReadableDatabase();

        String[] columns = { EventMyOpener.COL_NAME,  EventMyOpener.COL_STARTDATE,  EventMyOpener.COL_MINPRICE,  EventMyOpener.COL_MAXPRICE,   EventMyOpener.COL_TICKETMASTERURL,  EventMyOpener.COL_IMAGEURL};
        Cursor res =  db.rawQuery( "select * from EVENTS", null );
        int eventNameIndex  = res.getColumnIndex( EventMyOpener.COL_NAME);
        int startDateIndex = res.getColumnIndex( EventMyOpener.COL_STARTDATE);
        double minPriceIndex = res.getColumnIndex( EventMyOpener.COL_MINPRICE);
        double maxPriceIndex = res.getColumnIndex( EventMyOpener.COL_MAXPRICE);
        int ticketMasterUrlIndex = res.getColumnIndex( EventMyOpener.COL_TICKETMASTERURL);
        int imageUrlIndex = res.getColumnIndex( EventMyOpener.COL_IMAGEURL);

        //iterate over the results, return true if there is a next item:
        list.clear();
        while (res.moveToNext()){
            String eventName = res.getString(res.getColumnIndex(EventMyOpener.COL_NAME));
            String startDate = res.getString(res.getColumnIndex(EventMyOpener.COL_STARTDATE));
            double minPrice = res.getDouble(res.getColumnIndex(EventMyOpener.COL_MINPRICE));
            double maxPrice = res.getDouble(res.getColumnIndex(EventMyOpener.COL_MAXPRICE));
            String ticketMasterUrl = res.getString(res.getColumnIndex(EventMyOpener.COL_TICKETMASTERURL));
            String imageUrl = res.getString(res.getColumnIndex(EventMyOpener.COL_IMAGEURL));
            //vent(String name, String startDate, double minPrice, double maxPrice, String ticketMasterUrl, String imageUrl)
            Event event = new Event(eventName, startDate, minPrice, maxPrice,ticketMasterUrl, imageUrl);
            list.add(event);
        }
        if (list.isEmpty()) {
            Toast.makeText(EventDatabaseListActivity.this, R.string.no_events_found, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataFromDatabase();
        //EventsAdaptor.notifyDataSetChanged();
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