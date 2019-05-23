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

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {


    private  TextView tv1;
    private String text;



    @SuppressLint("ValidFragment")
    public TimePickerFragment(TextView tv1, String text) {

        this.tv1 = tv1;
        this.text = text;
    }

    public TimePickerFragment(){
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new android.app.TimePickerDialog(getActivity(),R.style.DialogTheme, this,
        hour,minute, DateFormat.is24HourFormat(getActivity()));
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

            tv1.setText(text+ hourOfDay+ ":"+ minute);

    }



}
