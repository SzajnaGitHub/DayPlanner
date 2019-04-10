package com.inc.dayplanner;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;


public class SaveFileLocal extends AppCompatActivity {

    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE";

    public void saveFile(Context ctx,String username,String pathToDataFile){
        String filename = "DayPlannerIDdataFile";

        String content =username+"|"+pathToDataFile;

        try
        {
            FileOutputStream fileOutputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            writeDataToFile(fileOutputStream, content);
        }catch(FileNotFoundException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }

        //Toast.makeText(ctx, "Data has been written to file " + userEmalFileName, Toast.LENGTH_LONG).show();
    }






    private void writeDataToFile(FileOutputStream fileOutputStream, String data)
    {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.write(data);

            bufferedWriter.flush();
            bufferedWriter.close();
            outputStreamWriter.close();
        }catch(FileNotFoundException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }catch(IOException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }
    }



}
