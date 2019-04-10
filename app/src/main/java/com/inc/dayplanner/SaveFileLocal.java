package com.inc.dayplanner;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class SaveFileLocal extends AppCompatActivity {

    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE";

    public void saveFile(String content){
        String filename = "DayPlannerIDdataFile";
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(content.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readFile(){
        String filename = "DayPlannerIDdataFile";
        Context ctx = getApplicationContext();

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = ctx.openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String fileData = readFromFileInputStream(fileInputStream);

        if(fileData.length()>0) {
            Toast.makeText(ctx, "Load saved data complete.", Toast.LENGTH_SHORT).show();
        }
    }

    private String readFromFileInputStream(FileInputStream fileInputStream)
    {
        StringBuffer retBuf = new StringBuffer();

        try {
            if (fileInputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String lineData = bufferedReader.readLine();
                while (lineData != null) {
                    retBuf.append(lineData);
                    lineData = bufferedReader.readLine();
                }
            }
        }catch(IOException ex)
        {
            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
        }finally
        {
            return retBuf.toString();
        }
    }



}
