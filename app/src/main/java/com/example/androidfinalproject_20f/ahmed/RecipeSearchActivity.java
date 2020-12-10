package com.example.androidfinalproject_20f.ahmed;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.androidfinalproject_20f.R;
import com.example.androidfinalproject_20f.chrish.MainActivity;
import com.google.android.material.navigation.NavigationView;
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
    private Button searchButton, favoriteButton;

    /**
     * creating SharedPreference var
     */
    private SharedPreferences prefs;

    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_search);
        setupView();
        handleFavoriteButtonClick();
        handleSearchButtonClick();
        manageSharedPreferenceManager();
    }

    /**
     * Managing Data Stored in the shared preference
     */
    private void manageSharedPreferenceManager() {
        prefs = getSharedPreferences("RecipeSearchSharedPreference", Context.MODE_PRIVATE);
        recipeNameEditText.setText(prefs.getString("R_NAME", ""));
        ingredientsEditText.setText(prefs.getString("INGREDIENTS", ""));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipemenu, menu);
        return true;
    }

    /**
     * Handling When user click on option menu item
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                openFavoriteActivity();
                return true;
            case R.id.menu_help:
                alertDialog(getString(R.string.rs_search_screen_info));
                return true;
            case R.id.menu_home:
                //Toast.makeText(this, R.string.rs_we_already_home, Toast.LENGTH_SHORT).show();
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        drawerToggle.syncState();
    }

    /**
     * Initializing views
     */
    private void setupView() {
        recipeNameEditText = findViewById(R.id.recipeNameEditText);
        ingredientsEditText = findViewById(R.id.ingredientsEditText);
        searchButton = findViewById(R.id.searchButton);
        favoriteButton = findViewById(R.id.favoriteButton);
        toolbar = findViewById(R.id.toolbar);
        // Find our drawer view
        mDrawer = findViewById(R.id.drawer_layout);
        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(toolbar);
        nvDrawer = findViewById(R.id.nvView);
        setupDrawerContent(nvDrawer);
        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        drawerToggle = setupDrawerToggle();
        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        Snackbar.make(findViewById(R.id.mainLayout), R.string.rs_welcome_recipe_search_by_ahmed, Snackbar.LENGTH_LONG).show();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_favorites:
                openFavoriteActivity();
                break;
            case R.id.menu_help:
                alertDialog(getString(R.string.rs_search_screen_info));
                break;
            case R.id.menu_home:
                //Toast.makeText(this, R.string.rs_we_already_home, Toast.LENGTH_SHORT).show();
                Intent mainPage = new Intent(this, MainActivity.class);
                startActivity(mainPage);
                break;
        }
        mDrawer.closeDrawers();
    }

    /**
     * Handle when user click on search button
     */
    private void handleSearchButtonClick() {
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
            i.putExtra("fav", false);
            startActivity(i);

            // Saving the SharedPrefrences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("R_NAME", recipeName);
            editor.putString("INGREDIENTS", ingredients);
            editor.commit();
        });

    }

    /**
     * Handling When user click on Favorite Button
     */
    private void handleFavoriteButtonClick() {
        favoriteButton.setOnClickListener(view -> {
            openFavoriteActivity();
        });
    }

    /**
     * Open Favorite recipe screen when user click on favorite button
     */
    private void openFavoriteActivity() {
        Intent i = new Intent(RecipeSearchActivity.this, RecipeListActivity.class);
        i.putExtra("fav", true);
        startActivity(i);
    }

    /**
     * Settling Up Alert Dialog
     */
    void alertDialog(String message) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                R.string.rs_continue,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

}