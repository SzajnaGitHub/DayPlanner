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

/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * klasa obsługująca menu przesuwalne
 *
 * dziedziczy z FragmentStatePagerAdapter
 */


public class SwipeAdapter extends FragmentStatePagerAdapter {


    public Calendar calendar = Calendar.getInstance();
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private static Map<Integer, Fragment> mPageReferenceMap = new HashMap<>();
    private static List<PlannerFragment> plannerFragmentList = new ArrayList<>();


    private int previousPosition = 4999;

    /**
     * konstruktor pozwalający na zmianę fragmentów
     * @param fm menadżer zmiany fragmentów
     */

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    /**
     * metoda zwraca aktualną pozycje
     * @param object obiekt
     * @return pozycja
     */

    public int getItemPosition(Object object){
        return POSITION_NONE;
    }

    /**
     * metoda pozwalająca na zmianę fragmentów oraz zwrócenie
     * aktualnie wybranego fragmentu
     *
     * @param position
     * @return
     */

    @Override
    public Fragment getItem(int position) {
        calendar.add(Calendar.DATE,position-previousPosition);
        String date = df.format(calendar.getTime());

        PlannerFragment pageFragment = PlannerFragment.newInstance(date,position);

        plannerFragmentList.add(pageFragment);

        mPageReferenceMap.put(position, pageFragment);

        previousPosition=position;
        return pageFragment;
    }

    /**
     * metoda ustawia nam liczbę przesuwalnych fragmentów na 10000
     * @return liczbę zainicjowanych fragmentów
     */

    @Override
    public int getCount() {
        return 10000;
    }

    /**
     * metoda zwraca fragment na podstawie klucza
     * @param key klucz
     * @return fragment
     */

    public Fragment getFragment(int key) {
        return mPageReferenceMap.get(key);
    }

    /**
     * metoda usuwa dany fragment z hashMapy przy usunięciu fragmentu
     * @param container
     * @param position
     * @param object
     */


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        mPageReferenceMap.remove(position);
    }

    /**
     * metoda pozwala na wyświetlanie dnia na podstawie informacji z kalendarza
     * @param position
     * @return
     */

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