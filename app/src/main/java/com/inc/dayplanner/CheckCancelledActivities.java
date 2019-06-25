package com.inc.dayplanner;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.widget.Toast;

import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.GoogleDriveApi.ReadFileLocal;
import com.inc.dayplanner.GoogleDriveApi.SaveFileLocal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckCancelledActivities extends IntentService {

    List<String[]> arrayLocal;
    String[] arrayRead;
    ReadFileLocal readFileLocal;
    int length;
    GoogleDriveOperation googleDriveOperation = new GoogleDriveOperation();
    public static String nameActivityCancelled;

    public CheckCancelledActivities() {
        super("Cancelled Activities");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        doBackgroundWork();
    }

    private void doBackgroundWork(){
        new Thread(() -> {
            Looper.prepare();
            while (true){
                System.out.println("TEST ANULACJI");

                googleDriveOperation.readFile(getApplicationContext());
                if(GoogleDriveOperation.driveFileToOpen!=null){
                    System.out.println("drive nie jest null");
                    googleDriveOperation.retrieveContentsToCheckCancelledActivities(GoogleDriveOperation.driveFileToOpen, this);
                }

                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void saveFromArrayListToFile(List<String[]> arrayGoogle){

        arrayLocal = new ArrayList<>();
//        saveFromArrayListToFile(arrayGoogle);
        readFileLocal = new ReadFileLocal();
        arrayRead = readFileLocal.readFile(getApplicationContext(),"DayPlannerCheckCancelledAct").split("#endActivityLine#");
        for(int i=0; i<arrayRead.length;i++){
            if(arrayRead[i].equals("")){
                String[] fakeArray={"x","x","x","x","x","x","x","BrAkAkTyWnOOsci","x","x","x"};
                arrayLocal.add(fakeArray);
            }else{
                arrayLocal.add(arrayRead[i].split("&!&#&"));
            }

            System.out.println(arrayLocal.get(i)[0]);
        }



        if(arrayGoogle.size()>arrayLocal.size()){
            length=arrayLocal.size();
        }else{
            length=arrayGoogle.size();
        }
        for(int i=0;i<length;i++){
//            System.out.println("Google: "+arrayGoogle.get(i)[7]);
//            System.out.println("Local: "+arrayLocal.get(i)[7]);
            if(!arrayGoogle.get(i)[7].equals(arrayLocal.get(i)[7]) && !arrayLocal.equals("BrAkAkTyWnOOsci")){
                System.out.println("ANULOWANIE AKTYWNOSCI");
                Toast toast = Toast.makeText(getApplicationContext(),"Anulacja aktywnosci "+arrayLocal.get(i)[3],Toast.LENGTH_LONG);
                toast.show();

                //-----------------------------NOTIFICATION-----------------------------
                nameActivityCancelled=arrayLocal.get(i)[3];
                Calendar c = Calendar.getInstance();
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlertReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

                alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

                //---------------------------END NOTIFICATION---------------------------

                googleDriveOperation.retrieveContents(GoogleDriveOperation.driveFileToOpen);

            }
        }
        String textToSave;//=dateString+"&!&#&"+fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"&!&#&"+dateRemainder+"\n";
        textToSave="";
        for(int i=0;i<arrayGoogle.size();i++){
            textToSave+=arrayGoogle.get(i)[0]+"&!&#&"+arrayGoogle.get(i)[1]+"&!&#&"+arrayGoogle.get(i)[2]+"&!&#&"+arrayGoogle.get(i)[3]+"&!&#&"+arrayGoogle.get(i)[4]+"&!&#&"+arrayGoogle.get(i)[5]+"&!&#&"+arrayGoogle.get(i)[6]+"&!&#&"+arrayGoogle.get(i)[7]+"#endActivityLine#";
        }
        SaveFileLocal saveFileLocal = new SaveFileLocal();
        saveFileLocal.saveCheckCancelledActivities(getApplicationContext(),textToSave);
    }
}
