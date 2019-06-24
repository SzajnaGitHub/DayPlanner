package com.inc.dayplanner;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.media.AudioManager;

import com.inc.dayplanner.Fragments.PlannerFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MuteJobScheduler extends IntentService {


    public MuteJobScheduler() {
        super("Check mute");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        doBackgroundWork();
    }

    private void doBackgroundWork(){
        new Thread(() -> {
            Calendar calendar = Calendar.getInstance();
            int realHour;
            int realMinute;

            while (true) {
                List<String[]> activitiesThreadList = PlannerFragment.activityList;
                @SuppressLint("SimpleDateFormat") String df = new SimpleDateFormat("d MMM yyyy").format(calendar.getTime());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

                calendar = Calendar.getInstance();
                realHour = calendar.get(Calendar.HOUR_OF_DAY);
                realMinute = calendar.get(Calendar.MINUTE);
                Date dateReal=new Date();
                Date dateFrom=new Date();
                Date dateTo=new Date();

//                    if(appMutedPhone) {
//                        if (timeToUnmute[0] != -1 || timeToUnmute[1] != -1) {
//                            if (timeToUnmute[0] >= realHour && timeToUnmute[1] >= realMinute) {
//                                timeToUnmute[0] = -1;
//                                timeToUnmute[1] = -1;
//                                appMutedPhone = false;
//                                //TODO:Unmute
//                            }
//                        }
//                    }

                if (!activitiesThreadList.isEmpty()) {
                    for (int i = 0; i < activitiesThreadList.size(); i++) {

                        if (activitiesThreadList.get(i)[4].equals("true")&& activitiesThreadList.get(i)[0].equals(df)) {
                            try {
                                dateReal = simpleDateFormat.parse(realHour+":"+realMinute);
                                dateFrom  = simpleDateFormat.parse(activitiesThreadList.get(i)[1]);
                                dateTo = simpleDateFormat.parse(activitiesThreadList.get(i)[2]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (dateFrom.compareTo(dateReal)<=0 && dateTo.compareTo(dateReal)>0) {
//                                        appMutedPhone = true;
//                                        timeToUnmute = timeToInt;
                                    AudioManager audioManager = PlannerFragment.audioManager;
                                    if(audioManager.getRingerMode()!=AudioManager.RINGER_MODE_VIBRATE){
                                        audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                                    }
                            }
                        }
                    }
                }
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
