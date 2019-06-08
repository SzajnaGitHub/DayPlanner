package com.inc.dayplanner;


import android.annotation.SuppressLint;
import android.media.AudioManager;

import com.inc.dayplanner.Fragments.PlannerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class CheckMuteThread implements Runnable{

    private Calendar calendar = Calendar.getInstance();
    private int realHour;
    private int realMinute;
    private boolean appMutedPhone=false;


    private void unMutePhone(AudioManager audioManager) {
        audioManager=PlannerFragment.audioManager;
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }



    @Override
    public void run() {
        int[] timeToUnmute = new int[2];
        int[] timeFromInt = new int[2];
        int[] timeToInt = new int[2];
        timeToUnmute[0]=-1;
        timeToUnmute[1]=-1;
        while (true) {
            List<String[]> activitiesThreadList = PlannerFragment.activityList;
            @SuppressLint("SimpleDateFormat") String df = new SimpleDateFormat("d MMM yyyy").format(calendar.getTime());
            if(appMutedPhone) {
                if (timeToUnmute[0] != -1 || timeToUnmute[1] != -1) {
                    if (timeToUnmute[0] >= realHour && timeToUnmute[1] >= realMinute) {
                        timeToUnmute[0] = -1;
                        timeToUnmute[1] = -1;
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

                    if (activitiesThreadList.get(i)[4].equals("true")&& activitiesThreadList.get(i)[0].equals(df)) {
                        String timeFrom = activitiesThreadList.get(i)[1];
                        String timeTo = activitiesThreadList.get(i)[2];
                        String[] timeFromTab = timeFrom.split(":");
                        String[] timeToTab = timeTo.split(":");
                        try {
                            timeFromInt[0] = Integer.valueOf(timeFromTab[0].trim());
                            timeFromInt[1] = Integer.valueOf(timeFromTab[1].trim());
                            timeToInt[0] = Integer.valueOf(timeToTab[0].trim());
                            timeToInt[1] = Integer.valueOf(timeToTab[1].trim());
                        } catch (Exception e) {
                            continue;
                        }


                        if (timeFromInt[0] <= realHour && timeToInt[0] >= realHour) {
                            if (timeFromInt[1] <= realMinute && timeToInt[1] >= realMinute) {
                                appMutedPhone = true;
                                timeToUnmute = timeToInt;
                                AudioManager audioManager = PlannerFragment.audioManager;
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

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}
