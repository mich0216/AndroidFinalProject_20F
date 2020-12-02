package com.example.androidfinalproject_20f.sabiha;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.example.androidfinalproject_20f.MainActivity;
import com.example.androidfinalproject_20f.R;

public class EventSearchActivity extends AppCompatActivity {

    /**
     * city name declaration
     */
    private EditText cityNameEditText;

    /**
     * searchEventButton declaration
     */
    private Button searchEventButton;

    /**
     * saving city name
     */
    private SharedPreferences prefs = null;


    private Button savedEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_search);

        cityNameEditText = findViewById(R.id.cityNameEditText);
        searchEventButton = findViewById(R.id.searchEventButton);
        savedEventButton = findViewById(R.id.savedEventButton);

        //Toolbar tBar = (Toolbar) findViewById(R.id.eventToolbar);

//This loads the toolbar, which calls onCreateOptionMev
        //setSupportActionBar(tBar);

/*        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.eventOpen, R.string.eventClose);
        drawer.addDrawerListener(toggle);
        toggle.syncState();  */

        // initializing prefs in TicketmasterSP file
        prefs = getSharedPreferences("TicketmasterSP", Context.MODE_PRIVATE);
        String savedString = prefs.getString("CityName", "");
        cityNameEditText.setText(savedString);


        searchEventButton.setOnClickListener(v -> {

            String cityName = cityNameEditText.getText().toString();

            // Validating if user don't enter city name
            if (cityName.isEmpty()) {
                Toast.makeText(this, R.string.please_enter_city_name, Toast.LENGTH_SHORT).show();
                return;
            }

            saveSharedPrefs(cityName);

            Intent i = new Intent(EventSearchActivity.this, EventListActivity.class);
            i.putExtra("CITY_NAME", cityName);
            startActivity(i);

        });
        savedEventButton.setOnClickListener(v -> {

            Intent i = new Intent(EventSearchActivity.this, EventDatabaseListActivity.class);
            startActivity(i);

        });

    }


   // @Override
   /* public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.eventmenu, menu);
        //    MenuInflater inflater2 = getMenuInflater();
        // inflater.inflate(R.menu.nagvigationmenu, menu);
        return true;
    }*/

    /**
     * saveSharePrefs method saving the city name.
     *
     * @param stringToSave store the city name
     */
    private void saveSharedPrefs(String stringToSave) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("CityName", stringToSave);
        editor.commit();
    }

}