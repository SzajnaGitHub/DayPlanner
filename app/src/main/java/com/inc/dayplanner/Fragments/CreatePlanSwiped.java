package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;


public class CreatePlanSwiped extends Fragment {

    private ViewPager viewPager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
      //  setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_daily_plan, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getFragmentManager(),7);
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(0);
        viewPager.getAdapter().notifyDataSetChanged();

        return view;
    }

/*

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem goToItem = menu.findItem(R.id.goToButton);
        goToItem.setVisible(false);

    }
*/



}
