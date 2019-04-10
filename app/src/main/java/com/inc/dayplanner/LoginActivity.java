package com.inc.dayplanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.OpenFileActivityOptions;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.android.gms.drive.widget.DataBufferAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


import static com.google.android.gms.drive.DriveId.decodeFromString;


public class LoginActivity extends BaseDemoActivity{


    private TextView mFileContents;
    private DriveFile driveFileToOpen;
    String pathToDataFile=null;


    //private static final String TAG = "Google Drive Activity";
    @Override
    protected void onDriveClientReady() {
        //listFiles();
//        createFile();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFileContents = findViewById(R.id.mFileContents);
        mFileContents.setText("");
    }

    public void Login(View view) {
            SaveFileLocal saveFileLocal = new SaveFileLocal();
//            saveFileLocal.saveFile(getApplicationContext(),getUsername(), pathToDataFile);
            readFile();
            rewriteContents(driveFileToOpen);
            retrieveContents(driveFileToOpen);
//        String filename = "DayPlannerIDdataFile";
//        File directory = Environment.getFilesDir();
//        File file = new File(directory, filename);
//        StringBuilder text = new StringBuilder();
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(file));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                text.append(line);
//                text.append('\n');
//            }
//            br.close();
//        }
//        catch (IOException e) {
            //You'll need to add proper error handling here
        //}

//        createFile();

//        driveFileToOpen= decodeFromString(pathToDataFile).asDriveFile();
//        retrieveContents(driveFileToRead);

        //Intent intent = new Intent(this, MainActivity.class);
        //startActivity(intent);
    }
    /////////////////////////////////////////////////////////////////////////////////////////////
//    /**
//     * Request code for file creator activity.
//     */
//    private static final int REQUEST_CODE_CREATOR = NEXT_AVAILABLE_REQUEST_CODE;
//
//    /**
//     * Request code for the file opener activity.
//     */
//    private static final int REQUEST_CODE_OPENER = NEXT_AVAILABLE_REQUEST_CODE + 1;
//
//    /**
//     * Text file MIME type.
//     */
//    private static final String MIME_TYPE_TEXT = "text/plain";
//
//    /**
//     * Drive ID of the currently opened Drive file.
//     */
//    private DriveId mCurrentDriveId;
//
//    /**
//     * Currently opened file's metadata.
//     */
//    private Metadata mMetadata;
//
//    /**
//     * Currently opened file's contents.
//     */
//    private DriveContents mDriveContents;

    private static final String TAG = "LoginActivity";
//    private void openDriveFile() {
//        Log.i(TAG, "Open Drive file.");
//
//        if (!isSignedIn()) {
//            Log.w(TAG, "Failed to open file, user is not signed in.");
//            return;
//        }
//
//        // Build activity options.
//        final OpenFileActivityOptions openFileActivityOptions =
//                new OpenFileActivityOptions.Builder()
//                        .setMimeType(Collections.singletonList(MIME_TYPE_TEXT))
//                        .build();
//
//        // Start a OpenFileActivityIntent
//        mDriveClient.newOpenFileActivityIntentSender(openFileActivityOptions)
//                .addOnSuccessListener(new OnSuccessListener<IntentSender>() {
//                    @Override
//                    public void onSuccess(IntentSender intentSender) {
//                        try {
//                            startIntentSenderForResult(
//                                    intentSender,
//                                    REQUEST_CODE_OPENER,
//                                    /* fillInIntent= */ null,
//                                    /* flagsMask= */ 0,
//                                    /* flagsValues= */ 0,
//                                    /* extraFlags= */ 0);
//                        } catch (IntentSender.SendIntentException e) {
//                            Log.w(TAG, "Unable to send intent.", e);
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e(TAG, "Unable to create OpenFileActivityIntent.", e);
//            }
//        });
//    }

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
                        writer.write("MAMY TOO!!!");
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
                        })
                .addOnFailureListener(this, e -> {
                    Log.e(TAG, "Unable to create file", e);
                    showMessage(getString(R.string.file_create_error));
                });
        // [END drive_android_create_file]
    }

    private void appendContents(DriveFile file) {
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
                out.write("Hello world".getBytes());
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

    private void rewriteContents(DriveFile file) {
        // [START drive_android_open_for_write]
        Task<DriveContents> openTask =
                getDriveResourceClient().openFile(file, DriveFile.MODE_WRITE_ONLY);
        // [END drive_android_open_for_write]
        // [START drive_android_rewrite_contents]
        openTask.continueWithTask(task -> {
            DriveContents driveContents = task.getResult();
            try (OutputStream out = driveContents.getOutputStream()) {
                out.write("Hello world".getBytes());
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

    private void retrieveContents(DriveFile file) {

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
    }

    public String getUsername() {
        AccountManager manager = AccountManager.get(this);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type
            // values.
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

//    private DataBufferAdapter<Metadata> mResultsAdapter;
//    private void listFiles() {
//        // [START drive_android_query_title]
//        Query query = new Query.Builder()
//                .addFilter(Filters.eq(SearchableField.TITLE, "DayPlanner.txt"))
//                .build();
//        // [END drive_android_query_title]
//        Task<MetadataBuffer> queryTask =
//                getDriveResourceClient()
//                        .query(query)
//                        .addOnSuccessListener(this,
//                                metadataBuffer -> mResultsAdapter.append(metadataBuffer))
//                        .addOnFailureListener(this, e -> {
//                            Log.e(TAG, "Error retrieving files", e);
//                            showMessage(getString(R.string.query_failed));
//                            finish();
//                        });
//    }



    public void readFile(){
        SaveFileLocal saveFileLocal = new SaveFileLocal();
        String fileData=saveFileLocal.readFile(getApplicationContext());
        String [] dataFromFile;
        dataFromFile=fileData.split("\\|");
        pathToDataFile=dataFromFile[1];
        driveFileToOpen= decodeFromString(pathToDataFile).asDriveFile();
        retrieveContents(driveFileToOpen);
    }

}



