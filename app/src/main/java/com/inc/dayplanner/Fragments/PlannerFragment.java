package com.inc.dayplanner.Fragments;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.AlertReceiver;
import com.inc.dayplanner.CheckMuteThread;
import com.inc.dayplanner.ViewChange.DynamicViews;
import com.inc.dayplanner.GoogleDriveApi.GoogleDriveOperation;
import com.inc.dayplanner.R;
import com.inc.dayplanner.ViewChange.SwipeAdapter;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.resolveSize;


public class PlannerFragment extends Fragment  implements PopupFragment.ActivityHandlerListener, AdapterView.OnItemSelectedListener {


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
    public static AudioManager audioManager;
    private DateFormat df = new SimpleDateFormat("d MMM yyyy");
    private Calendar calendar = Calendar.getInstance();
    public static List<String[]> activityList = new ArrayList<>();
    public static List<Context> contextList = new ArrayList<>();
    private static boolean ifAddedNewElement=false;
    private Button delButton;
    private List<DynamicViews> idList = new ArrayList<>();
    private String remainderTime;
    private @ColorInt int color;
    private TextView wrongHourTextView;
    private ImageButton button;


    private String date;
    private int position;


    public static PlannerFragment newInstance(String date, int position) {

        Bundle bundle = new Bundle();
        bundle.putString("Date", date);
        bundle.putInt("Position",position);

        PlannerFragment fragment = new PlannerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.goToButton:

                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.setCallBack(ondate);
                datePicker.show(getFragmentManager(),"date picker");
                return true;

        }

        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_planner, container, false);
        gridLayout = view.findViewById(R.id.gridLayout);
        gridLayout.removeAllViews();
        dayTextView = view.findViewById(R.id.dayText);
        messageFrame = view.findViewById(R.id.messageFrame);
        activityText = view.findViewById(R.id.activityText);
        muteCheckbox = view.findViewById(R.id.muteCheckBox);
        remindSpinner = view.findViewById(R.id.reminderSpinner);
        wrongHourTextView = view.findViewById(R.id.wrongHoursTextView);
