package com.inc.dayplanner;

import android.content.Intent;
import android.content.IntentSender;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "Google Drive Activity";
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    public void Login(View view) {

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                    .addOnConnectionFailedListener((GoogleApiClient.OnConnectionFailedListener) this)
                    .build();
        }
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }
    public void onClickLogout(View view) {
        mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback((new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                Toast.makeText(getApplicationContext(), "Wylogowano", Toast.LENGTH_LONG).show();
            }
        }));
    }

    private static final int REQUEST_CODE_RESOLUTION = 1;

    @Override
    public void onConnected(Bundle connectionHint) {
        Toast.makeText(getApplicationContext(), "Zalogowano ", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient połączenie nieudane: " + result.toString());
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient połączenie wstrzymane.");
    }


    public void onClickCreateFile(View view) {
        fileOperation = true;
        // Create new contents resource.
        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                .setResultCallback(driveContentsCallback);
    }
    }
}
