package com.inc.dayplanner;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.inc.dayplanner.Fragments.PlannerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class SwipeAdapter extends FragmentStatePagerAdapter {


    private Calendar calendar = Calendar.getInstance();
    private int day = calendar.get(Calendar.DAY_OF_YEAR);
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();


    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment pageFragment = new PlannerFragment();
        Bundle bundle = new Bundle();


        String date = df.format(calendar.getTime());

        //System.out.println(date);
        // System.out.println("numer dnia tygodnia " + setDay(position));
        // System.out.println("pozycja" + position);


        bundle.putString("Date", date);
        bundle.putString("dayOfTheWeek", setDay(position));
        pageFragment.setArguments(bundle);


        mPageReferenceMap.put(position, pageFragment);


        return pageFragment;
    }


    @Override
    public int getCount() {
        return 10000;
    }

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    private String setDay(int position) {

        String dayofTheWeek = null;
        int realDay = (day - 7 + position) % 7 + 1;

        switch (realDay) {

            case Calendar.SUNDAY:
                dayofTheWeek = "Sunday";
                break;
            case Calendar.MONDAY:
                dayofTheWeek = "Monday";
                break;
            case Calendar.TUESDAY:
                dayofTheWeek = "Tuesday";
                break;
            case Calendar.WEDNESDAY:
                dayofTheWeek = "Wednesday";
                break;
            case Calendar.THURSDAY:
                dayofTheWeek = "Thursday";
                break;
            case Calendar.FRIDAY:
                dayofTheWeek = "Friday";
                break;
            case Calendar.SATURDAY:
                dayofTheWeek = "Saturday";
                break;
        }

        //  System.out.println("DAY: " + realDay);
        return dayofTheWeek;
    }


}