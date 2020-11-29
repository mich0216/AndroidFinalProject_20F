package com.example.androidfinalproject_20f.chrish;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.androidfinalproject_20f.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the factory method to
 * create an instance of this fragment.
 */
public class CovidDetailsFragment extends Fragment {

    private Bundle dataFromActivity;
    private String resultByDate;
    private AppCompatActivity parentActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        resultByDate = dataFromActivity.getString(ViewHistory.DATE);

        View result =  inflater.inflate(R.layout.datalayout, container, false);
        
        TextView province = result.findViewById(R.id.provinceName);
        TextView caseNumber = result.findViewById(R.id.caseNumber);
        province.setText(resultByDate);
        caseNumber.setText("1000");
        return result;
       /* View result =  inflater.inflate(R.layout.fragment_covid_details, container, false);

        TextView title = (TextView)result.findViewById(R.id.title);
        ListView caseNumber = result.findViewById(R.id.searchListView);
        caseNumber.(dataFromActivity.getString(ViewHistory.DATE));

        return result;*/
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}