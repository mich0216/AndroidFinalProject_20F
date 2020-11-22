package com.example.androidfinalproject_20f.ahmed;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        recipeListView = findViewById(R.id.recipeListView);
        progressContainer = findViewById(R.id.progressContainer);

        myListAdapter = new MyListAdapter();
        recipeListView.setAdapter(myListAdapter);


        String recipeName = getIntent().getStringExtra("R_NAME");
        String ingredients = getIntent().getStringExtra("INGREDIENTS");

        RecipeQuery rq = new RecipeQuery();
        rq.execute("http://www.recipepuppy.com/api/?i=" + ingredients + "&q=" + recipeName + "&p=3");

        recipeListView.setOnItemLongClickListener((parent, view, position, id) -> {

            Recipe r = elements.get(position);
            new AlertDialog.Builder(RecipeListActivity.this)
                    .setTitle(r.getTitle())
                    .setMessage("Ingredients: " + r.getIngredients())
                    .setPositiveButton("Details", (dialog, which) -> {
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

                    Recipe r = new Recipe(title, recipeUrl, ingredients, imageUrl);
                    elements.add(r);

                }


            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

          //  progressContainer.setVisibility(View.INVISIBLE);
            myListAdapter.notifyDataSetChanged();

            if (elements.isEmpty()) {
                Toast.makeText(RecipeListActivity.this, "No recipies found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}