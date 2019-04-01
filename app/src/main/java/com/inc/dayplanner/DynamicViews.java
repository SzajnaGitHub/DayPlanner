package com.inc.dayplanner;

import android.content.Context;
import android.os.Build;
import android.widget.TextView;


public class DynamicViews {

    private Context ctx;

    public DynamicViews(Context ctx) {
        this.ctx = ctx;
    }

    public TextView hourTextView(Context context, String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TxtField_hour);
        }

        return textView;
    }


    public TextView activityTextView(Context context, String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.TxtField);
        }

        return textView;
    }


}


