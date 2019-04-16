package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.inc.dayplanner.R;

public class DayOfTheWeekFragment extends Fragment {

    private TextView textView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day_of_the_week, container, false);

       // textView = view.findViewById(R.id.demoText);

       // String message = getArguments().getString("message");

       // textView.setText(message);
        return  view;
    }

}
