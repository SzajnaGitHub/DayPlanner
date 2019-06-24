package com.inc.dayplanner.Fragments;


import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;

import java.text.DateFormat;
import java.util.Calendar;

public class CreatePlan extends Fragment {

    private TextView fromDate;
    private TextView toDate;
    private String whichDateTextIsPicked;
    private Button createPlanButton;
    private RadioButton radioNo;
    private RadioButton radioYes;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);
        fromDate= view.findViewById(R.id.fromDate);
        toDate = view.findViewById(R.id.toDate);

        fromDate.setOnClickListener(v -> {

            DatePickerFragment datePicker = new DatePickerFragment();
            datePicker.setCallBack(ondate);
            if (getFragmentManager() != null) {
                datePicker.show(getFragmentManager(),"date picker");

            }

            whichDateTextIsPicked = "fromDate";
        });
        toDate.setOnClickListener(v -> {

            DatePickerFragment datePicker = new DatePickerFragment();
            datePicker.setCallBack(ondate);
            if (getFragmentManager() != null) {
                datePicker.show(getFragmentManager(),"date picker");
            }

            whichDateTextIsPicked = "toDate";
        });





        radioNo = view.findViewById(R.id.radioNo);
        radioYes = view.findViewById(R.id.radioYes);
        createPlanButton = view.findViewById(R.id.createPlanButton);
        createPlanButton.setOnClickListener(v -> {

            if(!fromDate.getText().equals("click") || !toDate.getText().equals("click")) {
                if (getFragmentManager() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreatePlanSwiped(), null).addToBackStack(null).commit();
                }

            } else {
                fromDate.setTextColor(Color.parseColor("#e71837"));
                toDate.setTextColor(Color.parseColor("#e71837"));
                createPlanButton.setBackgroundResource(R.drawable.round_button_red);
            }


        });


        return view;
    }


    DatePickerDialog.OnDateSetListener ondate = (view, year, monthOfYear, dayOfMonth) -> {

        System.out.println("DateListener");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String currentDateString = DateFormat.getDateInstance().format(c.getTime());

        if(whichDateTextIsPicked.equals("fromDate")) {
            fromDate.setText(currentDateString);
        } else {
            toDate.setText(currentDateString);
        }


    };


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem goToItem = menu.findItem(R.id.goToButton);
        goToItem.setVisible(false);

    }
}

