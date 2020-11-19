package com.example.androidfinalproject_20f;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.androidfinalproject_20f.audiosearch.ArtistInputActivity;

import com.example.androidfinalproject_20f.ahmed.RecipeSearchActivity;

/**
 * @author Ahmed Elakad, Chrishanthi Michael, Sabiha Rahman, Vettival Ponnampalam
 * CST 2335-020
 * MainActivty is the starting class
 */


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /// setContentView loads objects onto the screen.
        setContentView(R.layout.activity_main);


        Button audioSearchButton = findViewById(R.id.button4);
        audioSearchButton.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, ArtistInputActivity.class);

        // button to go to covid data page
        Button cButton = findViewById(R.id.covidButton);

        // creating a transition to load WelcomePageCovid from main activity page
        Intent goToWelcomePage = new Intent(this, WelcomePageCovid.class);

        //when you click the button, it will start the next activity
        cButton.setOnClickListener(click ->
        {
            startActivity(goToWelcomePage);
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, RecipeSearchActivity.class);

            startActivity(i);
        });
    }



}