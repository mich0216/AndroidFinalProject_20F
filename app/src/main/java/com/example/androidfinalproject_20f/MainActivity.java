package com.example.androidfinalproject_20f;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

/***
 * @author Chrishanthi Michael
 * CST 2335-020
 * MainActivty is the starting class
 */


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cButton = findViewById(R.id.covidButton);

        Intent goToWelcomePage = new Intent(this, WelcomePageCovid.class);

        cButton.setOnClickListener(click ->
        {
            startActivity(goToWelcomePage);
        });

    }



}