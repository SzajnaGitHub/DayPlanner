package com.inc.dayplanner.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.inc.dayplanner.Fragments.CreatePlanFragment;
import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private Switch sw;
    private Menu menu;
    private MenuItem itemSwitch;
    private Toolbar toolbar;
    private TextView toolbarDateTextView;
    DateFormat df = new SimpleDateFormat("d MMM yyyy");
    Date dateobj = new Date();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        toolbarDateTextView = findViewById(R.id.toolbarDate);
        toolbarDateTextView.setText(df.format(dateobj));


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
        toolbar.setTitle("Plan");

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.nav_plan:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PlannerFragment(), null).commit();
                drawer.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Plan");
                break;

            case R.id.nav_create_planer:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CreatePlanFragment(), null).commit();
                drawer.closeDrawer(GravityCompat.START);
                toolbar.setTitle("Daily");


                break;


            case R.id.app_bar_switch:
                switchButtonHandler(sw);
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

        View menuItems = findViewById(R.id.nav_view);
        View toolobar = findViewById(R.id.toolbar);

        if (sw.isChecked()) {
            sw.setChecked(false);
            drawer.setBackgroundColor(getResources().getColor(R.color.CreamBackground));
            menuItems.setBackgroundColor(getResources().getColor(R.color.CreamBackground));
            toolobar.setBackgroundColor(getResources().getColor(R.color.CreamBackground));


        } else {
            sw.setChecked(true);
            drawer.setBackgroundColor(getResources().getColor(R.color.DarkToolbar));
            menuItems.setBackgroundColor(getResources().getColor(R.color.DarkToolbar));
            toolobar.setBackgroundColor(getResources().getColor(R.color.DarkBackground));
        }


    }


}

