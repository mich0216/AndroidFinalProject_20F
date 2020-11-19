package com.example.androidfinalproject_20f;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidfinalproject_20f.audiosearch.ArtistInputActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button audioSearchButton = findViewById(R.id.button4);
        audioSearchButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ArtistInputActivity.class);
            startActivity(i);
        });
    }
}