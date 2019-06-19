package com.inc.dayplanner.GoogleDriveApi;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.inc.dayplanner.Activities.LoginActivity;
import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.Fragments.PlannerFragment;
import com.inc.dayplanner.R;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;

import static com.google.android.gms.drive.DriveId.decodeFromString;




public class GoogleDriveOperation extends BaseDemoActivity {


    public static DriveFile driveFileToOpen;
    public static String pathToDataFile=null;
    private static final String TAG = "GoogleActivity";



    @Override
    protected void onDriveClientReady() {
//        createFile();
//        retrieveContents(GoogleDriveOperation.driveFileToOpen);
//        loadingBar.setVisibility(View.INVISIBLE);
    }

    public void createNewGoogleFileAndAppendID(Context context){
        SaveFileLocal saveFileLocal = new SaveFileLocal();
        saveFileLocal.saveFile(context,getUsername(context), pathToDataFile);
    }


    public void createFile(Context context) {
        // [START drive_android_create_file]
        final Task<DriveFolder> rootFolderTask = getDriveResourceClient().getRootFolder();
        final Task<DriveContents> createContentsTask = getDriveResourceClient().createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = rootFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write("");
                    }

                    MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                            .setTitle("DayPlanner.txt")
                            .setMimeType("text/plain")
                            .setStarred(true)
                            .build();

