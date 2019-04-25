package com.inc.dayplanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.os.ParcelFileDescriptor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.drive.DriveContents;
import android.widget.TextView;

import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import static com.google.android.gms.drive.DriveId.decodeFromString;


public class LoginActivity extends BaseDemoActivity{


    private TextView mFileContents;
    private DriveFile driveFileToOpen;
    static String pathToDataFile=null;
    private static final String TAG = "LoginActivity";


    //private static final String TAG = "Google Drive Activity";
    @Override
    protected void onDriveClientReady() {
//        createFile();
//        createNewGoogleFileAndAppendID();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFileContents = findViewById(R.id.mFileContents);
        mFileContents.setText("");
    }

    public void Login(View view) {
        createFile();
//        createNewGoogleFileAndAppendID();
        readFile();
        if(driveFileToOpen==null){
            //createNewGoogleFileAndAppendID();
        }else {
            try {
                retrieveContents(driveFileToOpen);
            } catch (Exception e) {
                //TODO:zapytac uzytkownika czy chce zaimportowac dane
                createNewGoogleFileAndAppendID();
            }
        }

        ReadFileLocal readFileLocal = new ReadFileLocal();
        String fileData=readFileLocal.readFile(getApplicationContext());
        System.out.println(fileData);
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////

    public void createNewGoogleFileAndAppendID(){
        SaveFileLocal saveFileLocal = new SaveFileLocal();
//        createFile();
//        fileData +="|"+pathToDataFile;
        saveFileLocal.saveFile(getApplicationContext(),getUsername(), pathToDataFile);
//        System.out.println("Blad");
    }


    private void createFile() {
        // [START drive_android_create_file]
        final Task<DriveFolder> rootFolderTask = getDriveResourceClient().getRootFolder();
        final Task<DriveContents> createContentsTask = getDriveResourceClient().createContents();
        Tasks.whenAll(rootFolderTask, createContentsTask)
                .continueWithTask(task -> {
                    DriveFolder parent = rootFolderTask.getResult();
                    DriveContents contents = createContentsTask.getResult();
                    OutputStream outputStream = contents.getOutputStream();
                    try (Writer writer = new OutputStreamWriter(outputStream)) {
                        writer.write(signInAccount.getEmail());
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
                            showMessage(getString(R.string.file_created,
                                    driveFile.getDriveId().encodeToString()));
                            pathToDataFile=driveFile.getDriveId().encodeToString();
                            createNewGoogleFileAndAppendID();
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to create file", e);
                    showMessage(getString(R.string.file_create_error));
                });
        // [END drive_android_create_file]
    }



    private void appendContents(DriveFile file, String contentToAppend) {
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
                            showMessage(getString(R.string.content_updated));
//                            finish();
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to update contents", e);
                    showMessage(getString(R.string.content_update_failed));
//                    finish();
                });
        // [END drive_android_append_contents]
    }



    private void rewriteContents(DriveFile file, String contentToRewrite) {
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
                            showMessage(getString(R.string.content_updated));
//                            finish();
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to update contents", e);
                    showMessage(getString(R.string.content_update_failed));
//                    finish();
                });
        // [END drive_android_rewrite_contents]
    }



    private void retrieveContents(DriveFile file) throws NullPointerException {
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
                                builder.append(line).append("\n");
                            }
                            showMessage(getString(R.string.content_loaded));
                            mFileContents.setText(builder.toString());
                        }
                        // [END drive_android_read_as_string]
                        // [END_EXCLUDE]
                        // [START drive_android_discard_contents]
                        Task<Void> discardTask = getDriveResourceClient().discardContents(contents);
                        // [END drive_android_discard_contents]
                        return discardTask;
                    })
                    .addOnFailureListener(e -> {
                        // Handle failure
                        // [START_EXCLUDE]
                        Log.e(TAG, "Unable to read contents", e);
                        showMessage(getString(R.string.read_failed));
                        // [END_EXCLUDE]
                    });
            // [END drive_android_read_contents]
        }catch(NullPointerException e){
            throw new NullPointerException(e.toString());
        }
            }



    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if (parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return null;
        } else
            return null;
    }



    public void readFile(){
        ReadFileLocal readFileLocal = new ReadFileLocal();
        String fileData=readFileLocal.readFile(getApplicationContext());
        String [] recordsFromFile;
        recordsFromFile = fileData.split("///");
        String [] [] usersAndIDFromFile = new String[recordsFromFile.length][];
        for(int i=0;i<recordsFromFile.length;i++){
            usersAndIDFromFile[i]=recordsFromFile[i].split("\\|");
        }

        pathToDataFile="";
        for(int i=0; i<recordsFromFile.length;i++){
            if(usersAndIDFromFile[i][0].equals(getUsername())){
                pathToDataFile=usersAndIDFromFile[i][1];
            }
        }
        if(pathToDataFile.equals("")){
//            createNewGoogleFileAndAppendID();
        }
        //TODO:sprawdzic czy w pliku jest tylko jedno ID
        try {
            driveFileToOpen = decodeFromString(pathToDataFile).asDriveFile();
        }catch(Exception e){
            driveFileToOpen=null;
        }
        //retrieveContents(driveFileToOpen);
    }

    public void signOutGoogleAccount(View view){
        signOut();
        revokeAccess();
        signIn();
    }

}



