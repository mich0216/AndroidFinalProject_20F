package com.example.androidfinalproject_20f.ahmed;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidfinalproject_20f.R;

import java.io.InputStream;

public class RecipeDetailsFragment extends Fragment {

    private ImageView recipeImage;
    private TextView recipeTitle,recipeIngredients;
    private Button recipeUrlButton, hideButton, add_to_fav_button;
    // Database object
    private SQLiteDatabase db;
    private String title, recipeUrl, ingredients, imageUrl = "";
    /**
     * Boolean to check User is comn favorite screen or Search Screen
     */
    private Boolean isFromFav;
    private AppCompatActivity parentActivity;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_details, container, false);
        setupView(v);
        manageBundleData();
        handleRecipeUrlButton();
        handleHideButtonClick();
        handleFavoriteButtonClick();
        //        // load image from URL
//        // Download recipe image from internet
//        URL urlIcon = null;
//        Bitmap image = null;
//        try {
//            urlIcon = new URL(imageUrl);
//
//            HttpURLConnection connection = (HttpURLConnection) urlIcon.openConnection();
//            connection.connect();
//            int responseCode = connection.getResponseCode();
//            if (responseCode == 200) {
//                image = BitmapFactory.decodeStream(connection.getInputStream());
//                recipeImage.setImageBitmap(image);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return v;
    }

    /**
     * Managing incoming bundle data
     */
    private void manageBundleData() {
        Bundle bundle = getArguments();
        long id = bundle.getLong("Recipe_ID");
        title = bundle.getString("TITLE");
        recipeUrl = bundle.getString("RECIPEURL");
        ingredients = bundle.getString("INGREDIENTS");
        imageUrl = bundle.getString("IMAGEURL");
        isFromFav = bundle.getBoolean("fav",false);

        /**
         * Checking if the User Selected favorite or search
         */
        if (isFromFav){
            add_to_fav_button.setText(R.string.rs_remove_from_fav);
        }
        recipeTitle.setText(getString(R.string.rs_title) + title);
        recipeIngredients.setText(getString(R.string.rs_ingredients) + ingredients);
    }

    /**
     * Setting up view
     */
    private void setupView(View v) {
        //        recipeImage = v.findViewById(R.id.recipeImage);
        recipeTitle = v.findViewById(R.id.recipeTitle);
        recipeIngredients = v.findViewById(R.id.recipeIngredients);
        recipeUrlButton = v.findViewById(R.id.recipeUrlButton);
        hideButton = v.findViewById(R.id.hideButton);
        add_to_fav_button = v.findViewById(R.id.add_to_fav_button);
    }

    /**
     * Handle Recipe Url Button
     */
    private void handleRecipeUrlButton() {
        recipeUrlButton.setOnClickListener(v1 -> {
            // Reference: StackOverflow - https://stackoverflow.com/questions/3004515/sending-an-intent-to-browser-to-open-specific-url
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(recipeUrl));
            startActivity(i);
        });
    }

    /**
     * Handle Hide Button Click
     */
    private void handleHideButtonClick() {
        hideButton.setOnClickListener(v1 -> {
            parentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
            parentActivity.finish();
        });
    }

    /**
     * Handle Favorite Button Click
     */
    private void handleFavoriteButtonClick() {
        add_to_fav_button.setOnClickListener(view -> {
            if (isFromFav){
                deleteFavoriteRecipeFromDatabase(title);
                Toast.makeText(getActivity(), R.string.rs_recipe_removed, Toast.LENGTH_SHORT).show();
            }else {
                if (checkIfRecipeAlreadyInDatabase(title)){
                    Toast.makeText(getActivity(), R.string.rs_recipy_already_added, Toast.LENGTH_SHORT).show();
                }else {
                    long returnedId = addFavoriteRecipeToDatabase();
                    if (returnedId <= 0) {
                        Toast.makeText(getActivity(), R.string.rs_unable_to_add_fav, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), R.string.rs_added_to_fav, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
            // checking if recipe in db using title

    private Boolean checkIfRecipeAlreadyInDatabase(String title) {
        RecipeDataOpener dbOpener = new RecipeDataOpener(getActivity());
        db = dbOpener.getWritableDatabase();
        String selectQuery = "SELECT * FROM " + RecipeDataOpener.TABLE_NAME + " where " + RecipeDataOpener.COL_TITLE + "='" + title + "'";
        Cursor crsr = db.rawQuery(selectQuery, null);
        if (crsr.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

        /**
     * Deleting Favorite Recipe From the Database
     */
    private void deleteFavoriteRecipeFromDatabase(String title){
        RecipeDataOpener dbOpener = new RecipeDataOpener(getActivity());
        db = dbOpener.getWritableDatabase();
        String[] whereArgs ={title};
        int count =db.delete(RecipeDataOpener.TABLE_NAME ,RecipeDataOpener.COL_TITLE+" = ?",whereArgs);

    }

    /**
     * Adding Favorite Recipe to the Database.
     */
    private long addFavoriteRecipeToDatabase(){
        RecipeDataOpener dbOpener = new RecipeDataOpener(getActivity());
        db = dbOpener.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecipeDataOpener.COL_TITLE, title);
        contentValues.put(RecipeDataOpener.COL_RECIPE_URL, recipeUrl);
        contentValues.put(RecipeDataOpener.COL_IMAGE_URL, imageUrl);
        contentValues.put(RecipeDataOpener.COL_INGREDIENTS, ingredients);
        long id = db.insert(RecipeDataOpener.TABLE_NAME, null , contentValues);
        return id;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}