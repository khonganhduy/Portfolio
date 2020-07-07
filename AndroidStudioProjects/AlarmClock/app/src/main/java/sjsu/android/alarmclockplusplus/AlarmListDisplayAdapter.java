package sjsu.android.alarmclockplusplus;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AlarmListDisplayAdapter extends RecyclerView.Adapter<AlarmListDisplayAdapter.MyViewHolder> {
    private List<Alarm> mDataset;
    private final AlarmViewModel mViewModel;
    private final Context context;
    public int highlightedItemPosition;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView cardView;
        public TextView timeDisplay;
        public TextView dateDisplay;
        public Switch mySwitch;
        public ImageButton deleteButton;
        public AlarmManager alarmManager;


        public MyViewHolder(CardView v) {
            super(v);
            cardView = v;
            timeDisplay = (TextView) v.findViewById(R.id.timeDisplay);
            dateDisplay = (TextView) v.findViewById(R.id.dayDisplay);
            mySwitch = (Switch) v.findViewById(R.id.onOffSwitch);
            deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
            alarmManager = (AlarmManager) v.getContext().getSystemService(Context.ALARM_SERVICE);
        }
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public AlarmListDisplayAdapter(Context context, AlarmViewModel avm) {
        this.context = context;
        mViewModel = avm;
        this.highlightedItemPosition = -1;
    }

    // allows for UI to update
    public void setAlarms(List<Alarm> alarmList) {
        mDataset = alarmList;
        notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlarmListDisplayAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Alarm alarm = mDataset.get(position);
        final String timeText = alarm.getAlarmTime();
        final MyViewHolder vh = holder;

        //Bind alarm to cardview
        vh.cardView.setTag(alarm);
        // Go to settings to update alarm settings
        vh.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(view.getContext(), SetAlarmSettingsActivity.class);
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_TIME, timeText);
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, alarm.getAlarmId());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, alarm.getRingtonePath());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, alarm.getRepeatableDays());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_TRIGGER_DATE, alarm.getTriggerDate());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_MODE, alarm.isSnoozeMode());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, alarm.getSnoozeTime());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_DESC, alarm.getDescription());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, alarm.isVibration_on());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, alarm.isMinigame_on());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_ON, alarm.isAlarm_on());
                myIntent.putExtra(AlarmListDisplayActivity.ALARM_POSITION, position);

                ((Activity) context).startActivityForResult(myIntent, AlarmListDisplayActivity.ALARM_REQUEST_CODE);
            }
        });
        // Toggle on or off alarms
        vh.mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    mViewModel.update(alarm.getAlarmId(), true);
                    // Original date alarm
                    if(alarm.getRepeatableDays() == null) {
                        setTimer(alarm, position);
                    }
                    // Repeated alarm
                    else{
                        int alarmId = alarm.getAlarmId();
                        String[] days = alarm.getRepeatableDays().split(" ");
                        for(int i = 0; i < days.length; i++){
                            switch(days[i]){
                                case "M":
                                    setRepeatTimer(alarm, alarmId + 1, 1);
                                    break;
                                case "T":
                                    setRepeatTimer(alarm, alarmId + 2, 2);
                                    break;
                                case "W":
                                    setRepeatTimer(alarm, alarmId + 3, 3);
                                    break;
                                case "Th":
                                    setRepeatTimer(alarm, alarmId + 4, 4);
                                    break;
                                case "F":
                                    setRepeatTimer(alarm, alarmId + 5, 5);
                                    break;
                                case "Sa":
                                    setRepeatTimer(alarm, alarmId + 6, 6);
                                    break;
                                case "Su":
                                    setRepeatTimer(alarm, alarmId + 7, 7);
                                    break;
                            }
                        }
                    }
                }
                else{
                    // Original alarm deactivate
                    if (alarm.getRepeatableDays() == null) {
                        mViewModel.update(alarm.getAlarmId(), false);
                        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), i, 0);
                        pendingIntent.cancel();
                        pendingIntent = PendingIntent.getBroadcast(context, AlarmListDisplayActivity.SNOOZE_REQUEST_CODE, i, 0);
                        pendingIntent.cancel();
                    }
                    //Repeated days intent
                    else{
                        mViewModel.update(alarm.getAlarmId(), false);
                        Intent intent = new Intent(context, AlarmBroadcastReceiver.class);
                        int alarmId = alarm.getAlarmId();
                        String[] days = alarm.getRepeatableDays().split(" ");
                        PendingIntent pendingIntent;
                        for(int i = 0; i < days.length; i++){
                            switch(days[i]){
                                case "M":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 1, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "T":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 2, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "W":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 3, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "Th":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 4, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "F":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 5, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "Sa":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 6, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                                case "Su":
                                    pendingIntent = PendingIntent.getBroadcast(context, alarmId + 7, intent, 0);
                                    pendingIntent.cancel();
                                    break;
                            }
                        }
                        pendingIntent = PendingIntent.getBroadcast(context, AlarmListDisplayActivity.SNOOZE_REQUEST_CODE, intent, 0);
                        pendingIntent.cancel();
                    }
                }
            }
        });
        // Set alarm to off or on based on previous settings
        vh.mySwitch.setChecked(alarm.isAlarm_on());
        //Delete alarm from management screen as well as database
        vh.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vh.mySwitch.setChecked(false);
                mViewModel.delete((Alarm)vh.cardView.getTag());
            }
        });
        // Display for alarm management screen
        TextView dateDisplay = (TextView)holder.timeDisplay;

        dateDisplay.setText(alarm.getAlarmTime());

        TextView daysDisplay = (TextView)holder.dateDisplay; //MAYBE CHANGE THIS LATER...

        if(alarm.getRepeatableDays() == null){
            daysDisplay.setText("");
            if(alarm.getTriggerDate() != null){
                daysDisplay.setText(alarm.getTriggerDate());
            }
        }
        else {
            daysDisplay.setText(alarm.getRepeatableDays());
        }

        if (highlightedItemPosition == position){
            if (holder.mySwitch.isChecked()){
                holder.mySwitch.setChecked(false);
            }
            highlightedItemPosition = -1;
        }


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        }
        return 0;
    }

    public void setTimer(Alarm alarm, int position){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        try{
            Date newDate = dateFormat.parse(alarm.getAlarmTime());
            Date date = new Date();
            cal_now.setTime(date);
            cal_alarm.setTime(date);
            // To set an alarm on a specific date
            if(alarm.getTriggerDate() != null){
                String[] dateParams = alarm.getTriggerDate().split("/");
                cal_alarm.set(Calendar.MONTH, Integer.parseInt(dateParams[0]) - 1);
                cal_alarm.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParams[1]));
                cal_alarm.set(Calendar.YEAR, Integer.parseInt(dateParams[2]));
            }
            cal_alarm.set(Calendar.HOUR_OF_DAY, newDate.getHours());
            cal_alarm.set(Calendar.MINUTE, newDate.getMinutes());
            cal_alarm.set(Calendar.SECOND, 0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (cal_alarm.before(cal_now)){
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
        i.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, alarm.getRingtonePath());
        i.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, alarm.getSnoozeTime());
        i.putExtra(AlarmListDisplayActivity.ALARM_ID, alarm.getAlarmId());
        i.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, alarm.isVibration_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, alarm.isMinigame_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, alarm.getRepeatableDays());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarm.getAlarmId(), i, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }

    public void setRepeatTimer(Alarm alarm, int alarmSubID, int dayOfWeek){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        try{
            Date newDate = dateFormat.parse(alarm.getAlarmTime());
            Date date = new Date();
            cal_now.setTime(date);
            cal_now.set(Calendar.SECOND, 0);
            cal_alarm.setTime(date);
            cal_alarm.set(Calendar.DAY_OF_WEEK, dayOfWeek);

            cal_alarm.set(Calendar.HOUR_OF_DAY, newDate.getHours());
            cal_alarm.set(Calendar.MINUTE, newDate.getMinutes());
            cal_alarm.set(Calendar.SECOND, 0);
            // Check we aren't setting it in the past which would trigger it to fire instantly
            if(cal_alarm.getTimeInMillis() <= cal_now.getTimeInMillis()) {
                cal_alarm.add(Calendar.DAY_OF_YEAR, 7);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Intent i = new Intent(context, AlarmBroadcastReceiver.class);
        i.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, alarm.getRingtonePath());
        i.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, alarm.getSnoozeTime());
        i.putExtra(AlarmListDisplayActivity.ALARM_ID, alarm.getAlarmId());
        i.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, alarm.isVibration_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, alarm.isMinigame_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, alarm.getRepeatableDays());

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmSubID, i, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
    }
}
