package com.inc.dayplanner;

import android.app.AlarmManager;
import android.media.AudioManager;

import com.inc.dayplanner.Fragments.PlannerFragment;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CheckMuteThread implements Runnable{

    private Calendar calendar = Calendar.getInstance();
    private String TimeFrom;
    private String[] TimeFromTab;
    private String TimeTo;
    private String [] TimeToTab;
    private List<String[]> activitiesThreadList = new ArrayList<>();
    private int [] TimeFromInt;
    private int [] TimeToInt;
    int realHour;
    int realMinute;
    private boolean appMutedPhone=false;
    private int [] TimeToUnmute;
    private static AudioManager audioManager;
    private String df;
    private String [] TimeToRemaindString;
    private int [] TimeToRemaindInt;
    private String[] timeToRemaind;
    private String realDate;



    private void unMutePhone(AudioManager audioManager) {
        audioManager=PlannerFragment.audioManager;
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }



    @Override
    public void run() {
        TimeToUnmute=new int[2];
        TimeFromInt=new int[2];
        TimeToInt=new int[2];
        TimeToUnmute[0]=-1;
        TimeToUnmute[1]=-1;
        while (true) {
            activitiesThreadList = PlannerFragment.activityList;
            df = new SimpleDateFormat("d MMM yyyy").format(calendar.getTime());
            if(appMutedPhone==true) {
                if (TimeToUnmute[0] != -1 || TimeToUnmute[1] != -1) {
                    if (TimeToUnmute[0] >= realHour && TimeToUnmute[1] >= realMinute) {
                        TimeToUnmute[0] = -1;
                        TimeToUnmute[1] = -1;
                        appMutedPhone = false;
                        //TODO:Unmute
                    }
                }
            }
            calendar = Calendar.getInstance();
            realHour = calendar.get(Calendar.HOUR_OF_DAY);
            realMinute = calendar.get(Calendar.MINUTE);
            if (!activitiesThreadList.isEmpty()) {
                for (int i = 0; i < activitiesThreadList.size(); i++) {

                    if (activitiesThreadList.get(i)[4].equals("true")&&activitiesThreadList.get(i)[0].equals(df)) {
                        TimeFrom = activitiesThreadList.get(i)[1];
                        TimeTo = activitiesThreadList.get(i)[2];
                        TimeFromTab = TimeFrom.split(":");
                        TimeToTab = TimeTo.split(":");
                        try {
                            TimeFromInt[0] = Integer.valueOf(TimeFromTab[0].trim());
                            TimeFromInt[1] = Integer.valueOf(TimeFromTab[1].trim());
                            TimeToInt[0] = Integer.valueOf(TimeToTab[0].trim());
                            TimeToInt[1] = Integer.valueOf(TimeToTab[1].trim());
                        } catch (Exception e) {
                            continue;
                        }


                        if (TimeFromInt[0] <= realHour && TimeToInt[0] >= realHour) {
                            if (TimeFromInt[1] <= realMinute && TimeToInt[1] >= realMinute) {
                                appMutedPhone = true;
                                TimeToUnmute = TimeToInt;
                                audioManager=PlannerFragment.audioManager;
                                if(audioManager.getRingerMode()!=AudioManager.RINGER_MODE_VIBRATE){
                                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                }
                            }
                        }

                    }
                }
            }

            try {
                Thread.sleep(30000);
                System.out.println("Watek idzie spac :-0");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
