package com.inc.dayplanner.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import com.inc.dayplanner.DynamicViews;
import com.inc.dayplanner.R;
import java.text.SimpleDateFormat;
import java.util.Date;
import static android.view.View.INVISIBLE;


public class PlannerFragment extends Fragment {

    private GridLayout gridLayout;
    private DynamicViews dynamicViews;
    private TextView dayTextView;
    private Context context;
    private FrameLayout messageFrame;
    private TextView fromText;
    private TextView toText;
    private TextView activityText;
    private boolean flag=false;
    private CheckBox muteCheckbox;
    private Date now = new Date();
    private CheckBox remindCheckbox;
    private Spinner remindSpinner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);
        dayTextView = view.findViewById(R.id.dayText);
        messageFrame = view.findViewById(R.id.messageFrame);
        SimpleDateFormat simpleDateformat = new SimpleDateFormat("EEEE");


        String message = null;

        if (getArguments() != null) {
            message = getArguments().getString("dayOfTheWeek");
            dayTextView.setText(message);
        }else {
            dayTextView.setText(simpleDateformat.format(now));
        }


        final AudioManager audioManager = (AudioManager)getContext().getSystemService(getContext().AUDIO_SERVICE);


        fromText = view.findViewById(R.id.fromText);
        toText = view.findViewById(R.id.toText);
        activityText = view.findViewById(R.id.activityText);
        muteCheckbox = view.findViewById(R.id.muteCheckBox);

        final ImageButton addButton = view.findViewById(R.id.addButton2);
        addButton.setOnClickListener(v -> {
            gridLayout.addView(dynamicViews.linearLayout(getContext(),
                    fromText.getText()+"-"+toText.getText(), ""+activityText.getText()));

            if(muteCheckbox.isChecked())  {
                //ADD BUTTON METHODS

                audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);


            }});


        final ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {
            dynamicViews = new DynamicViews(context);

            if(!flag) {
                messageFrame.setVisibility(View.VISIBLE);
                flag = true;
            }else{
                flag = false;
                messageFrame.setVisibility(INVISIBLE);

            }

        });


        remindSpinner = view.findViewById(R.id.reminderSpinner);




        remindCheckbox = view.findViewById(R.id.remindCheckBox);


        return view;
    }


    private void unMutePhone(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }





}
