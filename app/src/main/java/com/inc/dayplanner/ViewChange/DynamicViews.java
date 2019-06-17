package com.inc.dayplanner.ViewChange;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.R;

import org.w3c.dom.Text;


public class DynamicViews {

    private Context ctx;
    private int id;
    private String hourText;
    private String activityText;
    private LinearLayout linearLayoutVariable;
    private TextView houtTV;
    private TextView activityTV;


    public DynamicViews(Context ctx) {
        this.ctx = ctx;
    }

    public TextView hourTextView(Context context, String text) {

        houtTV = new TextView(context);

        hourText=text;
        houtTV.setText(text);
        houtTV.setWidth((int) (getWidth(context) * 0.2));
        houtTV.setTextSize(15);
        houtTV.setClickable(true);
        return houtTV;
    }


    public TextView activityTextView(Context context, final String text) {

        activityText=text;
        activityTV = new TextView(context);
        activityTV.setText(text);
        activityTV.setWidth((int) (getWidth(context) * 0.8));
        activityTV.setTextSize(20);
        activityTV.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        activityTV.setClickable(true);
        return activityTV;
    }

    public LinearLayout linearLayout(Context context,TextView tvHour, TextView tvActivity) {

        linearLayoutVariable = new LinearLayout(context);
        linearLayoutVariable.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutVariable.addView(tvHour);
        linearLayoutVariable.addView(tvActivity);

        linearLayoutVariable.setBackgroundResource(R.drawable.border_shape);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 20, 0, 20);

        linearLayoutVariable.setId(ViewCompat.generateViewId());

        id = linearLayoutVariable.getId();

        linearLayoutVariable.setLayoutParams(params);


        return linearLayoutVariable;
    }

    public int getId() {
        return id;
    }

    public String getHourText() {
        return hourText;
    }

    public String getActivityText() {
        return activityText;
    }

    public TextView getHoutTV() {
        return houtTV;
    }

    public TextView getActivityTV() {
        return activityTV;
    }

    public LinearLayout getLinearLayoutVariable() {
        return linearLayoutVariable;
    }

    private int getWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.widthPixels;

    }

}