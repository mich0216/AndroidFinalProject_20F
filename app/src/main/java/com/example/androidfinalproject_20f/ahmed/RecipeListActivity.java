package com.example.androidfinalproject_20f.ahmed;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.androidfinalproject_20f.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RecipeListActivity extends AppCompatActivity {

    /**
     * To display recipe list
     */
    private ListView recipeListView;

    /**
     * TO display loading progress bar
     */
    private LinearLayout progressContainer;

    /**
     * List of the recipes object
     */
    private List<Recipe> elements = new ArrayList<>();
    /**
     * Adapter for recipe list view
     */
    private MyListAdapter myListAdapter;

    /**
     *
     */
    private ProgressBar progressbar;

    private boolean isTablet = false;
    private TextView txt_login;
    private Boolean isFromFav;
    private String recipeName, ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        // Set a Toolbar to replace the ActionBar.
        setSupportActionBar(toolbar);

        setupView();
        setupAdapter();
        manageComingBundleData();
        handleRecipeOnLongClickListener();
        handleRecipeOnClickListener();
        checkDeviceType();
        if (isFromFav) {
            elements.clear();
            getData();
        } else {
            RecipeQuery rq = new RecipeQuery();
            rq.execute("http://www.recipepuppy.com/api/?i=" + ingredients + "&q=" + recipeName + "&p=3");
        }
    }

    /**
     * Checking if the device is phone or tablet.
     */
    private void checkDeviceType() {
        View fragmentLoadingSpace = findViewById(R.id.fragmentLoadingSpace);
        if (fragmentLoadingSpace == null) {
            isTablet = false;
        } else {
            isTablet = true;
        }
    }

    /**
     * Managing incoming bundle data
     */
    private void manageComingBundleData() {
        recipeName = getIntent().getStringExtra("R_NAME");
        ingredients = getIntent().getStringExtra("INGREDIENTS");
        isFromFav = getIntent().getBooleanExtra("fav", false);
    }

    /**
     * Setting Up adapter to show recipe list
     */
    private void setupAdapter() {
        myListAdapter = new MyListAdapter();
        recipeListView.setAdapter(myListAdapter);
    }

    /**
     * Initializing views
     */
    private void setupView() {
        recipeListView = findViewById(R.id.recipeListView);
        progressContainer = findViewById(R.id.progressContainer);
        progressbar = findViewById(R.id.progressbar);
        txt_login = findViewById(R.id.txt_login);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * Managing recipe list when user comes back from Details Screen
         */
        if (isFromFav) {
            elements.clear();
            getData();
            if (myListAdapter != null) {
                myListAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Passing recipe data when user click on recipe
     */
    private void handleRecipeOnClickListener() {
        recipeListView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe m = elements.get(position);
            //Create a bundle to pass data to the new fragment
            Bundle dataToPass = new Bundle();
            dataToPass.putLong("Recipe_ID", m.getId());
            dataToPass.putString("TITLE", m.getTitle());
            dataToPass.putString("RECIPEURL", m.getRecipeUrl());
            dataToPass.putString("INGREDIENTS", m.getIngredients());
            dataToPass.putString("IMAGEURL", m.getImageUrl());
            dataToPass.putBoolean("fav", isFromFav);

            if (isTablet) {
                RecipeDetailsFragment dFragment = new RecipeDetailsFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLoadingSpace, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment. Calls onCreate() in DetailFragment
            } else {
                Intent i = new Intent(RecipeListActivity.this, RecipeEmptyActivity.class);
                i.putExtras(dataToPass);
                startActivity(i);
            }

        });
    }

    /**
     * Handling long click press from user on the recipe list
     */
    private void handleRecipeOnLongClickListener() {
        recipeListView.setOnItemLongClickListener((parent, view, position, id) -> {
            Recipe r = elements.get(position);
            new AlertDialog.Builder(RecipeListActivity.this)
                    .setTitle(r.getTitle())
                    .setMessage(getString(R.string.rs_ingredients) + r.getIngredients())
                    .setPositiveButton(R.string.rs_details, (dialog, which) -> {
                        // Reference: StackOverflow - https://stackoverflow.com/questions/3004515/sending-an-intent-to-browser-to-open-specific-url
                        String url = r.getRecipeUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    })
                    .show();

            return false;
        });
    }

    /**
     * Retrieving favorite recipes from database and adding it to the Arraylist
     */
    public void getData() {
        progressbar.setVisibility(View.INVISIBLE);
        txt_login.setVisibility(View.INVISIBLE);

        RecipeDataOpener dbOpener = new RecipeDataOpener(this);
        SQLiteDatabase db = dbOpener.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recipeData", null);
        while (res.moveToNext()) {
            String title = res.getString(res.getColumnIndex(RecipeDataOpener.COL_TITLE));
            String recipeUrl = res.getString(res.getColumnIndex(RecipeDataOpener.COL_RECIPE_URL));
            String ingredients = res.getString(res.getColumnIndex(RecipeDataOpener.COL_INGREDIENTS));
            String imageUrl = res.getString(res.getColumnIndex(RecipeDataOpener.COL_IMAGE_URL));
            Recipe r = new Recipe(title, recipeUrl, ingredients, imageUrl);
            elements.add(r);
        }
        if (elements.isEmpty()) {
            Toast.makeText(RecipeListActivity.this, R.string.rs_no_recipy, Toast.LENGTH_SHORT).show();
        }
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
                if (isFromFav) {
                    alertDialog(getString(R.string.rs_fav_list_info));
                } else {
                    alertDialog(getString(R.string.rs_seach_list_info));
                }

                return true;
            case R.id.menu_home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Open Favorite recipe screen when user click on favorite button
     */
    private void openFavoriteActivity() {
        Intent i = new Intent(RecipeListActivity.this, RecipeListActivity.class);
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

    /**
     * Adapter class for recipe list view
     */
    private class MyListAdapter extends BaseAdapter {

        public int getCount() {
            return elements.size();
        }

        public Recipe getItem(int position) {
            return elements.get(position);
        }

        public long getItemId(int position) {
            Recipe m = getItem(position);
            return m.getId();
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            Recipe recipe = getItem(position);
            //make a new row:
            View newView;
            newView = inflater.inflate(R.layout.row_recipe, parent, false);
            //set what the text should be for this row:
            TextView recipeTitleTextView = newView.findViewById(R.id.recipeTitleTextView);
            recipeTitleTextView.setText(recipe.getTitle());
            //return it to be put in the table
            return newView;
        }
    }

    /**
     * retriving receipe from url
     */
    class RecipeQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {

                // UV Rating - JSON Parsing
                //create a URL object of what server to contact:
                URL urlJson = new URL(urls[0]);

                //open the connection
                HttpURLConnection urlConnectionJson = (HttpURLConnection) urlJson.openConnection();

                //wait for data:
                InputStream responseJson = urlConnectionJson.getInputStream();

                //JSON reading:   Look at slide 26
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(responseJson, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON: Look at slide 27:
                JSONObject recipeResultJsonObject = new JSONObject(result);

                JSONArray recipeJsonArray = recipeResultJsonObject.getJSONArray("results");

                for (int i = 0; i < recipeJsonArray.length(); i++) {

                    JSONObject singleRecipeJsonObject = recipeJsonArray.getJSONObject(i);

                    Log.i("RecipeListActivity", singleRecipeJsonObject.toString());

                    String title = singleRecipeJsonObject.getString("title");
                    String recipeUrl = singleRecipeJsonObject.getString("href");
                    String ingredients = singleRecipeJsonObject.getString("ingredients");
                    String imageUrl = singleRecipeJsonObject.getString("thumbnail");

                    Recipe r = new Recipe(title.trim(), recipeUrl, ingredients, imageUrl);
                    elements.add(r);

                    int p = ((i + 1) * 100) / recipeJsonArray.length();
                    publishProgress(p); // to disuplay the progress bar

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressbar.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressContainer.setVisibility(View.INVISIBLE);
            myListAdapter.notifyDataSetChanged();

            if (elements.isEmpty()) {
                Toast.makeText(RecipeListActivity.this, R.string.rs_no_recipy, Toast.LENGTH_SHORT).show();
            }
        }
    }
}