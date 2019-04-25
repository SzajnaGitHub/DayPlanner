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
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class PlannerFragment extends Fragment {

    private GridLayout gridLayout;
    private DynamicViews dynamicViews;
    private TextView dayTextView;
    private Context context;
    private GridLayout.LayoutParams lp;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);
        dayTextView = view.findViewById(R.id.dayText);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

       dayTextView.setText(new SimpleDateFormat("EEEE",Locale.ENGLISH).format(date.getTime()));

        final ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {

              dynamicViews = new DynamicViews(context);

            gridLayout.addView(dynamicViews.linearLayout(getContext(),"7:30-9:30","Siłownia"));

            }
        });


        return view;
    }



}
