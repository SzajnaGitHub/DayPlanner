package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;


public class CreatePlanFragment extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);

        ViewPager viewPager = view.findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(4999);
        viewPager.getAdapter().notifyDataSetChanged();





        return view;
    }



}
