package com.inc.dayplanner.ViewChange;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.inc.dayplanner.Fragments.PlannerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SwipeAdapter extends FragmentStatePagerAdapter {


    public Calendar calendar = Calendar.getInstance();
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private static Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    private static List<PlannerFragment> plannerFragmentList = new ArrayList<>();
    private int count;
    private int previousPosition;
    private boolean dateFormat;



    public SwipeAdapter(FragmentManager fm,int count) {
        super(fm);
        this.count = count;
        if(count == 7) {
            previousPosition = 0;
            dateFormat = true;
        } else {
            previousPosition = 4999;
            dateFormat = false;
        }
    }

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }


    @Override
    public Fragment getItem(int position) {
        calendar.add(Calendar.DATE,position-previousPosition);
        String date = df.format(calendar.getTime());


        PlannerFragment pageFragment = PlannerFragment.newInstance(date,dateFormat,position);

        plannerFragmentList.add(pageFragment);

        mPageReferenceMap.put(position, pageFragment);

        previousPosition=position;
        return pageFragment;
    }


    @Override
    public int getCount() {
        return count;
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

    static public String setDayOfTheWeek(int position) {

            String dayofTheWeek ="";
        switch (position) {

            case 0:
                dayofTheWeek = "Monday";
                break;
            case 1:
                dayofTheWeek = "Tuesday";
                break;
            case 2:
                dayofTheWeek = "Wednesday";
                break;
            case 3:
                dayofTheWeek = "Thursday";
                break;
            case 4:
                dayofTheWeek = "Friday";
                break;
            case 5:
                dayofTheWeek = "Saturday";
                break;
            case 6:
                dayofTheWeek = "Sunday";
                break;
        }
        return dayofTheWeek;

        }

}