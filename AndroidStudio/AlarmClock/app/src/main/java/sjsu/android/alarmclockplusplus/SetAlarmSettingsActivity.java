package sjsu.android.alarmclockplusplus;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.EditText;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

public class SetAlarmSettingsActivity extends AppCompatActivity implements SnoozeDialogFragment.OnCompleteListener{

    private TimePicker tp;
    private int snooze_time;
    private boolean vibration_on, minigame_on;
    private String description, ringtone_path, repeatable_days, trigger_date;
    TextView snoozeTimeTextView, musicTextView, dateTextView;
    EditText descriptionEditText;
    Switch vibrationToggleSwitch, minigameToggleSwitch;
    ToggleButton saButton,suButton,mButton,tButton,wButton,thButton,fButton;
    boolean[] daysButtonsActivated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.DarkTheme);
        setContentView(R.layout.activity_alarm);
        final Bundle intentBundle = getIntent().getExtras();
        daysButtonsActivated = new boolean[7];

        // Also used to initially set all of the fields in settings!
        String timeText = intentBundle.getString(AlarmListDisplayActivity.ALARM_TIME);
        final int alarm_id = intentBundle.getInt(AlarmListDisplayActivity.ALARM_ID);
        ringtone_path = intentBundle.getString(AlarmListDisplayActivity.ALARM_RING_PATH);
        repeatable_days = intentBundle.getString(AlarmListDisplayActivity.ALARM_REPEAT_DAYS);
        trigger_date = intentBundle.getString(AlarmListDisplayActivity.ALARM_TRIGGER_DATE);
        final boolean snooze_mode = intentBundle.getBoolean(AlarmListDisplayActivity.ALARM_SNOOZE_MODE);
        snooze_time = intentBundle.getInt(AlarmListDisplayActivity.ALARM_SNOOZE_TIME);
        description = intentBundle.getString(AlarmListDisplayActivity.ALARM_DESC);
        vibration_on = intentBundle.getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION);
        minigame_on = intentBundle.getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME);
        final boolean alarm_on = intentBundle.getBoolean(AlarmListDisplayActivity.ALARM_ON);

        // Set description to previously entered description for alarm if it exists
        descriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        if(description != null){
            descriptionEditText.setText(description);
        }
        // Set vibration toggle switch to on or off based on previous settings
        vibrationToggleSwitch = (Switch) findViewById(R.id.vibrationToggle);
        vibrationToggleSwitch.setChecked(vibration_on);
        // Set mingame toggle switch to on or off based on previous settings
        minigameToggleSwitch = (Switch) findViewById(R.id.minigameToggle);
        minigameToggleSwitch.setChecked(minigame_on);
        // Set music display to chosen song from previous settings
        musicTextView = (TextView) findViewById(R.id.second_line1);
        if(ringtone_path != null){
            int index = ringtone_path.lastIndexOf('/');
            String title  = ringtone_path.substring(index + 1);
            title = title.trim();
            musicTextView.setText(title);
        }
        // Set snooze time to previous selected value
        snoozeTimeTextView = (TextView) findViewById(R.id.snoozeTimeTextView);
        snoozeTimeTextView.setText(String.valueOf(snooze_time) + " minutes");


        dateTextView = (TextView) findViewById(R.id.dateDisplayTextView);
        if(trigger_date != null){
            dateTextView.setText(trigger_date);
        }

        mButton = (ToggleButton) findViewById(R.id.mondayButton);
        tButton = (ToggleButton) findViewById(R.id.tuesdayButton);
        wButton = (ToggleButton) findViewById(R.id.wednesdayButton);
        thButton = (ToggleButton) findViewById(R.id.thursdayButton);
        fButton = (ToggleButton) findViewById(R.id.fridayButton);
        saButton = (ToggleButton) findViewById(R.id.saturdayButton);
        suButton = (ToggleButton) findViewById(R.id.sundayButton);


        tp = (TimePicker) findViewById(R.id.time_picker);
        // Set the time picker to display alarm's current time
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        try {
            Date date = dateFormat.parse(timeText);
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            tp.setHour(c.get(Calendar.HOUR_OF_DAY));
            tp.setMinute(c.get(Calendar.MINUTE));

        } catch (ParseException e) {
            // Do nothing, this exception does not affect the overall program
        }

        Button cancel = (Button) findViewById(R.id.cancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        Button save = (Button) findViewById(R.id.saveButton);
        save.setOnClickListener(new View.OnClickListener() { // PULL DATA FROM THE SETTINGS VIEW TO UPDATE DATABASE (with updateIntent.putExtra(FIELD DEFINED IN ALARMLISTDISPLAYACTIVITY, VALUE))
            @Override
            public void onClick(View view) {


                String time;
                String mins = Integer.toString(tp.getMinute());
                if(tp.getMinute() < 10){
                    mins = "0" + mins;
                }
                if(tp.getHour() > 12) {
                    time = (tp.getHour() - 12) + ":" + mins + " PM";
                }
                else{
                    if(tp.getHour() == 0) {
                        time = "12:" + mins + " AM";
                    }
                    else{
                        time = tp.getHour() + ":" + mins + " AM";
                    }
                }

                if(dateTextView.getText().toString().compareTo(getString(R.string.date_message)) != 0){
                    repeatable_days = null;
                    trigger_date = dateTextView.getText().toString();
                }
                description = descriptionEditText.getText().toString();

                if(dateTextView.getText().toString().compareTo(getString(R.string.date_message)) == 0) {
                    for (int i = 0; i < daysButtonsActivated.length; i++) {
                        switch (i) {
                            case 0:
                                if (daysButtonsActivated[0]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "M ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("M ");
                                    }
                                }
                                break;
                            case 1:
                                if (daysButtonsActivated[1]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "T ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("T ");
                                    }
                                }
                                break;
                            case 2:
                                if (daysButtonsActivated[2]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "W ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("W ");
                                    }
                                }
                                break;
                            case 3:
                                if (daysButtonsActivated[3]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "Th ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("Th ");
                                    }
                                }
                                break;
                            case 4:
                                if (daysButtonsActivated[4]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "F ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("F ");
                                    }
                                }
                                break;
                            case 5:
                                if (daysButtonsActivated[5]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "Sa";
                                    } else {
                                        repeatable_days = repeatable_days.concat("Sa ");
                                    }
                                }
                                break;
                            case 6:
                                if (daysButtonsActivated[6]) {
                                    if (repeatable_days == null) {
                                        repeatable_days = "Su ";
                                    } else {
                                        repeatable_days = repeatable_days.concat("Su ");
                                    }
                                }
                                break;
                        }
                    }
                }
                if(repeatable_days != null) {
                    repeatable_days = repeatable_days.substring(0, repeatable_days.length() - 1);
                }
                Intent updateIntent = new Intent();
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, alarm_id);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_TIME, time);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, snooze_time);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, ringtone_path);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, repeatable_days);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_TRIGGER_DATE, trigger_date);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_MODE, snooze_mode);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_DESC, description);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, vibration_on);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, minigame_on);
                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_ON, alarm_on);

                updateIntent.putExtra(AlarmListDisplayActivity.ALARM_POSITION, intentBundle.getInt(AlarmListDisplayActivity.ALARM_POSITION));

                setResult(RESULT_OK, updateIntent);
                finish();
            }
        });
        // Custom Sound Selector
        View soundSelector = (View) findViewById(R.id.sound_selector);
        soundSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent soundSelectorIntent = new Intent(view.getContext() , SoundSelectorActivity.class);
                startActivityForResult(soundSelectorIntent, 0);
            }
        });

        // Custom snooze time
        View snoozeSelector = (View) findViewById(R.id.snooze_selector);
        snoozeSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SnoozeDialogFragment dialogFragment = new SnoozeDialogFragment(view);
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                dialogFragment.show(activity.getSupportFragmentManager(), "slider");
            }
        });

        // Area for date selection options to be displayed
        View dateSelector = (View) findViewById(R.id.date_selector);
        final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // Make repeat days empty if we select a date
                repeatable_days = null;
                mButton.setChecked(false);
                tButton.setChecked(false);
                wButton.setChecked(false);
                thButton.setChecked(false);
                fButton.setChecked(false);
                saButton.setChecked(false);
                suButton.setChecked(false);
                // MONTH STARTS AT 0 SO WE CHANGE IT TO MATCH NORMAL CONVENTION
                int normalMonth = month + 1;
                dateTextView.setText(normalMonth + "/" + day + "/" + year);
            }
        };
        // Display the datePicker dialog when user clicks on the date area
        dateSelector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(SetAlarmSettingsActivity.this,
                        android.R.style.Theme_DeviceDefault_Dialog_NoActionBar_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.show();
            }
        });
        vibrationToggleSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                vibration_on = b;
            }
        });

        minigameToggleSwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                minigame_on = b;
            }
        });

        mButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[0] = true;
                }
                else{
                    daysButtonsActivated[0] = false;
                }
            }
        });
        tButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[1] = true;
                }
                else{
                    daysButtonsActivated[1] = false;
                }
            }
        });
        wButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[2] = true;
                }
                else{
                    daysButtonsActivated[2] = false;
                }
            }
        });
        thButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[3] = true;
                }
                else{
                    daysButtonsActivated[3] = false;
                }
            }
        });
        fButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[4] = true;
                }
                else{
                    daysButtonsActivated[4] = false;
                }
            }
        });
        saButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[5] = true;
                }
                else{
                    daysButtonsActivated[5] = false;
                }
            }
        });
        suButton.setOnCheckedChangeListener(new ToggleButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    trigger_date = null;
                    dateTextView.setText(getString(R.string.date_message));
                    daysButtonsActivated[6] = true;
                }
                else{
                    daysButtonsActivated[6] = false;
                }
            }
        });

        // Set the repeat day buttons to on if they were previously set on
        if(repeatable_days != null){
            String[] daysActivated = repeatable_days.split(" ");
            repeatable_days = null; // Clear out the initial selection as user may select different days
            for(int i = 0; i < daysActivated.length; i++){
                switch (daysActivated[i]){
                    case "M":
                        mButton.setChecked(true);
                        break;
                    case "T":
                        tButton.setChecked(true);
                        break;
                    case "W":
                        wButton.setChecked(true);
                        break;
                    case "Th":
                        thButton.setChecked(true);
                        break;
                    case "F":
                        fButton.setChecked(true);
                        break;
                    case "Sa":
                        saButton.setChecked(true);
                        break;
                    case "Su":
                        suButton.setChecked(true);
                        break;
                }
            }
        }
    }

    // Return information from fragment
    @Override
    public void onComplete(int minutes){
        snooze_time = minutes;
        snoozeTimeTextView.setText(String.valueOf(minutes) + " minutes");
    }

    //------------------------------------------------------------------


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String name = data.getStringExtra(AlarmListDisplayActivity.ALARM_RING_NAME);
            ringtone_path = data.getStringExtra(AlarmListDisplayActivity.ALARM_RING_PATH);
            musicTextView.setText(name);
        }
    }
}
