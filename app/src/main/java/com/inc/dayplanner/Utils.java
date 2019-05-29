package com.inc.dayplanner;

import android.app.Activity;
import android.content.Intent;


public class Utils {
    private static int sTheme;

    public final static int LIGHT_THEME = 1;
    public final static int DARK_THEME = 2;
    /** * Set the theme of the Activity, and restart it by creating a new Activity of the same type. */
    public static void changeToTheme(Activity activity, int theme,String check)
    { sTheme = theme; activity.finish(); activity.startActivity(new Intent(activity, activity.getClass()).putExtra("isCheched",check));  }
    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity) {
        switch (sTheme) {
            case LIGHT_THEME: activity.setTheme(R.style.AppTheme); break;
            case DARK_THEME: activity.setTheme(R.style.DarkTheme); break; }

    }


}