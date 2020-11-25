package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfinalproject_20f.R;

public class EventSearchActivity extends AppCompatActivity {

    /**
     * city name declaration
     */
    private EditText cityNameEditText;

    /**
     * searchEventButton declaration
     */
    private Button searchEventButton;

    /**
     * saving city name
     */
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        cityNameEditText = findViewById(R.id.cityNameEditText);
        searchEventButton = findViewById(R.id.searchEventButton);

        // initializing prefs in TicketmasterSP file
        prefs = getSharedPreferences("TicketmasterSP", Context.MODE_PRIVATE);
        String savedString = prefs.getString("CityName", "");
        cityNameEditText.setText(savedString);


        searchEventButton.setOnClickListener(v -> {

            String cityName = cityNameEditText.getText().toString();

            // Validating if user don't enter city name
            if (cityName.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_city_name, Toast.LENGTH_SHORT).show();
                return;
            }

            saveSharedPrefs(cityName);

            Intent i = new Intent(EventSearchActivity.this, EventListActivity.class);
            i.putExtra("CITY_NAME", cityName);
            startActivity(i);

        });

    }

    /**
     * saveSharePrefs method saving the city name.
     * @param stringToSave store the city name
     */
    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CityName", stringToSave);
        editor.commit();
    }

}