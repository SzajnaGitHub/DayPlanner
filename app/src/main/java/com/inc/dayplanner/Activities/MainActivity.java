package com.inc.dayplanner.Activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import com.inc.dayplanner.AlertReceiver;
import com.inc.dayplanner.Fragments.AboutFragment;
import com.inc.dayplanner.Fragments.CreatePlanFragment;
import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Klasa MainActivity jest główną klasą projektu, zarządza ona wymianą fragmentów
 * dziedziczy z klasy GoogleDriveOperation i implementuje interfejs OnNavigationItemSelectedListener
 */


public class MainActivity extends GoogleDriveOperation implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Switch sw;
    public Toolbar toolbar;
    public static boolean importData;


    /**
     * metoda deklaruje obsługę przycisków na toolbarze
     * @param item dany przycisk na toolbarze
     * @return fałsz, ponieważ w tym miejscu tylko dany przycisk jest zadeklaorwany
     */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       switch(item.getItemId()){
           case R.id.goToButton:
               return false;

       }

        return false;
    }

    public static MainActivity mainActivity;
    public static String timeEarlierReminder;
    public static String contentActivityReminder;


    /**
     * metoda ustawiająca menu aplikacji na podane przez nas
     * @param menu podane przez nas menu
     * @return true
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_menu,menu);
        MenuItem goToItem = menu.findItem(R.id.goToButton);
       // goToItem.setVisible(false);
        return true;
    }

    /**
     * funkcja ustawiająca wszystkie parametry
     * uruchamiana jest podczas tworzenia Aktywności
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.onActivityCreateSetTheme(this);
        setContentView(R.layout.activity_main);
        mainActivity=this;


        String swipeChecked;
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
        setSupportActionBar(toolbar);


        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Menu menu = navigationView.getMenu();
        MenuItem itemSwitch = menu.findItem(R.id.app_bar_switch);
        itemSwitch.setActionView(R.layout.switch_item);
        sw = menu.findItem(R.id.app_bar_switch).getActionView().findViewById(R.id.switcher);



        if (swipeChecked != null){

            if(swipeChecked.equals("light")){
                sw.setChecked(false);
            }
            else{
                sw.setChecked(true);
            }
        }

        runFragmentMethod(new PlannerFragment());
        toolbar.setTitle("Today");

    }


    /**
     * metoda obsługująca wszystkie  przyciski na panelu bocznym
     * @param menuItem dany przycisk na panelu bocznym
     * @return
     */

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_plan:
                runFragmentMethod(new PlannerFragment());
                toolbar.setTitle("Today");
                break;

            case R.id.nav_create_planer:
                runFragmentMethod(new CreatePlanFragment());
                toolbar.setTitle("Daily");
                break;

            case R.id.app_bar_switch:
                switchButtonHandler(sw);
                break;

            case R.id.app_bar_logout:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                LoginActivity.loginActivityInstance.signOutGoogleAccount();
                LoginActivity.loginActivityInstance.recreate();
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                GoogleDriveOperation.driveFileToOpen=null;
                GoogleDriveOperation.pathToDataFile=null;
                break;

            case R.id.app_bar_import:
                importData=true;
                openFileExplorerGoogleDrive(getApplicationContext());
                break;

            case R.id.app_bar_info:
                runFragmentMethod(new AboutFragment());
                toolbar.setTitle("About");
                break;

            case R.id.app_bar_synchronize:
                synchronize();
                break;

        }

        return true;
    }


    /**
     * metoda obsługująca przycisk back* powodująca minimalizacje aplikacji przy
     * wciśnięciu przycisku
     */
    @Override
    public void onBackPressed() {
        this.moveTaskToBack(true);
    }

    /**
     * metoda obsługująca przycik do zmiany trybu z dziennego na nocny
     * @param sw podany przycisk
     */

    public void switchButtonHandler(Switch sw) {

            if (sw.isChecked()) {
                sw.setChecked(false);
                Utils.changeToTheme(this, Utils.LIGHT_THEME, "light");

            } else {
                sw.setChecked(true);
                Utils.changeToTheme(this, Utils.DARK_THEME, "dark");

            }

        }

    /**
     * ustawianie powiadomienia przy ustawieniu aktywności na aktywności przypomnienia
     * @param dateToParse  dokładny czas w którym ma się pojawić powiadomienie
     * @param activity dana  aktywność
     * @param timeEarlier ile czasu wcześniej wyświetlić komunikat
     */

    public void setNotification(String dateToParse,String activity, String timeEarlier){
            Calendar c = Calendar.getInstance();

            timeEarlierReminder=timeEarlier;
            contentActivityReminder=activity;

            @SuppressLint("SimpleDateFormat") SimpleDateFormat reminderDF = new SimpleDateFormat("HH:mm-d MMM yyyy");
            try {
                    c.setTime(reminderDF.parse(dateToParse));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(this, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
        }


    /**
     * metoda zamieniająca fragmenty
     * @param newFragment nowy fragment, który zostanie wyświetlony
     */

    public void runFragmentMethod(Fragment newFragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newFragment, null).commit();
        drawer.closeDrawer(GravityCompat.START);

    }


    /**
     * metoda synchronizujaca dane znajdujace sie w aplikacji z danymi na dysku Google Drive
     *
     */
    private void synchronize(){
        retrieveContents(GoogleDriveOperation.driveFileToOpen);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



}