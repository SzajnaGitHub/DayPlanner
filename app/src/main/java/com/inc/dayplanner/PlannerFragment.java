package com.inc.dayplanner;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;


public class PlannerFragment extends Fragment {

    private GridLayout gridLayout;
    DynamicViews dynamicViews;
    private Context context;
    GridLayout.LayoutParams lp;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);


        final ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

                dynamicViews = new DynamicViews(context);

                gridLayout.addView(dynamicViews.hourTextView(getContext(),"9:30-10:30"));
                gridLayout.addView(dynamicViews.activityTextView(getContext(),"Basen"));

            }
        });


        return view;
    }



}
