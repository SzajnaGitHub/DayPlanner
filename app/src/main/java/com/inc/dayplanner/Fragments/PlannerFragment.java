package com.inc.dayplanner.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.inc.dayplanner.Activities.MainActivity;
import com.inc.dayplanner.CheckMuteThread;
import com.inc.dayplanner.NetworkState;
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
import java.util.List;

import static android.view.View.INVISIBLE;


public class PlannerFragment extends Fragment  implements PopupFragment.ActivityHandlerListener, AdapterView.OnItemSelectedListener {


    private GridLayout gridLayout;
    public TextView dayTextView;
    private Context context;
    private FrameLayout messageFrame;
    private TextView activityText;
    private CheckBox muteCheckbox;
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
    private List<DynamicViews> idList = new ArrayList<>();
    private String remainderTime;
    private @ColorInt int textcolor;
    private @ColorInt int canceltextcolor;
    private TextView wrongHourTextView;
    private boolean isDaily = false;
    private static String dateToDelete;
    public static boolean isSkipToDate=false;
    public static Calendar skipToCalendar;


    public static PlannerFragment newInstance(String date, boolean dateFormat, int position) {

        Bundle bundle = new Bundle();
        bundle.putString("Date", date);
        bundle.putBoolean("dateFormat",dateFormat);
        bundle.putInt("position",position);

        PlannerFragment fragment = new PlannerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }



    @Override
    public void onStart() {
        super.onStart();
        context=getContext();
    }