//        remindCheckbox = view.findViewById(R.id.remindCheckBox);
        fromHourPickerTextView = view.findViewById(R.id.fromHourPicker);
        toHourPickerTextView = view.findViewById(R.id.toHourPicker);
        final ImageButton addButton = view.findViewById(R.id.addButton2);
        audioManager = (AudioManager)getContext().getSystemService(getContext().AUDIO_SERVICE);
        delButton = view.findViewById(R.id.deleteButton);


        contextList.add(context);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.textcolor, typedValue, true);
        color = typedValue.data;


        Thread thread = new Thread(new CheckMuteThread());
        thread.start();



        readBundle(getArguments());



        //View fragment date
        String message = null;
        if (getArguments() != null) {
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


        //Set activity starting hour
        toHourPickerTextView.setOnClickListener(v -> {

            TimePickerFragment timePickerFragment = new TimePickerFragment(toHourPickerTextView);
            assert getFragmentManager() != null;
            timePickerFragment.show(getFragmentManager(),"timePicker");
        });

        //Set activity ending hour
        fromHourPickerTextView.setOnClickListener(v -> {

            TimePickerFragment timePickerFragment = new TimePickerFragment(fromHourPickerTextView);
            assert getFragmentManager() != null;
            timePickerFragment.show(getFragmentManager(),"timePicker");
        });

        //Add button handler
        addButton.setOnClickListener(v -> {
            boolean allDataVerified = false;
//            String date = df.format(calendar.getTime());
            String date = dayTextView.getText().toString();
            hour2 = toHourPickerTextView.getText().toString();
            hour1 = fromHourPickerTextView.getText().toString();

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            Date d1 = null;
            Date d2 = null;
            long elapse = 0;
            try {
                d1 = sdf.parse(hour1);
                d2 = sdf.parse(hour2);
                elapse = d2.getTime() - d1.getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }

            System.out.println(elapse);

            if(elapse <=0){
                messageFrame.setBackgroundResource(R.drawable.bubble_red);
                    fromHourPickerTextView.setTextColor(Color.parseColor("#e71837"));
                    toHourPickerTextView.setTextColor(Color.parseColor("#e71837"));
                    wrongHourTextView.setVisibility(View.VISIBLE);

            }else{
                fromHourPickerTextView.setTextColor(color);
                toHourPickerTextView.setTextColor(color);
                messageFrame.setBackgroundResource(R.drawable.bubble);
                wrongHourTextView.setVisibility(View.INVISIBLE);
                frameVisibility = false;
                allDataVerified = true;

            }



            if(muteCheckbox.isChecked())  {
                //ADD BUTTON METHODS
//                if(audioManager.getRingerMode()!=AudioManager.RINGER_MODE_VIBRATE){
//                    audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
//                 }

            }
            String mute;
            if(muteCheckbox.isChecked()){
                mute="true";
            }else{
                mute="false";
            }
            //---------------------------------------------------------------------------------------REMAINDER---------------------------------------------------------------------------------------
            calendar = Calendar.getInstance();
            String dateToParse="";
            if(date.equals("Sunday")||date.equals("Monday")||date.equals("Tuesday")||date.equals("Wednesday")||date.equals("Thursday")||date.equals("Friday")||date.equals("Saturday")){
                dateToParse = hour1+"-"+ df.format(calendar.getTime());
            }else{
                dateToParse = hour1+"-"+dayTextView.getText().toString();
            }

            SimpleDateFormat remainderDF = new SimpleDateFormat("HH:mm-d MMM yyyy");
            try {
                calendar.setTime(remainderDF.parse(dateToParse));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            switch(remainderTime){
                case "15 minutes earlier":
//                    calendar.add(Calendar.DATE,position-previousPosition);
                    calendar.add(Calendar.MINUTE,-15);
                    break;
                case "30 minutes earlier":
                    calendar.add(Calendar.MINUTE,-30);
                    break;
                case "1 hour earlier":
                    calendar.add(Calendar.MINUTE,-60);
                    break;
                case "2 hours earlier":
                    calendar.add(Calendar.MINUTE,-120);
                    break;
                case "1 day earlier":
                    calendar.add(Calendar.DATE,-1);
                    break;
            }
            String dateToSaveRemainder="";
            if(remainderTime.equals("Remaind me")||remainderTime.equals("No remaind me")){
                dateToSaveRemainder = "no remaind";
            }else{
                Calendar cCurrent = Calendar.getInstance();
                Date date1 = calendar.getTime();
                Date dateCurrent = cCurrent.getTime();
                if(date1.after(dateCurrent)) {
                    dateToSaveRemainder = remainderDF.format(calendar.getTime());
                    MainActivity.mainActivity.setNotification(dateToSaveRemainder, activityText.getText().toString(), remainderTime);
                }
            }


            //---------------------------------------------------------------------------------------END REMAINDER---------------------------------------------------------------------------------------

            if(allDataVerified) {

           // addToListActivity(hour1,hour2,activityText.getText().toString());
            String[] addElement = {hour1,hour2,activityText.getText().toString(),mute, dateToSaveRemainder};
            activityList.add(addElement);
            if(date.equals("Sunday")||date.equals("Monday")||date.equals("Tuesday")||date.equals("Wednesday")||date.equals("Thursday")||date.equals("Friday")||date.equals("Saturday")){
                date=df.format(calendar.getTime());
            }
            save(date,hour1,hour2,activityText.getText().toString(),mute,dateToSaveRemainder);
//            sortAndAddToLayout(dayTextView.getText().toString());

            if(activityText.getText().length()>20){
                activityText.setText(activityText.getText().toString().substring(0,19)+"...");
            }


                messageFrame.setVisibility(INVISIBLE);
                fromHourPickerTextView.setText(R.string.start_hour);
                toHourPickerTextView.setText(R.string.end_hour);
                activityText.setText("");

             //   button.callOnClick();


                Handler handler = new Handler();
                handler.postDelayed(this::refresh, 1000);



            }

        });


        button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {

//            context=getContext();
            activityList.sort((o1, o2) -> o1[1].compareTo(o2[1]));

            if(!frameVisibility) {
                messageFrame.setVisibility(View.VISIBLE);
                frameVisibility = true;
                ifAddedNewElement=true;
                contextToAddElement=getContext();

            }else{
                frameVisibility = false;
                refresh();
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


//        remindCheckbox = view.findViewById(R.id.remindCheckBox);
//        read(dayTextView.getText().toString());

        Spinner spinner = view.findViewById(R.id.reminderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.remainder, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        return view;
    }





    private void addToListActivity(String from, String to, String activ){
//        if(getContext() != null)context=getContext();
//        if(ifAddedNewElement==true)context=contextToAddElement;

//        if(getContext() != null)context=getContext();

        dynamicViews = new DynamicViews(context);
        TextView tvHour = dynamicViews.hourTextView(context,from+"-"+to);
        TextView tvActivity = dynamicViews.activityTextView(context,activ);
        LinearLayout linearLayout = dynamicViews.linearLayout(context,tvHour,tvActivity);




        tvHour.setTextColor(color);
        tvActivity.setTextColor(color);



        tvHour.setOnLongClickListener(v ->
        {
            PopupFragment dialog = new PopupFragment();
            if (getFragmentManager() != null) {
                dialog.setTargetFragment(PlannerFragment.this,1);
                dialog.show(getFragmentManager(),"dialog");
            }
            return false;

        });

        tvActivity.setOnLongClickListener(v ->
        {
            PopupFragment dialog = new PopupFragment();
            if (getFragmentManager() != null) {
                dialog.setTargetFragment(PlannerFragment.this,1);
                dialog.show(getFragmentManager(),"dialog");
            }
            return false;
        });


      //  if(ifAddedNewElement){context=contextToAddElement;}

        gridLayout.addView(linearLayout);
        idList.add(dynamicViews);
   //     System.out.println(idList.size());

        //System.out.println(idList);


    }

    private void save(String dateString, String fromText,String toText, String activityText, String mute,String dateRemainder){
        String textToSave=dateString+"&!&#&"+fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"&!&#&"+dateRemainder+"\n";
        saveToGoogleDrive.appendContents(GoogleDriveOperation.driveFileToOpen,textToSave);
        refresh();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void read(String date){
        //remove all elements from gridLayout
        gridLayout.removeAllViews();
        //compare

        //add all activities to PlannerActivity
        for(int i=0; i<activityList.size();i++){
            if(activityList.get(i)[0].equals(date)) {
                addToListActivity(activityList.get(i)[1], activityList.get(i)[2], activityList.get(i)[3]);
            }
        }
    }



    private void unMutePhone(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        remainderTime = parent.getItemAtPosition(position).toString();
//        Toast.makeText(parent.getContext(),remainderTime, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroyView() {
        idList.clear();
        super.onDestroyView();
    }



    @Override
    public void onItemDeleted(){

        System.out.println("onItemDeleted");
        refresh();
    }

    @Override
    public void onItemEdited(){
        System.out.println("onItemEdited");

    }

    DatePickerDialog.OnDateSetListener ondate = (view, year, monthOfYear, dayOfMonth) -> {

        System.out.println("DateListener");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String goToSpecificDate = DateFormat.getDateInstance().format(c.getTime());

       // dayTextView.setText(goToSpecificDate);

        refresh();
    };




    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            date = bundle.getString("name");
            position = bundle.getInt("age");
        }

    }

    private void refresh(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        System.out.println("refresh");
    }

}
