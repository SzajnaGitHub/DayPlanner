package com.inc.dayplanner.ViewChange;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.R;


public class DynamicViews {

    private Context ctx;
    private int id;
    private boolean isToDelete = false;
    LinearLayout linearLayout;


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
        textView.setWidth((int) (getWidth(context) * 0.2));
        textView.setTextSize(15);
        textView.setClickable(true);
 /*       textView.setOnLongClickListener(v -> {
                    this.idToDelete = linearLayout.getId();
                    System.out.println("longClick");
                    return false;
                }

        );*/
        textView.setOnClickListener(v -> {
            this.isToDelete = true;

        });
        return textView;
    }


    private TextView activityTextView(Context context, final String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setWidth((int) (getWidth(context) * 0.8));
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setClickable(true);
/*
        textView.setOnLongClickListener(v -> {
                    this.idToDelete = linearLayout.getId();
                    System.out.println("longClick");
                    return false;
                }

                );
*/




//        textView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                textView.setText("long CLICK");
//                return false;
//            }
//        });
      textView.setOnClickListener(v -> {
          this.isToDelete = true;
      });

        return textView;
    }

    public LinearLayout linearLayout(Context context, String hour, String act) {

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(hourTextView(context, hour));
        linearLayout.addView(activityTextView(context, act));

        linearLayout.setBackgroundResource(R.drawable.border_shape);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(100, 20, 0, 20);

        linearLayout.setId(ViewCompat.generateViewId());

        id = linearLayout.getId();

        linearLayout.setLayoutParams(params);
        return linearLayout;
    }

    public int getId() {
        return id;
    }

    public boolean isToDelete() {
        return isToDelete;
    }




}