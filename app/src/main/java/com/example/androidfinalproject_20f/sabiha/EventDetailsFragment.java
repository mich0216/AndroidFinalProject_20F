package com.example.androidfinalproject_20f.sabiha;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.androidfinalproject_20f.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class EventDetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private AppCompatActivity parentActivity;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
//        id = dataFromActivity.getLong(ChatRoomActivity.MESSAGE_ID );

        // Inflate the layout for this fragment

        View result = inflater.inflate(R.layout.fragment_event_details, container, false);

        //show the event details
        TextView eName = (TextView) result.findViewById(R.id.eName);
        eName.setText(getString(R.string.event_name) + dataFromActivity.getString(EventListActivity.EVENT_NAME));

        TextView eStartDate = (TextView) result.findViewById(R.id.eStartDate);
        eStartDate.setText(getString(R.string.start_date) + dataFromActivity.getString(EventListActivity.EVENT_START_DATE));

        TextView eMinPrice = (TextView) result.findViewById(R.id.eMinPrice);
        eMinPrice.setText(getString(R.string.min_price) + dataFromActivity.getDouble(EventListActivity.EVENT_MIN_PRICE));

        TextView eMaxPrice = (TextView) result.findViewById(R.id.eMaxPrice);
        eMaxPrice.setText(getString(R.string.max_price) + dataFromActivity.getDouble(EventListActivity.EVENT_MAX_PRICE));

        Button eSaveEventButton = (Button) result.findViewById(R.id.eSaveEventButton);
        eSaveEventButton.setOnClickListener(Clk -> {

            EventMyOpener dbOpener = new EventMyOpener(parentActivity);
            db = dbOpener.getWritableDatabase();

            ContentValues newRowValue = new ContentValues();
            //Now provide a value for every database column defined in MyOpener.java:
            //put string name in the NAME column:
            newRowValue.put(EventMyOpener.COL_NAME, dataFromActivity.getString(EventListActivity.EVENT_NAME));
            newRowValue.put(EventMyOpener.COL_STARTDATE, dataFromActivity.getString(EventListActivity.EVENT_START_DATE));
            newRowValue.put(EventMyOpener.COL_MINPRICE, dataFromActivity.getDouble(EventListActivity.EVENT_MIN_PRICE));
            newRowValue.put(EventMyOpener.COL_MAXPRICE, dataFromActivity.getDouble(EventListActivity.EVENT_MAX_PRICE));
            newRowValue.put(EventMyOpener.COL_TICKETMASTERURL, dataFromActivity.getDouble(EventListActivity.EVENT_TICKETMASTER_URL));
            newRowValue.put(EventMyOpener.COL_IMAGEURL, dataFromActivity.getDouble(EventListActivity.EVENT_IMAGE_URL));

            //Now insert in the database:
            long newId = db.insert(EventMyOpener.TABLE_NAME, null, newRowValue);

        });

        // get the Hide button, and add a click listener:
        Button hideButton = (Button) result.findViewById(R.id.hideButton);
        hideButton.setOnClickListener(Clk -> {
            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity) context;
    }
}