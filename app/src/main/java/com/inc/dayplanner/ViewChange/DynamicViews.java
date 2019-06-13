package com.inc.dayplanner.ViewChange;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.R;

/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Klasa posiadająca metody dodawania poszczególnych
 * elementów takich jak
 * textView zawierający godzinę danej aktywności
 * textView zawierający opis danej aktywności
 * oraz LinearLayout, w którym podane wyżej textView się zawierają
 *
 */



public class DynamicViews {

    private Context ctx;
    private int id;
    private String hourText;
    private String activityText;

    /**
     * konstruktor ustawiający kontekst
     * @param ctx podany przez nas kontekst
     */

    public DynamicViews(Context ctx) {
        this.ctx = ctx;
    }

    /**
     * ustawianie textView zawierającego godzinę rozpoczęcia
     * oraz godzinę zakończenia danej aktywności
     *
     * metoda pozwala na ustawienie:
     * -tekstu
     * -wymiarów
     * -oraz obsługi przy dłuższym kliknięciu na daną aktywność
     *
     * @param context kontekst
     * @param text nasz tekst który będzie się wyświetlał na ekranie
     * @return textView
     */

    public TextView hourTextView(Context context, String text) {

        final TextView textView = new TextView(context);

        hourText=text;
        textView.setText(text);
        textView.setWidth((int) (getWidth(context) * 0.2));
        textView.setTextSize(15);
        textView.setClickable(true);
        return textView;
    }

    /**
     * ustawianie textView zawierającego opis danej aktywności
     *
     * metoda pozwala na ustawienie:
     * -tekstu
     * -wymiarów
     * -oraz obsługi przy dłuższym kliknięciu na daną aktywność
     *
     * @param context kontekst
     * @param text nasz tekst który będzie się wyświetlał na ekranie
     * @return textView
     */




    public TextView activityTextView(Context context, final String text) {

        activityText=text;
        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setWidth((int) (getWidth(context) * 0.8));
        textView.setTextSize(20);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setClickable(true);
        return textView;
    }


    /**
     * ustawianie LinearLayoutu zawierającego textView godzin trwania aktywności
     * oraz textView zawierający opis aktywności
     *
     * metoda pozwala na ustawienie:
     * -tekstu
     * -wymiarów
     * -oraz obsługi przy dłuższym kliknięciu na daną aktywność
     *
     *
     * @param context kontekst
     * @param tvHour textView zawierający czas trwania aktywności
     * @param tvActivity textView zawierający opis aktywności
     * @return linearlayout zawierający 2 textView
     */



    public LinearLayout linearLayout(Context context,TextView tvHour, TextView tvActivity) {

        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.addView(tvHour);
        linearLayout.addView(tvActivity);

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

    public String getHourText() {
        return hourText;
    }

    public String getActivityText() {
        return activityText;
    }

    private int getWidth(Context context) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((MainActivity) context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics.widthPixels;

    }

}