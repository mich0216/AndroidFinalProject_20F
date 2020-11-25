package com.example.androidfinalproject_20f.audiosearch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfinalproject_20f.R;
/**
 * @author Vettivale Ponnampalam
 * CST 2335 -020
 * The main class for the ArtistInputActivity extended from AppCompatActivity
 * */
public class ArtistInputActivity extends AppCompatActivity {

    /**
     * the albumNameEditText object
     */
    private EditText albumNameEditText;

    /**
     * the searchButton object
     */
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_input);

        albumNameEditText = findViewById(R.id.albumNameEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {
            String artistName = albumNameEditText.getText().toString();

            if (artistName.isEmpty()) {

                Toast.makeText(this, "Please enter the artist name", Toast.LENGTH_SHORT).show();

            } else {

                Intent i = new Intent(ArtistInputActivity.this, AlbumListActivity.class);
                i.putExtra("ARTIST_NAME", artistName);
                startActivity(i);

            }
        });

    }
}