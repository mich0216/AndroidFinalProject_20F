package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfinalproject_20f.MainActivity;
import com.example.androidfinalproject_20f.R;

public class EventSearchActivity extends AppCompatActivity {

    private EditText cityNameEditText;
    private Button searchEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        cityNameEditText = findViewById(R.id.cityNameEditText);
        searchEventButton = findViewById(R.id.searchEventButton);


        searchEventButton.setOnClickListener(v -> {

            String cityName = cityNameEditText.getText().toString();

            // Validating if user don't enter city name
            if (cityName.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_city_name, Toast.LENGTH_SHORT).show();
                return;
            }

            Intent i = new Intent(EventSearchActivity.this, EventListActivity.class);
            i.putExtra("CITY_NAME", cityName);
            startActivity(i);

        });

    }
}