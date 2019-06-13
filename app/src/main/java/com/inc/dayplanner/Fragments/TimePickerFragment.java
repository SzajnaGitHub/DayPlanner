package com.inc.dayplanner.Fragments;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TextView;
import android.widget.TimePicker;
import com.inc.dayplanner.R;
import java.util.Calendar;




/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa, która pozwala na wyświetlanie zegara przy wciśnięciu przycisku
 *
 * dziedziczy z klasy DialogFragment
 * implementuje interfejs OnTimeSetListener
 */


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private  TextView tv1;

    @SuppressLint("ValidFragment")
    public TimePickerFragment(TextView tv1) {

        this.tv1 = tv1;
    }

    public TimePickerFragment(){
    }

    /**
     * metoda uruchamia się przy tworzeniu fragmentu
     * w tej metodzie ustawia się wszystkie parametry oraz wygląd zwracanego zegara
     * @param savedInstanceState
     * @return zegar
     */

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new android.app.TimePickerDialog(getActivity(),R.style.DialogTheme, this,
        hour,minute, DateFormat.is24HourFormat(getActivity()));
    }

    /**
     * metoda obsługująca wybranie danej godziny na zegarze
     * @param view aktualnyy widok
     * @param hourOfDay podana przez nas godzina
     * @param minute ponada przez nas minuta
     */

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        String minutes;
        String hours;

        if(minute<10) {
            minutes = 0 + "" + minute;
        }else{
            minutes=minute+"";
        }
        if(hourOfDay<10){
            hours = 0+""+hourOfDay;
        }else{
        hours=hourOfDay+"";
    }

        tv1.setText(hours+ ":"+ minutes);

    }



}
