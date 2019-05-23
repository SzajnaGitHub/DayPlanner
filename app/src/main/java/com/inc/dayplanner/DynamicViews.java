package com.inc.dayplanner;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inc.dayplanner.Activities.MainActivity;


public class DynamicViews {

    private Context ctx;

    private int getWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.widthPixels;

    }

    public DynamicViews(Context ctx) {
        this.ctx = ctx;
    }

    private TextView hourTextView(Context context, String text) {

        final TextView textView = new TextView(context);

        textView.setText(text);
        textView.setWidth((int) (getWidth(context)*0.2));
        textView.setTextSize(15);
        textView.setClickable(true);
        textView.setOnClickListener(v -> textView.setText("7:30-9:00"));


        return textView;
    }


    private TextView activityTextView(Context context, final String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setWidth((int) (getWidth(context)*0.8));
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setClickable(true);
        textView.setOnClickListener(v -> textView.setText("twoja stara"));

        return textView;
    }

    public LinearLayout linearLayout(Context context,String hour,String act ) {

        final LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(hourTextView(context,hour));
        linearLayout.addView(activityTextView(context,act));

        linearLayout.setBackgroundResource(R.drawable.border_shape);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(100,20, 0, 20);
        linearLayout.setLayoutParams(params);
        return linearLayout;
    }


}


