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
//    private String TAG_WRITE_READ_FILE = "TAG_WRITE_READ_FILE";
//    private void saveFile(){
//        Context ctx = getApplicationContext();
//        String filename = "DayPlannerIDdataFile";
//        username=getUsername();
//        String content =username+"|"+pathToDataFile;
//
//        try
//        {
//            FileOutputStream fileOutputStream = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
//            writeDataToFile(fileOutputStream, content);
//        }catch(FileNotFoundException ex)
//        {
//            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
//        }
//
//        //Toast.makeText(ctx, "Data has been written to file " + userEmalFileName, Toast.LENGTH_LONG).show();
//    }
//
//
//    private String readFromFileInputStream(FileInputStream fileInputStream)
//    {
//        StringBuffer retBuf = new StringBuffer();
//
//        try {
//            if (fileInputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//
//                String lineData = bufferedReader.readLine();
//                while (lineData != null) {
//                    retBuf.append(lineData);
//                    lineData = bufferedReader.readLine();
//                }
//            }
//        }catch(IOException ex)
//        {
//            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
//        }finally
//        {
//            return retBuf.toString();
//        }
//    }
//
//    private void writeDataToFile(FileOutputStream fileOutputStream, String data)
//    {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
//            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
//
//            bufferedWriter.write(data);
//
//            bufferedWriter.flush();
//            bufferedWriter.close();
//            outputStreamWriter.close();
//        }catch(FileNotFoundException ex)
//        {
//            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
//        }catch(IOException ex)
//        {
//            Log.e(TAG_WRITE_READ_FILE, ex.getMessage(), ex);
//        }
//    }
//
//    public String getUsername() {
//        AccountManager manager = AccountManager.get(this);
//        Account[] accounts = manager.getAccountsByType("com.google");
//        List<String> possibleEmails = new LinkedList<String>();
//
//        for (Account account : accounts) {
//            // TODO: Check possibleEmail against an email regex or treat
//            // account.name as an email address only for certain account.type
//            // values.
//            possibleEmails.add(account.name);
//        }
//
//        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
//            String email = possibleEmails.get(0);
//            String[] parts = email.split("@");
//            if (parts.length > 0 && parts[0] != null)
//                return parts[0];
//            else
//                return null;
//        } else
//            return null;
//    }



}
