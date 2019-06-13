package com.inc.dayplanner.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.inc.dayplanner.R;

import java.util.Calendar;


/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa, która pozwala na wyświetlanie kalendarza przy uruchamianiu przycisku
 *
 * dziedziczy z klasy DialogFragment
 *
 */


public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener ondateSet;

    /**
     * metoda ustawia metode obsługująca kalendarz
     * @param ondate data
     */

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }


    /**
     * metoda ucuhamia się przy tworzeniu Fragmentu
     * buduje fragment oraz strukturę kalendarza
     *
     * @param savedInstanceState
     * @return fragment będący kalendarzem
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.DialogTheme,ondateSet,
                year,month,day);
    }
}