    @Override
    public void onAttach(Context mcontext) {
        super.onAttach(context);
        context = mcontext;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem goToItem = menu.findItem(R.id.goToButton);
        if(!isDaily) {
            goToItem.setVisible(true);
        } else {
            goToItem.setVisible(false);
        }

        //super.onCreateOptionsMenu(menu, inflater);
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
        wrongHourTextView = view.findViewById(R.id.wrongHoursTextView);
        fromHourPickerTextView = view.findViewById(R.id.fromHourPicker);
        toHourPickerTextView = view.findViewById(R.id.toHourPicker);
        final ImageButton addButton = view.findViewById(R.id.addButton2);
        audioManager = (AudioManager)getContext().getSystemService(getContext().AUDIO_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityList.sort((o1, o2) -> o1[1].compareTo(o2[1]));
        }

        contextList.add(context);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(R.attr.textcolor, typedValue, true);
        textcolor = typedValue.data;
        theme.resolveAttribute(R.attr.canceltextcolor, typedValue, true);
        canceltextcolor = typedValue.data;


        Thread thread = new Thread(new CheckMuteThread());
        thread.start();



        //View fragment date
        String message;
        readBundle(getArguments());
        if (getArguments()!=null && !getArguments().getBoolean("dateFormat")) {

            message = getArguments().getString("Date");
            dayTextView.setText(message);
            context = getContext();
            read(dayTextView.getText().toString());
            isDaily = false;
        } else if(getArguments()!=null && getArguments().getBoolean("dateFormat")) {

            int day = getArguments().getInt("position");

            dayTextView.setText(SwipeAdapter.setDayOfTheWeek(day));
            isDaily = true;

        }else {
            dayTextView.setText(SwipeAdapter.setDay(1));
            context=getContext();
            String date = df.format(calendar.getTime());
            read(date);
            isDaily = true;
        }

        if(isDaily) {
            //getActivity().getActionBar().setDisplay
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

            boolean status = NetworkState.getConnectivityStatusString(getContext());
            if(!status) {
                Toast noConnection = Toast.makeText(getContext(),"No internet connection. Turn on Wi-Fi or Mobile Data", Toast.LENGTH_LONG);
                noConnection.show();
            }else {

                boolean allDataVerified = false;
                String date = dayTextView.getText().toString();
                hour2 = toHourPickerTextView.getText().toString();
                hour1 = fromHourPickerTextView.getText().toString();

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date d1;
                Date d2;
                long elapse = 0;
                try {
                    d1 = sdf.parse(hour1);
                    d2 = sdf.parse(hour2);
                    elapse = d2.getTime() - d1.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                System.out.println(elapse);

                if (elapse <= 0) {
                    messageFrame.setBackgroundResource(R.drawable.bubble_red);
                    fromHourPickerTextView.setTextColor(Color.parseColor("#e71837"));
                    toHourPickerTextView.setTextColor(Color.parseColor("#e71837"));
                    wrongHourTextView.setVisibility(View.VISIBLE);

                } else {
                    fromHourPickerTextView.setTextColor(textcolor);
                    toHourPickerTextView.setTextColor(textcolor);
                    messageFrame.setBackgroundResource(R.drawable.bubble);
                    wrongHourTextView.setVisibility(INVISIBLE);
                    frameVisibility = false;
                    allDataVerified = true;

                }


                String mute;
                if (muteCheckbox.isChecked()) {
                    mute = "true";
                } else {
                    mute = "false";
                }

                //---------------------------------------------------------------------------------------REMAINDER---------------------------------------------------------------------------------------
                calendar = Calendar.getInstance();
                String dateToParse = "";
                if (date.equals("Sunday") || date.equals("Monday") || date.equals("Tuesday") || date.equals("Wednesday") || date.equals("Thursday") || date.equals("Friday") || date.equals("Saturday")) {
                    dateToParse = hour1 + "-" + df.format(calendar.getTime());
                } else {
                    dateToParse = hour1 + "-" + dayTextView.getText().toString();
                }

                SimpleDateFormat remainderDF = new SimpleDateFormat("HH:mm-d MMM yyyy");
                try {
                    calendar.setTime(remainderDF.parse(dateToParse));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                switch (remainderTime) {
                    case "15 minutes earlier":
                        calendar.add(Calendar.MINUTE, -15);
                        break;
                    case "30 minutes earlier":
                        calendar.add(Calendar.MINUTE, -30);
                        break;
                    case "1 hour earlier":
                        calendar.add(Calendar.MINUTE, -60);
                        break;
                    case "2 hours earlier":
                        calendar.add(Calendar.MINUTE, -120);
                        break;
                    case "1 day earlier":
                        calendar.add(Calendar.DATE, -1);
                        break;
                }
                String dateToSaveRemainder = "";
                if (remainderTime.equals("Set reminder")) {
                    dateToSaveRemainder = "Set reminder";
                } else {
                    Calendar cCurrent = Calendar.getInstance();
                    Date date1 = calendar.getTime();
                    Date dateCurrent = cCurrent.getTime();
                    if (date1.after(dateCurrent)) {
                        dateToSaveRemainder = remainderDF.format(calendar.getTime());
                        MainActivity.mainActivity.setNotification(dateToSaveRemainder, activityText.getText().toString(), remainderTime);
                    } else {
                        dateToSaveRemainder = "Set reminder";
                    }
                }


                //---------------------------------------------------------------------------------------END REMAINDER---------------------------------------------------------------------------------------

                if (allDataVerified) {

                    if (isDaily) {
                        addToListActivity(hour1, hour2, activityText.getText().toString(),"active");
                    }
                    String[] addElement = {date, hour1, hour2, activityText.getText().toString(), mute, dateToSaveRemainder, remainderTime, "active"};
                    activityList.add(addElement);
                    if (date.equals("Sunday") || date.equals("Monday") || date.equals("Tuesday") || date.equals("Wednesday") || date.equals("Thursday") || date.equals("Friday") || date.equals("Saturday")) {
                        date = df.format(calendar.getTime());
                    }


                    save(date, hour1, hour2, activityText.getText().toString(), mute, dateToSaveRemainder, remainderTime, "active");
//            sortAndAddToLayout(dayTextView.getText().toString());

                    if (activityText.getText().length() > 20) {
                        activityText.setText(activityText.getText().toString().substring(0, 19) + "...");
                    }


                messageFrame.setVisibility(INVISIBLE);
                fromHourPickerTextView.setText(R.string.start_hour);
                toHourPickerTextView.setText(R.string.end_hour);
                activityText.setText("");
                muteCheckbox.setChecked(false);


                    if (!isDaily) {
                        Handler handler = new Handler();
                        handler.postDelayed(this::refresh, 1000);
                    }


                    closeKeyboard(view);

                }
            }
        });


        ImageButton button = view.findViewById(R.id.addButton);
        button.setOnClickListener(v -> {
            //activityList.sort((o1, o2) -> o1[1].compareTo(o2[1]));
            showHideAddActivityFragment();
        });



       // remindSpinner = view.findViewById(R.id.reminderSpinner);


        Spinner spinner = view.findViewById(R.id.reminderSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.remainder, R.layout.spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }


    private void showHideAddActivityFragment(){
        if(!frameVisibility) {
            messageFrame.setVisibility(View.VISIBLE);
            frameVisibility = true;

        }else{
            frameVisibility = false;
            messageFrame.setVisibility(INVISIBLE);

            if (isDaily) {
                refresh();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    activityList.sort(((o1, o2) -> o1[1].compareTo(o2[1])));
                }
            }
        }
    }


    private String[] deleteFromListActivity(DynamicViews dynamicViewsToDelete){

        String[] dateToEdit;

        int idActivityToDelete=searchActivity(dynamicViewsToDelete);
        dateToEdit=activityList.get(idActivityToDelete);
        activityList.remove(idActivityToDelete);
        saveFromArrayListToFile();

        return dateToEdit;
    }

    private int searchActivity(DynamicViews dynamicViewsToDelete){

        if(dateToDelete.equals("Sunday")||dateToDelete.equals("Monday")||dateToDelete.equals("Tuesday")||
                dateToDelete.equals("Wednesday")||dateToDelete.equals("Thursday")||dateToDelete.equals("Friday")|| dateToDelete.equals("Saturday")){

            dateToDelete=df.format(calendar.getTime());
        }

        String[] hour;
        String[] deleteElement = new String[4];// = {hour1,hour2,activityText.getText().toString(),mute, dateToSaveRemainder};
        for (int i=0;i<idList.size();i++){

            if(idList.get(i).equals(dynamicViewsToDelete)){

                hour=idList.get(i).getHourText().split("-");
                deleteElement[0]=dateToDelete;
                deleteElement[1]=hour[0];
                deleteElement[2]=hour[1];
                deleteElement[3]=idList.get(i).getActivityText();
                for(int j=0;j<activityList.size();j++){

                    if(activityList.get(j)[0].equals(deleteElement[0]) && activityList.get(j)[1].equals(deleteElement[1]) &&
                            activityList.get(j)[2].equals(deleteElement[2]) && activityList.get(j)[3].equals(deleteElement[3])){
                            return j;
                    }
                }
            }
        }
        return -1;
    }

    private void editActivity(DynamicViews dynamicViews){
        showHideAddActivityFragment();
        String[] dateToEdit = deleteFromListActivity(dynamicViews);
        fromHourPickerTextView.setText(dateToEdit[1]);
        toHourPickerTextView.setText(dateToEdit[2]);
        activityText.setText(dateToEdit[3]);
        if(dateToEdit[4].equals("true")){
            muteCheckbox.setChecked(true);
        }else{
            muteCheckbox.setChecked(false);
        }
        if(!dateToEdit[5].equals("Set reminder")){

           switch (dateToEdit[6]){
               case "15 minutes earlier":
//                    calendar.add(Calendar.DATE,position-previousPosition);
                   remindSpinner.setSelection(1);
                   break;
               case "30 minutes earlier":
                   remindSpinner.setSelection(2);
                   break;
               case "1 hour earlier":
                   remindSpinner.setSelection(3);
                   break;
               case "2 hours earlier":
                   remindSpinner.setSelection(4);
                   break;
               case "1 day earlier":
                   remindSpinner.setSelection(5);
                   break;

                   default:
                       remindSpinner.setSelection(0);
                       break;
           }
        }

    }

    LinearLayout testLayout;
    DynamicViews testDV;

    private void addToListActivity(String from, String to, String activ, String status) {

        DynamicViews dynamicViews = new DynamicViews(context);
        TextView tvHour = dynamicViews.hourTextView(context, from + "-" + to);
        TextView tvActivity = dynamicViews.activityTextView(context, activ);
        LinearLayout linearLayout = dynamicViews.linearLayout(context, tvHour, tvActivity);
        if(status.equals("cancelled")){
            tvHour.setTextColor(canceltextcolor);
            tvActivity.setTextColor(canceltextcolor);
        }else{
            tvHour.setTextColor(textcolor);
            tvActivity.setTextColor(textcolor);
        }


        final int[] id = {0};
        final int[] idAct = new int[1];

        tvHour.setOnLongClickListener(v ->
        {
            testLayout = linearLayout;
            id[0] = linearLayout.getId();

            for(int i =0; i< idList.size(); i++){

                if(idList.get(i).getId() == id[0]){
                    testDV= idList.get(i);
                    idAct[0] =i;
                }
            }
            PopupFragment.status = activityList.get(idAct[0])[7];
            dateToDelete = dayTextView.getText().toString();
            PopupFragment dialog = new PopupFragment();
            if (getFragmentManager() != null) {
                dialog.setTargetFragment(PlannerFragment.this, 1);
                dialog.show(getFragmentManager(), "dialog");
            }

            return false;
        });


        tvActivity.setOnLongClickListener(v ->
        {
           testLayout = linearLayout;
           id[0] = linearLayout.getId();

             for(int i =0; i< idList.size(); i++){

                if(idList.get(i).getId() == id[0]){
                    testDV= idList.get(i);
                    idAct[0] =i;
                }
            }
                PopupFragment.status = activityList.get(idAct[0])[7];
                dateToDelete = dayTextView.getText().toString();
                PopupFragment dialog = new PopupFragment();
                if (getFragmentManager() != null) {
                    dialog.setTargetFragment(PlannerFragment.this, 1);
                    dialog.show(getFragmentManager(), "dialog");

            }

            return false;
        });

        gridLayout.addView(linearLayout);
        idList.add(dynamicViews);
    }


    private void save(String dateString, String fromText,String toText,
                      String activityText, String mute,String dateRemainder, String remainderTime, String status){

        String textToSave=dateString+"&!&#&"+fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"&!&#&"
                +dateRemainder+"&!&#&"+remainderTime+"&!&#&"+status+"\n";


        saveToGoogleDrive.appendContents(GoogleDriveOperation.driveFileToOpen,textToSave);
    }

    private void saveFromArrayListToFile(){
        String textToSave;//=dateString+"&!&#&"+fromText+"&!&#&"+toText+"&!&#&"+activityText+"&!&#&"+mute+"&!&#&"+dateRemainder+"\n";
        textToSave="";
        for(int i=0;i<activityList.size();i++){
            textToSave+=activityList.get(i)[0]+"&!&#&"+activityList.get(i)[1]+"&!&#&"+activityList.get(i)[2]+"&!&#&"+activityList.get(i)[3]+"&!&#&"+activityList.get(i)[4]+"&!&#&"+activityList.get(i)[5]+"&!&#&"+activityList.get(i)[6]+"&!&#&"+activityList.get(i)[7]+"\n";
        }
        saveToGoogleDrive.rewriteContents(GoogleDriveOperation.driveFileToOpen,textToSave);
    }



    private void read(String date){
        //remove all elements from gridLayout
        gridLayout.removeAllViews();
        //compare
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activityList.sort(((o1, o2) -> o1[1].compareTo(o2[1])));
        }
        //add all activities to PlannerActivity
        for(int i=0; i<activityList.size();i++){
            if(activityList.get(i)[0].equals(date)) {
                addToListActivity(activityList.get(i)[1], activityList.get(i)[2], activityList.get(i)[3], activityList.get(i)[7]);
            }
        }
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        remainderTime = parent.getItemAtPosition(position).toString();
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

        gridLayout.removeView(testLayout);
        deleteFromListActivity(testDV);


    }

