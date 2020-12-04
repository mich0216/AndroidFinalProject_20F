package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfinalproject_20f.R;

import org.w3c.dom.Text;

public class SaveEventDetailActivity extends AppCompatActivity {

    public static final String EVENT_NAME = "EVENT_NAME";
    public static final String EVENT_START_DATE = "EVENT_START_DATE";
    public static final String EVENT_MIN_PRICE = "EVENT_MIN_PRICE";
    public static final String EVENT_MAX_PRICE = "EVENT_MAX_PRICE";
    public static final String EVENT_TICKETMASTER_URL = "EVENT_TICKETMASTER_URL";
    public static final String EVENT_IMAGE_URL = "EVENT_IMAGE_URL";

    /**
     * Event detail declaration
     */
    private TextView startDate, minPrice, maxPrice;

    /**
     * save and event URL button name declaration
     */
    private Button btnEventURL, btnSaveEvent;

    private EventMyOpener db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_event_detail);

        db = new EventMyOpener(this);

        Bundle event = getIntent().getExtras();

        startDate = (TextView)findViewById(R.id.txtEventStartDate);
        minPrice = (TextView)findViewById(R.id.txtEventMinPrice);
        maxPrice = (TextView)findViewById(R.id.txtEventMaxPrice);
        btnEventURL = (Button)findViewById(R.id.btnEventURL);
        btnSaveEvent = (Button)findViewById(R.id.btnSaveEvent);

        startDate.setText(event.get(EVENT_START_DATE).toString());
        minPrice.setText(event.get(EVENT_MIN_PRICE).toString());
        maxPrice.setText(event.get(EVENT_MAX_PRICE).toString());

        btnEventURL.setOnClickListener(click -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
                     i.setData(Uri.parse(event.get(EVENT_TICKETMASTER_URL).toString()));
                        startActivity(i);
        });

        btnSaveEvent.setOnClickListener(click -> {
            Event e = new Event(
                    event.get(EVENT_NAME).toString(),
                    event.get(EVENT_START_DATE).toString(),
                    (double)event.get(EVENT_MIN_PRICE),
                    (double)event.get(EVENT_MAX_PRICE),
                    event.get(EVENT_TICKETMASTER_URL).toString(),
                    event.get(EVENT_IMAGE_URL).toString()
            );

            db.insertEvent(e);
            Toast.makeText(this, R.string.EventSaveButton, Toast.LENGTH_SHORT).show();
        });
    }
}