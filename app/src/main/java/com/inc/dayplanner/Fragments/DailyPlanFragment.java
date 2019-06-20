package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;


public class DailyPlanFragment extends Fragment {

    public static ViewPager viewPager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_daily_plan, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getFragmentManager(),10000);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(4999);
        viewPager.getAdapter().notifyDataSetChanged();

        return view;
    }



}
