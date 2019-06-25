package com.inc.dayplanner.GoogleDriveApi;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;


public class SaveFileLocal extends AppCompatActivity {

    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE";

    public void saveIDFile(Context ctx, String username, String pathToDataFile){
        String filename = "DayPlannerIDdataFile";
        String fileData;

        String content =username+"|"+pathToDataFile;

            fileData=content;

        saveToFile(ctx,filename,fileData);


        //Toast.makeText(ctx, "Data has been written to file " + userEmalFileName, Toast.LENGTH_LONG).show();
    }

    public void saveCheckCancelledActivities(Context ctx, String fileData){
        String filename = "DayPlannerCheckCancelledAct";
        saveToFile(ctx,filename,fileData);
    }

    private void saveToFile(Context ctx,String filename, String fileData){
        try
        {
            FileOutputStream fileOutputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            writeDataToFile(fileOutputStream, fileData);
        }catch(FileNotFoundException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }
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
