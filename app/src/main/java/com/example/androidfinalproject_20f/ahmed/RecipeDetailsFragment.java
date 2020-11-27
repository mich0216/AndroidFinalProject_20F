package com.example.androidfinalproject_20f.ahmed;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidfinalproject_20f.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class RecipeDetailsFragment extends Fragment {

    private ImageView recipeImage;
    private TextView recipeTitle;
    private TextView recipeIngredients;
    private Button recipeUrlButton;
    private Button hideButton;

    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recipe_details, container, false);

//        recipeImage = v.findViewById(R.id.recipeImage);
        recipeTitle = v.findViewById(R.id.recipeTitle);
        recipeIngredients = v.findViewById(R.id.recipeIngredients);
        recipeUrlButton = v.findViewById(R.id.recipeUrlButton);
        hideButton = v.findViewById(R.id.hideButton);

        Bundle bundle = getArguments();
        long id = bundle.getLong("Recipe_ID");
        String title = bundle.getString("TITLE");
        String recipeUrl = bundle.getString("RECIPEURL");
        String ingredients = bundle.getString("INGREDIENTS");
        String imageUrl = bundle.getString("IMAGEURL");

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

        recipeTitle.setText("Title: " + title);
        recipeIngredients.setText("Ingredients: " + ingredients);


        recipeUrlButton.setOnClickListener(v1 -> {

            // Reference: StackOverflow - https://stackoverflow.com/questions/3004515/sending-an-intent-to-browser-to-open-specific-url
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(recipeUrl));
            startActivity(i);

        });

        hideButton.setOnClickListener(v1 -> {
            parentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .remove(this)
                    .commit();
        });

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}