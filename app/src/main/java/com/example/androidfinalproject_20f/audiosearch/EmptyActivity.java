package com.example.androidfinalproject_20f.audiosearch;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinalproject_20f.R;

public class EmptyActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        // this is copied directly from the ChatRoomActivity.java line 62-67
        DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
        dFragment.setArguments(dataToPass); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
    }
}