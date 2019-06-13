package com.inc.dayplanner.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inc.dayplanner.R;


/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa, która pośredniczy między viewpagerem a danym fragmentem wyświetlanym w przewijalnym menu
 *
 *
 * dziedziczy z klasy Fragment
 *
 */



public class DayOfTheWeekFragment extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_day_of_the_week, container, false);
        return  view;
    }

}
