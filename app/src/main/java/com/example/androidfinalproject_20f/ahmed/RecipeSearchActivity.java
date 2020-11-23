package com.example.androidfinalproject_20f.ahmed;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfinalproject_20f.R;
import com.google.android.material.snackbar.Snackbar;

public class RecipeSearchActivity extends AppCompatActivity {

    /**
     * EditText to enter the recipe name for searching
     */
    private EditText recipeNameEditText;

    /**
     * EditText to enter the ingredients for searching along with the recipe name
     */
    private EditText ingredientsEditText;

    /**
     * Search button to initiate the search
     */
    private Button searchButton;

    /**
     * creating SharedPreennes var
     */
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);

        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(v -> {

            // Validation
            String recipeName = recipeNameEditText.getText().toString();
            if (recipeName.isEmpty()) {
                Toast.makeText(this, R.string.rs_please_enter_recipe_name, Toast.LENGTH_SHORT).show();
                return;
            }

            String ingredients = ingredientsEditText.getText().toString();

            Intent i = new Intent(RecipeSearchActivity.this, RecipeListActivity.class);
            i.putExtra("R_NAME", recipeName);
            i.putExtra("INGREDIENTS", ingredients);
            startActivity(i);

            // Saving the SP
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("R_NAME", recipeName);
            editor.putString("INGREDIENTS",  ingredients);
            editor.commit();
        });

        Snackbar.make(findViewById(R.id.mainLayout), R.string.rs_welcome_recipe_search_by_ahmed, Snackbar.LENGTH_LONG).show();

        prefs = getSharedPreferences("RecipeSearchSharedPreference", Context.MODE_PRIVATE);

        // Calling SP
        recipeNameEditText.setText(prefs.getString("R_NAME", ""));
        ingredientsEditText.setText(prefs.getString("INGREDIENTS", ""));

    }
}