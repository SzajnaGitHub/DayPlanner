package com.inc.dayplanner.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.inc.dayplanner.AlertReceiver;
import com.inc.dayplanner.CheckMuteThread;
import com.inc.dayplanner.Fragments.CreatePlanFragment;
import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends GoogleDriveOperation implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Switch sw;
    private Menu menu;
    private MenuItem itemSwitch;
    private Toolbar toolbar;
    public final PlannerFragment plannerFragment = new PlannerFragment();
    private String swipeChecked;
    private Intent intent;
    public static boolean importData;
    public static MainActivity mainActivity;
    public static String timeEarlierReminder;
    public static String contentActivityReminder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        mainActivity=this;


       //Runnable muteChecker = new CheckMuteThread();
       // muteChecker.run();




        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                swipeChecked = null;
            } else {
                swipeChecked = extras.getString("isCheched");
            }
        } else {
            swipeChecked = (String) savedInstanceState.getSerializable("isCheched");
        }


        toolbar = findViewById(R.id.toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        menu = navigationView.getMenu();
        itemSwitch = menu.findItem(R.id.app_bar_switch);
        itemSwitch.setActionView(R.layout.switch_item);
        sw = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switcher);


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlannerFragment(), null).commit();
        drawer.closeDrawer(GravityCompat.START);
        toolbar.setTitle("Today");


        if (swipeChecked != null){

            if(swipeChecked.equals("light")){
                sw.setChecked(false);
            }
            else{
                sw.setChecked(true);
            }
        }
    }





    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_plan:
//                setNotification();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlannerFragment(), null).commit();
                drawer.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Today");
                break;

            case R.id.nav_create_planer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreatePlanFragment(), null).commit();
                drawer.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Daily");
                break;


            case R.id.app_bar_switch:
                switchButtonHandler(sw);
                break;

            case R.id.app_bar_logout:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                LoginActivity.loginActivityInstance.signOutGoogleAccount();
                LoginActivity.loginActivityInstance.recreate();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;

            case R.id.app_bar_import:
                GoogleDriveOperation.driveFileToOpen=null;
                importData=true;
                PlannerFragment.activityList.clear();
                LoginActivity.pathToDataFile="";
                openFileExplorerGoogleDrive(getApplicationContext());
//                LoginActivity.loginActivityInstance.recreate();
                break;


        }


        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void switchButtonHandler(Switch sw) {

            if (sw.isChecked()) {
                sw.setChecked(false);
                Utils.changeToTheme(this, Utils.LIGHT_THEME, "light");

            } else {
                sw.setChecked(true);
                Utils.changeToTheme(this, Utils.DARK_THEME, "dark");

            }

        }


        public void setNotification(String dateToParse,String activity, String timeEarlier){
            Calendar c = Calendar.getInstance();
            timeEarlierReminder=timeEarlier;
            contentActivityReminder=activity;

            SimpleDateFormat reminderDF = new SimpleDateFormat("HH:mm-d MMM yyyy");
            try {
                c.setTime(reminderDF.parse(dateToParse));
            } catch (ParseException e) {
                e.printStackTrace();
            }

//            c.setTime(date);
//            c.set(Calendar.HOUR_OF_DAY,hour);
//            c.set(Calendar.MINUTE, minute);
//            c.set(Calendar.SECOND, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }


}