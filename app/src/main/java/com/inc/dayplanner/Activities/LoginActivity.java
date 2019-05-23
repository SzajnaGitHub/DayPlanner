package com.inc.dayplanner.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveContents;
import android.widget.TextView;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.inc.dayplanner.GoogleDriveApi.BaseDemoActivity;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.GoogleDriveApi.ReadFileLocal;
import com.inc.dayplanner.GoogleDriveApi.SaveFileLocal;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;


import static com.google.android.gms.drive.DriveId.decodeFromString;


public class LoginActivity extends GoogleDriveOperation {


    private TextView mFileContents;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFileContents = findViewById(R.id.mFileContents);
        mFileContents.setText("");
    }

    public void Login(View view) {

        readFile(getApplicationContext());

        if(GoogleDriveOperation.pathToDataFile.equals("")){
            selectDatabaseFileFromGoogleDrive();
        }
        else if(GoogleDriveOperation.driveFileToOpen==null){
            selectDatabaseFileFromGoogleDrive();
        }else {
            try {
                retrieveContents(GoogleDriveOperation.driveFileToOpen);
                changeActivity();
            } catch (Exception e) {
                selectDatabaseFileFromGoogleDrive();
            }
        }

//        ReadFileLocal readFileLocal = new ReadFileLocal();
//        String fileData=readFileLocal.readFile(getApplicationContext());
//        System.out.println(fileData);

    }
    /////////////////////////////////////////////////////////////////////////////////////////////


    private void changeActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void decodePathToGoogleFile(){
        try {
            GoogleDriveOperation.driveFileToOpen = decodeFromString(GoogleDriveOperation.pathToDataFile).asDriveFile();
        }catch(Exception e){
            GoogleDriveOperation.driveFileToOpen=null;
        }
    }


    public void selectDatabaseFileFromGoogleDrive(){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        openFileExplorerGoogleDrive(getApplicationContext());
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        createFile(getApplicationContext());
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Czy posiadasz juz baze danych ze swoimi aktywnościami i chcesz ją zaimportować?").setPositiveButton("Tak, zaimportuj dane", dialogClickListener)
                .setNegativeButton("Nie, stworz nową baze", dialogClickListener).show();
    }


}



