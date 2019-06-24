package com.inc.dayplanner.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.GoogleDriveApi.BaseDemoActivity;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.MuteJobScheduler;
import com.inc.dayplanner.R;



public class LoginActivity extends GoogleDriveOperation {


    public static LoginActivity loginActivityInstance;
    public static ProgressBar loadingBar;
    private ConstraintLayout layout2;




    @Override
    protected void onDriveClientReady() {

        super.onDriveClientReady();
        readFile(getApplicationContext());

        if (GoogleDriveOperation.pathToDataFile.equals("")) {
            selectDatabaseFileFromGoogleDrive();
        } else if (GoogleDriveOperation.driveFileToOpen == null) {
            selectDatabaseFileFromGoogleDrive();
        } else {
            try {
                retrieveContents(GoogleDriveOperation.driveFileToOpen);
            } catch (Exception e) {
                selectDatabaseFileFromGoogleDrive();
            }
        }
        //loadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_login);
        loadingBar = findViewById(R.id.loadingBar);
        layout2 = findViewById(R.id.layout2);


        new Handler().postDelayed(() -> layout2.setVisibility(View.INVISIBLE),3000);


        Button changeAccountButton = findViewById(R.id.changeAccountButton);
        changeAccountButton.setOnClickListener(v -> {

            signOutGoogleAccount();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            GoogleDriveOperation.driveFileToOpen=null;
            GoogleDriveOperation.pathToDataFile=null;

        });

        loginActivityInstance=this;



    }

    public void Login(View view) {

        if(loadingBar.getVisibility() == View.INVISIBLE){

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

            loginActivityInstance=this;
        }

    }





    public void selectDatabaseFileFromGoogleDrive(){
        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    openFileExplorerGoogleDrive(getApplicationContext());
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    createFile(getApplicationContext());
                    PlannerFragment.activityList.clear();
                    break;
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy posiadasz juz baze danych ze swoimi aktywnościami i chcesz ją zaimportować?").setPositiveButton("Tak, zaimportuj dane", dialogClickListener)
                .setNegativeButton("Nie, stworz nową baze", dialogClickListener).show();
    }



}


