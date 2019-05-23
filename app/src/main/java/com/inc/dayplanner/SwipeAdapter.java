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
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();

    private int previousPosition = 4999;
    private int penultimatePosition = 4998;

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {
        Fragment pageFragment = new PlannerFragment();
        Bundle bundle = new Bundle();


/*        if(previousPosition<position){

            previousPosition = position;
            System.out.println("+++++++++++++");
        }
        if (previousPosition>position) {
            calendar.add(Calendar.DATE, -1);
            previousPosition = position;
            System.out.println("-------------");
        }*/

         calendar.add(Calendar.DATE,position-previousPosition);
         String date = df.format(calendar.getTime());

         System.out.println(date);
         System.out.println("numer dnia tygodnia " + setDay(position));
         System.out.println("pozycja" + position);


        //bundle.putString("dayOfTheWeek", setDay(position));
        pageFragment.setArguments(bundle);

        bundle.putString("Date", date);
        mPageReferenceMap.put(position, pageFragment);

        previousPosition=position;
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

    static public String setDay(int position) {

        Calendar setCalendar = Calendar.getInstance();
        int days = setCalendar.get(Calendar.DAY_OF_YEAR);


        String dayofTheWeek = null;
        int realDay = (days - 7 + position) % 7 + 1;

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

        return dayofTheWeek;
    }


}