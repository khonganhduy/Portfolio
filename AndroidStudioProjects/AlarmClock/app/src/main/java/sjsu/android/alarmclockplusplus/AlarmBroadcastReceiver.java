package sjsu.android.alarmclockplusplus;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        String filepath = intent.getStringExtra(AlarmListDisplayActivity.ALARM_RING_PATH);
        int snoozeTime = intent.getIntExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, 1);

        Notification noti = new Notification.Builder(context)
                .setContentTitle("Alarm is ON")
                .setContentText("You set up the alarm").build();


        NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        noti.flags|= Notification.FLAG_AUTO_CANCEL;


        Intent startIntent = new Intent(context, AlarmRingService.class);
        startIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, "");
        if (filepath == null){
            startIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, "");
        }
        else{
            startIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, filepath);
        }
        startIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
        startIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, snoozeTime);
        startIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, intent.getExtras().getInt(AlarmListDisplayActivity.ALARM_ID));
        startIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));

        context.startForegroundService(startIntent);


        // Start game activity
        if (intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME)){
            Intent myIntent = new Intent(context, GameActivity.class);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, snoozeTime);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, filepath);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, intent.getExtras().getInt(AlarmListDisplayActivity.ALARM_ID));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, intent.getExtras().getString(AlarmListDisplayActivity.ALARM_REPEAT_DAYS));

            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
        else {
            Intent myIntent = new Intent(context, SnoozeActivity.class);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, snoozeTime);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, filepath);
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, intent.getExtras().getInt(AlarmListDisplayActivity.ALARM_ID));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));
            myIntent.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, intent.getExtras().getString(AlarmListDisplayActivity.ALARM_REPEAT_DAYS));
            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}