    @Override
    public void onItemEdited(){

        gridLayout.removeView(testLayout);
        editActivity(testDV);



    }

    @Override
    public void onItemCanceled() {


        //TODO: zmiana statusu i zapis
        int idActivity=searchActivity(testDV);
        String[] activity = activityList.get(idActivity);
        if(activity[7].equals("active")) {
            activity[7] = "cancelled";
            testDV.getHoutTV().setTextColor(canceltextcolor);
            testDV.getActivityTV().setTextColor(canceltextcolor);
        }else{
            activity[7] = "active";
            testDV.getHoutTV().setTextColor(textcolor);
            testDV.getActivityTV().setTextColor(textcolor);
        }
        activityList.set(idActivity,activity);
        saveFromArrayListToFile();
    }




    DatePickerDialog.OnDateSetListener ondate = (view, year, monthOfYear, dayOfMonth) -> {

        System.out.println("DateListener");

        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        isSkipToDate=true;
        skipToCalendar=c;
        SwipeAdapter swipeAdapterSkipTo = new SwipeAdapter(getFragmentManager(),10000);
        swipeAdapterSkipTo.calendar=c;
        DailyPlanFragment.viewPager.setAdapter(swipeAdapterSkipTo);
        DailyPlanFragment.viewPager.setCurrentItem(4999);
        DailyPlanFragment.viewPager.getAdapter().notifyDataSetChanged();
//        refresh();
    };



    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            bundle.getString("name");
            bundle.getBoolean("dateFormat");
            bundle.getInt("position");

        }

    }
    private void refresh(){
        FragmentTransaction ft = null;
        if (getFragmentManager() != null) {
            ft = getFragmentManager().beginTransaction();
        }
        if (Build.VERSION.SDK_INT >= 26 && ft != null) {
            ft.setReorderingAllowed(false);
        }
        ft.detach(this).attach(this).commit();
        System.out.println("refresh");
    }
    private void closeKeyboard(View view) {
        if(view !=null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
    private void unMutePhone(AudioManager audioManager) {
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_RING);

        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

}
