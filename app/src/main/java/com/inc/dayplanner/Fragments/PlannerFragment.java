package com.inc.dayplanner.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.inc.dayplanner.Activities.LoginActivity;
import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.DynamicViews;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.SwipeAdapter;



import static android.view.View.INVISIBLE;


public class PlannerFragment extends Fragment {


    private GridLayout gridLayout;
    private DynamicViews dynamicViews;
    private TextView dayTextView;
    private Context context;
    private FrameLayout messageFrame;
    private TextView activityText;
    private CheckBox muteCheckbox;
    private CheckBox remindCheckbox;
    private Spinner remindSpinner;
    private TextView fromHourPickerTextView;
    private TextView toHourPickerTextView;
    private boolean frameVisibility = false;
    private String hour1;
    private String hour2;
    public static GoogleDriveOperation saveToGoogleDrive = new GoogleDriveOperation();
    private AudioManager audioManager;



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        read();
    }

    @Override
    public void onStart() {
        super.onStart();
        read();
    }

    @Override
    public void onResume() {
        super.onResume();
//        read();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //saveToGoogleDrive.retrieveContents(GoogleDriveOperation.driveFileToOpen);
//        read();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);
        dayTextView = view.findViewById(R.id.dayText);
        messageFrame = view.findViewById(R.id.messageFrame);
        activityText = view.findViewById(R.id.activityText);
        muteCheckbox = view.findViewById(R.id.muteCheckBox);
        remindSpinner = view.findViewById(R.id.reminderSpinner);
        remindCheckbox = view.findViewById(R.id.remindCheckBox);
        fromHourPickerTextView = view.findViewById(R.id.fromHourPicker);
        toHourPickerTextView = view.findViewById(R.id.toHourPicker);
        final ImageButton addButton = view.findViewById(R.id.addButton2);
        audioManager = (AudioManager)getContext().getSystemService(getContext().AUDIO_SERVICE);



        String message = null;
        if (getArguments() != null) {
           // message = getArguments().getString("dayOfTheWeek");
            message = getArguments().getString("Date");
            dayTextView.setText(message);
        }else {
            dayTextView.setText(SwipeAdapter.setDay(1));
        }



        toHourPickerTextView.setOnClickListener(v -> {

            TimePickerFragment timePickerFragment = new TimePickerFragment(toHourPickerTextView);
            assert getFragmentManager() != null;
            timePickerFragment.show(getFragmentManager(),"timePicker");
        });



        fromHourPickerTextView.setOnClickListener(v -> {

            TimePickerFragment timePickerFragment = new TimePickerFragment(fromHourPickerTextView);
            assert getFragmentManager() != null;
            timePickerFragment.show(getFragmentManager(),"timePicker");
        });




        addButton.setOnClickListener(v -> {
            hour2 = toHourPickerTextView.getText().toString();
            hour1 = fromHourPickerTextView.getText().toString();
            if(activityText.getText().length()>20){
                activityText.setText(activityText.getText().toString().substring(0,19)+"...");
            }

            if(muteCheckbox.isChecked())  {
                //ADD BUTTON METHODS
                if(audioManager.getRingerMode()!=AudioManager.RINGER_MODE_VIBRATE){
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
                 }

            }
            String mute;
            if(muteCheckbox.isChecked()){
                mute="true";
            }else{
                mute="false";
            }
            addToListActivity(hour1,hour2,activityText.getText().toString(),mute);
            save(hour1,hour2,activityText.getText().toString(),muteCheckbox);
        });






        final ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {


            if(!frameVisibility) {
                messageFrame.setVisibility(View.VISIBLE);
                frameVisibility = true;
            }else{
                frameVisibility = false;
                messageFrame.setVisibility(INVISIBLE);

            }

        });




        remindSpinner = view.findViewById(R.id.reminderSpinner);


        remindCheckbox = view.findViewById(R.id.remindCheckBox);

        return view;
    }

    private void addToListActivity(String from, String to, String activ, String mute){
        dynamicViews = new DynamicViews(context);

        gridLayout.addView(dynamicViews.linearLayout(getContext(),
                from+"-"+to, ""+activ));


        if(mute.equals("true"))  {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        }
    }


    private void save(String fromText,String toText, String activityText, CheckBox muteCheckbox){
        String mute;
        if(muteCheckbox.isChecked()){
            mute="true";
        }else{
            mute="false";
        }
        String textToSave=fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"\n";
        saveToGoogleDrive.appendContents(GoogleDriveOperation.driveFileToOpen,textToSave);
    }

    public void read(){
        int numberOfActivity=GoogleDriveOperation.contentFromGoogleFile.size();
        String[][]activityToAdd = new String[numberOfActivity][];
        gridLayout.removeAllViews();
        for(int i=0; i<numberOfActivity;i++){
            activityToAdd[i]=GoogleDriveOperation.contentFromGoogleFile.get(i).split("&!&#&");
            addToListActivity(activityToAdd[i][0],activityToAdd[i][1],activityToAdd[i][2],activityToAdd[i][3]);
        }
    }



    private void unMutePhone(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }



}
