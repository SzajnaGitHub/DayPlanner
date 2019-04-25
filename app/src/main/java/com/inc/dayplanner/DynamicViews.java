package com.inc.dayplanner;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.dayplanner.Activities.MainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import static android.content.Context.MODE_PRIVATE;


public class DynamicViews {

    private Context ctx;
    private static final String fileName = "example.txt";

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

    public TextView hourTextView(Context context, String text) {

        final TextView textView = new TextView(context);

        textView.setText(text);
        textView.setWidth((int) (getWidth(context)*0.2));
        textView.setTextSize(15);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("7:30-9:00");
            }
        });


        return textView;
    }


    public TextView activityTextView(Context context, final String text) {

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setWidth((int) (getWidth(context)*0.8));
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setClickable(true);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // SaveToFile(textView.getText().toString(),v);
                SaveToFile("LOLOLOLOLO",v);


                textView.setText(LoadFromFile(v));

            }
        });

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


    private void SaveToFile(String text, View view) {

        FileOutputStream fos = null;

        try {
            fos = view.getContext().openFileOutput(fileName, MODE_PRIVATE);
            fos.write(text.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String LoadFromFile(View view) {
        FileInputStream fis = null;
        StringBuilder sb = null;
        try {
            fis = view.getContext().openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }



        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }

}


