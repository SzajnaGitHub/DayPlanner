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

/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa posiadająca viewpager obsługujący przewijalny layout
 *
 * dziedziczy z klasy Fragment
 *
 */

public class CreatePlanFragment extends Fragment {

    public static ViewPager viewPager;


    /**
     * metoda uruchamia się przy tworzeniu fragmentu
     * ustawia menu aplikacji
     *
     * @param savedInstanceState
     */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    /**
     * metoda ustawia viewpager oraz swipeAdapter
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_create_plan, container, false);
        viewPager = view.findViewById(R.id.view_pager);
        SwipeAdapter swipeAdapter = new SwipeAdapter(getFragmentManager());
        viewPager.setAdapter(swipeAdapter);
        viewPager.setCurrentItem(4999);
        viewPager.getAdapter().notifyDataSetChanged();

        return view;
    }



}
