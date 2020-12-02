package com.example.androidfinalproject_20f.ahmed;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinalproject_20f.R;

public class RecipeEmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_empty);

        Bundle dataToPass = getIntent().getExtras(); //get the data that was passed from FragmentExample

        RecipeDetailsFragment dFragment = new RecipeDetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentLoadingSpace, dFragment) //Add the fragment in FrameLayout
                .commit(); //actually load the fragment. Calls onCreate() in DetailFragment

    }
}