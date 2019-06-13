package com.inc.dayplanner.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;


/**
 * @author Marcin Szajna, Kacper Seweryn
 *
 * Login Activity jest klasą uruchamiającą się zawsze jako pierwsza
 * dziedziczy z klasy GoogleDriveOperation
 *
 */


public class LoginActivity extends GoogleDriveOperation {


    public static LoginActivity loginActivityInstance;

    /**
     * metoda onDriveClientReady
     * sprawdza czy użytkownik ma już zaimportowany plik bazy danych, jeżeli nie, informuje go o możliwości importu lub stworzenia nowego pliku bazy danych
     */


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
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        loginActivityInstance=this;
    }

    /**
     * metoda selectDatabaseFileFromGoogleDrive
     * umożliwia zaimportowanie pliku z dysku Google Drive
     */
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