                    return getDriveResourceClient().createFile(parent, changeSet, contents);
                })
                .addOnSuccessListener(this,
                        driveFile -> {
//                            showMessage(getString(R.string.file_created, driveFile.getDriveId().encodeToString()));
                            pathToDataFile=driveFile.getDriveId().encodeToString();
                            decodePathToGoogleFile();
                            createNewGoogleFileAndAppendID(context);
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to create file", e);
//                    showMessage(getString(R.string.file_create_error));
                    finish();
                });
        // [END drive_android_create_file]
    }



    public void appendContents(DriveFile file, String contentToAppend) {
        // [START drive_android_open_for_append]
        Task<DriveContents> openTask =
                getDriveResourceClient().openFile(file, DriveFile.MODE_READ_WRITE);
        // [END drive_android_open_for_append]
        // [START drive_android_append_contents]
        openTask.continueWithTask(task -> {
            DriveContents driveContents = task.getResult();
            ParcelFileDescriptor pfd = driveContents.getParcelFileDescriptor();
            long bytesToSkip = pfd.getStatSize();
            try (InputStream in = new FileInputStream(pfd.getFileDescriptor())) {
                // Skip to end of file
                while (bytesToSkip > 0) {
                    long skipped = in.skip(bytesToSkip);
                    bytesToSkip -= skipped;
                }
            }
            try (OutputStream out = new FileOutputStream(pfd.getFileDescriptor())) {
                out.write(contentToAppend.getBytes());
            }
            // [START drive_android_commit_contents_with_metadata]
            MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                    .setStarred(true)
                    .setLastViewedByMeDate(new Date())
                    .build();
            Task<Void> commitTask =
                    getDriveResourceClient().commitContents(driveContents, changeSet);
            // [END drive_android_commit_contents_with_metadata]
            return commitTask;
        })
                .addOnSuccessListener(this,
                        aVoid -> {
                            retrieveContents(driveFileToOpen);
//                            showMessage(getString(R.string.content_updated));
                            finish();
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to update contents", e);
//                    showMessage(getString(R.string.content_update_failed));
                    finish();
                });
        // [END drive_android_append_contents]
    }



    public void rewriteContents(DriveFile file, String contentToRewrite) {
        // [START drive_android_open_for_write]
        Task<DriveContents> openTask =
                getDriveResourceClient().openFile(file, DriveFile.MODE_WRITE_ONLY);
        // [END drive_android_open_for_write]
        // [START drive_android_rewrite_contents]
        openTask.continueWithTask(task -> {
            DriveContents driveContents = task.getResult();
            try (OutputStream out = driveContents.getOutputStream()) {
                out.write(contentToRewrite.getBytes());
            }
            // [START drive_android_commit_content]
            Task<Void> commitTask =
                    getDriveResourceClient().commitContents(driveContents, null);
            // [END drive_android_commit_content]
            return commitTask;
        })
                .addOnSuccessListener(this,
                        aVoid -> {
//                            showMessage(getString(R.string.content_updated));
                            finish();
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to update contents", e);
//                    showMessage(getString(R.string.content_update_failed));
                    finish();
                });
        // [END drive_android_rewrite_contents]
    }


    public static ArrayList<String> contentFromGoogleFile= new ArrayList<String>();

    public void retrieveContents(DriveFile file) throws NullPointerException {
        contentFromGoogleFile.clear();
        try {
            // [START drive_android_open_file]
            Task<DriveContents> openFileTask =
                    getDriveResourceClient().openFile(file, DriveFile.MODE_READ_ONLY);
            // [END drive_android_open_file]
            // [START drive_android_read_contents]
            openFileTask
                    .continueWithTask(task -> {
                        DriveContents contents = task.getResult();
                        // Process contents...
                        // [START_EXCLUDE]
                        // [START drive_android_read_as_string]

                        try (BufferedReader reader = new BufferedReader(
                                new InputStreamReader(contents.getInputStream()))) {
                            StringBuilder builder = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                //builder.append(line).append("\n");
                                if(!line.equals("")){
                                    contentFromGoogleFile.add(line);
                                }

                            }
//                            showMessage(getString(R.string.content_loaded));
//                            finish();
                        }
                        // [END drive_android_read_as_string]
                        // [END_EXCLUDE]
                        // [START drive_android_discard_contents]

                        Task<Void> discardTask = getDriveResourceClient().discardContents(contents);
                        // [END drive_android_discard_contents]
                        return discardTask;
                    })
                    .addOnCompleteListener(e -> {
                        // Handle failure
                        // [START_EXCLUDE]
                        completeLoadingData=true;
                        PlannerFragment.activityList.clear();
                        for(int i=0; i<GoogleDriveOperation.contentFromGoogleFile.size();i++){
                            PlannerFragment.activityList.add(GoogleDriveOperation.contentFromGoogleFile.get(i).split("&!&#&"));
                        }
                        LoginActivity.loadingBar.setVisibility(View.INVISIBLE);
                        if(MainActivity.importData==true) {
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            MainActivity.importData=false;
                        }else{
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                        }
//                        showMessage(getString(R.string.read_failed));
                        // [END_EXCLUDE]
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        // [START_EXCLUDE]
                        Log.e(TAG, "Unable to read contents", e);
//                        showMessage(getString(R.string.read_failed));
                        finish();
                        // [END_EXCLUDE]
                    });
            // [END drive_android_read_contents]
        }catch(NullPointerException e){
            throw new NullPointerException(e.toString());
        }
    }



    public String getUsername(Context context) {
        signInAccount = GoogleSignIn.getLastSignedInAccount(context);
        return signInAccount.getEmail();

    }



    public void readFile(Context context){
        ReadFileLocal readFileLocal = new ReadFileLocal();
        String fileData=readFileLocal.readFile(context);
        String [] recordsFromFile;
        recordsFromFile = fileData.split("///");
        String [] [] usersAndIDFromFile = new String[recordsFromFile.length][];
        for(int i=0;i<recordsFromFile.length;i++){
            usersAndIDFromFile[i]=recordsFromFile[i].split("\\|");
        }

        pathToDataFile="";
        for(int i=0; i<recordsFromFile.length;i++){
            if(usersAndIDFromFile[i][0].equals(getUsername(context))){
                pathToDataFile=usersAndIDFromFile[i][1];
            }
        }
        decodePathToGoogleFile();
    }




    private void decodePathToGoogleFile(){
        try {
            driveFileToOpen = decodeFromString(pathToDataFile).asDriveFile();
        }catch(Exception e){
            driveFileToOpen=null;
        }
    }

    public void signOutGoogleAccount(){
        signOut();
        revokeAccess();
        signIn();
    }



    public void openFileExplorerGoogleDrive(Context context) {
        pickTextFile()
                .addOnSuccessListener(this,
                        driveId -> {
                            PlannerFragment.activityList.clear();
                            pathToDataFile=driveId.encodeToString();
                            createNewGoogleFileAndAppendID(context);
                            decodePathToGoogleFile();
                            retrieveContents(GoogleDriveOperation.driveFileToOpen);
                        })
                .addOnFailureListener(this, e -> {
                    if(MainActivity.importData==false) {
                        Log.e(TAG, "No file selected", e);
                        showMessage(getString(R.string.file_not_selected));
                        Intent intent = new Intent(this, LoginActivity.class);
                        startActivity(intent);
                        MainActivity.importData=false;
                    }else {
                        MainActivity.importData=false;
                    }
                });
        completeLoadingData=false;
    }
}
