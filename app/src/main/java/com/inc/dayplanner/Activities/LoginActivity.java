package com.inc.dayplanner.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;

import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;



public class LoginActivity extends GoogleDriveOperation {


    public static LoginActivity loginActivityInstance;
    public static ProgressBar loadingBar;

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
//        loadingBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loadingBar = findViewById(R.id.loadingBar);
        loginActivityInstance=this;
    }

    public void Login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        loginActivityInstance=this;
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


