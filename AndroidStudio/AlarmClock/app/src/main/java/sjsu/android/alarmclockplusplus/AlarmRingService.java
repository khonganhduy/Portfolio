package sjsu.android.alarmclockplusplus;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmRingService extends Service {

    private Ringtone ringtone;
    private Vibrator vibrator;
    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DESCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle("AlarmClock++") // title for notification
                .setContentText("Alarm is ringing.")// message for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), SnoozeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);

        startForeground(1, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        mediaPlayer = new MediaPlayer();
        if (intent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION)){
            vibrator = (Vibrator)this.getSystemService(Context.VIBRATOR_SERVICE);
            // {number of millis before turning vibrator on, number of millis to keep vibrator on before turning off, number of millis vibrator is off before turning it on}
            long[] pattern = {0, 2000, 2000};
            // repeat is index of pattern
            vibrator.vibrate(pattern, 0);
        }

        if (intent.getExtras().getString(AlarmListDisplayActivity.ALARM_RING_PATH).equals("")){
            Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            this.ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
            ringtone.play();
        }
        else {
            String path = intent.getStringExtra(AlarmListDisplayActivity.ALARM_RING_PATH);
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
            catch (Exception e){
                Toast.makeText(this, "Could not find music file", Toast.LENGTH_SHORT);
            }
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        if (mediaPlayer != null){
            mediaPlayer.stop();
        }
        if (ringtone != null){
            ringtone.stop();
        }
        if (vibrator != null){
            vibrator.cancel();
        }
    }
/*
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("DEBUG", "attempting to restart service");
        Intent myIntent = new Intent("com.android.ServiceStopped");
        myIntent.setClass(this, AlarmBroadcastReceiver.class);
        myIntent.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, rootIntent.getExtras().getInt(AlarmListDisplayActivity.ALARM_SNOOZE_TIME));
        myIntent.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, rootIntent.getExtras().getInt(AlarmListDisplayActivity.ALARM_RING_PATH));
        myIntent.putExtra(AlarmListDisplayActivity.ALARM_ID, rootIntent.getExtras().getInt(AlarmListDisplayActivity.ALARM_ID));
        myIntent.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, rootIntent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
        myIntent.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, rootIntent.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));
        Log.d("DEBUG", "restarting service");
        sendBroadcast(myIntent);
        super.onTaskRemoved(rootIntent);
    }
*/
}
