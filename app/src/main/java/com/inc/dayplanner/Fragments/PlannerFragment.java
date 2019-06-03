package com.inc.dayplanner.Fragments;

import android.content.Context;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.inc.dayplanner.ViewChange.DynamicViews;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.resolveSize;


public class PlannerFragment extends Fragment  implements PopupFragment.ActivityHandlerListener {


    private static GridLayout gridLayout;
    private DynamicViews dynamicViews;
    public TextView dayTextView;
    private static Context context;
    private static Context prevContext;
    private static Context contextToAddElement;
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
    private static AudioManager audioManager;
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private Calendar calendar = Calendar.getInstance();
    public static List<String[]> activityList = new ArrayList<>();
    public static List<Context> contextList = new ArrayList<>();
    private static boolean ifAddedNewElement=false;
    private Button delButton;
    private List<DynamicViews> idList = new ArrayList<>();


    @Override
    public void onStart() {
        super.onStart();
        String date = df.format(calendar.getTime());
//        read(date);
        context=getContext();
    }

    @Override
    public void onAttach(Context mcontext) {
        super.onAttach(context);
        context = mcontext;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
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
        delButton = view.findViewById(R.id.deleteButton);
        contextList.add(context);


        String message = null;
        if (getArguments() != null) {
           // message = getArguments().getString("dayOfTheWeek");
            message = getArguments().getString("Date");
            dayTextView.setText(message);
            context=getContext();
            read(dayTextView.getText().toString());
        }else {
            dayTextView.setText(SwipeAdapter.setDay(1));
            context=getContext();
            String date = df.format(calendar.getTime());
            read(date);
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
//            String date = df.format(calendar.getTime());
            String date = dayTextView.getText().toString();
            hour2 = toHourPickerTextView.getText().toString();
            hour1 = fromHourPickerTextView.getText().toString();

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
            String[] addElement = {hour1,hour2,activityText.getText().toString(),mute};
            activityList.add(addElement);
            if(date.equals("Sunday")||date.equals("Monday")||date.equals("Tuesday")||date.equals("Wednesday")||date.equals("Thursday")||date.equals("Friday")||date.equals("Saturday")){
                date=df.format(calendar.getTime());
            }
            save(date,hour1,hour2,activityText.getText().toString(),mute);
//            sortAndAddToLayout(dayTextView.getText().toString());

            if(activityText.getText().length()>20){
                activityText.setText(activityText.getText().toString().substring(0,19)+"...");
            }

        });


        final ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {

//            context=getContext();

            if(!frameVisibility) {
                messageFrame.setVisibility(View.VISIBLE);
                frameVisibility = true;
                ifAddedNewElement=true;
                contextToAddElement=getContext();
            }else{
                frameVisibility = false;
                messageFrame.setVisibility(INVISIBLE);
            }


        });


        delButton.setOnClickListener(v ->{

            for(int i =0; i<idList.size(); i++){
                if(idList.get(i).isToDelete()){
                    View viewToDelete = gridLayout.findViewById(idList.get(i).getId());
                    gridLayout.removeView(viewToDelete);
                    idList.remove(i);
                }
            }

        });


        remindSpinner = view.findViewById(R.id.reminderSpinner);


        remindCheckbox = view.findViewById(R.id.remindCheckBox);
//        read(dayTextView.getText().toString());
        return view;
    }





    private void addToListActivity(String from, String to, String activ, String mute){
//        if(getContext() != null)context=getContext();
//        if(ifAddedNewElement==true)context=contextToAddElement;

//        if(getContext() != null)context=getContext();


        dynamicViews = new DynamicViews(context);
        TextView tvHour = dynamicViews.hourTextView(context,from+"-"+to);
        TextView tvActivity = dynamicViews.activityTextView(context,activ);
        LinearLayout linearLayout = dynamicViews.linearLayout(context,tvHour,tvActivity);

        tvHour.setOnLongClickListener(v ->
        {
            PopupFragment dialog = new PopupFragment();
            if (getFragmentManager() != null) {
                dialog.show(getFragmentManager(),"dialog");
            }
            return false;

        });

        tvActivity.setOnLongClickListener(v ->
        {
            PopupFragment dialog = new PopupFragment();
            if (getFragmentManager() != null) {
                dialog.show(getFragmentManager(),"dialog");
            }
            return false;
        });


        if(ifAddedNewElement){context=contextToAddElement;}



        gridLayout.addView(linearLayout);
        idList.add(dynamicViews);
        System.out.println(idList.size());



        if(mute.equals("true"))  {
           // audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            }


    }

    private void save(String dateString, String fromText,String toText, String activityText, String mute){
        String textToSave=dateString+"&!&#&"+fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"\n";
        saveToGoogleDrive.appendContents(GoogleDriveOperation.driveFileToOpen,textToSave);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void read(String date){
        //remove all elements from gridLayout
        gridLayout.removeAllViews();
        //compare
        activityList.sort((o1, o2) -> o1[1].compareTo(o2[1]));
        //add all activities to PlannerActivity
        for(int i=0; i<activityList.size();i++){
            if(activityList.get(i)[0].equals(date)) {
                addToListActivity(activityList.get(i)[1], activityList.get(i)[2], activityList.get(i)[3], activityList.get(i)[4]);
            }
        }
    }



    private void unMutePhone(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }


    @Override
    public void onDestroyView() {
        idList.clear();
        super.onDestroyView();
    }

    @Override
    public void delete() {

    }

    @Override
    public void edit() {

    }
}
