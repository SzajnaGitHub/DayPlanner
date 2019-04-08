package com.inc.dayplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class LoginActivity extends AppCompatActivity {



    private static final String TAG = "Google Drive Activity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view) {
        BaseDriveActivity baseDriveActivity = new BaseDriveActivity();
        baseDriveActivity.signIn();
        //Intent intent = new Intent(this, GoogleDriveFileHolder.class);
        //startActivity(intent);
    }



}



