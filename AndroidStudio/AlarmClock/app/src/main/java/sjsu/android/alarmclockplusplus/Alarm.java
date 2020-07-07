package sjsu.android.alarmclockplusplus;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "alarm_table")
public class Alarm {
    @PrimaryKey
    @NonNull
    private int alarmId;

    @NonNull
    @ColumnInfo(name = "time")
    private String alarmTime;

    @Nullable
    @ColumnInfo(name = "ringtone_path")
    private String ringtonePath;

    @Nullable
    @ColumnInfo(name = "repeatable_days")
    private String repeatableDays;

    @Nullable
    @ColumnInfo(name = "trigger_date")
    private String triggerDate;

    @NonNull
    @ColumnInfo(name = "snooze_mode")
    private boolean snoozeMode;

    @ColumnInfo(name = "snooze_time")
    private int snoozeTime;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "vibration_on")
    private boolean vibration_on;

    @ColumnInfo(name = "minigame_on")
    private boolean minigame_on;

    @ColumnInfo(name = "alarm_on")
    private boolean alarm_on;

    public Alarm(int alarmId, String alarmTime){
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.snoozeMode = false;
    }

    @Ignore
    public Alarm(int alarmId, String alarmTime, boolean snoozeMode){
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.snoozeMode = snoozeMode;
    }

    @Ignore
    public Alarm(int alarmId, String alarmTime, String triggerDate){
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.triggerDate = triggerDate;
        this.snoozeMode = false;
    }

    @Ignore
    public Alarm(int alarmId, String alarmTime, String ringtonePath, String triggerDate){
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.ringtonePath = ringtonePath;
        this.triggerDate = triggerDate;
        this.snoozeMode = false;
    }

    @Ignore
    public Alarm(int alarmId, String alarmTime, String ringtonePath, String repeatableDays, String triggerDate, boolean snoozeMode, String description, int snoozeTime, boolean vibration_on, boolean minigame_on, boolean alarm_on){
        this.alarmId = alarmId;
        this.alarmTime = alarmTime;
        this.ringtonePath = ringtonePath;
        this.repeatableDays = repeatableDays;
        this.triggerDate = triggerDate;
        this.snoozeMode = snoozeMode;
        this.description = description;
        this.snoozeTime = snoozeTime;
        this.vibration_on = vibration_on;
        this.minigame_on = minigame_on;
        this.alarm_on = alarm_on;
    }

    public void setAlarmId(int alarmId){
        this.alarmId = alarmId;
    }
    public int getAlarmId(){
        return this.alarmId;
    }

    public String getAlarmTime(){
        return alarmTime;
    }
    public void setAlarmTime(String alarmTime){
        this.alarmTime = alarmTime;
    }

    public String getRingtonePath(){
        return ringtonePath;
    }
    public void setRingtonePath(String ringtonePath){
        this.ringtonePath = ringtonePath;
    }

    public String getRepeatableDays(){
        return repeatableDays;
    }
    public void setRepeatableDays(String repeatableDays){
        this.repeatableDays = repeatableDays;
    }

    public String getTriggerDate(){
        return triggerDate;
    }
    public void setTriggerDate(String triggerDate){
        this.triggerDate = triggerDate;
    }

    public boolean isSnoozeMode(){
        return snoozeMode;
    }
    public void setSnoozeMode(boolean snoozeMode){
        this.snoozeMode = snoozeMode;
    }

    public int getSnoozeTime(){ return snoozeTime; }
    public void setSnoozeTime(int snoozeTime){ this.snoozeTime = snoozeTime; }

    public String getDescription(){ return description; }
    public void setDescription(String desc){ this.description = desc; }

    public boolean isVibration_on() { return vibration_on; }
    public void setVibration_on(boolean mode){ vibration_on = mode; }

    public boolean isMinigame_on() { return minigame_on; }
    public void setMinigame_on(boolean mode) { minigame_on = mode; }

    public boolean isAlarm_on() { return alarm_on; }
    public void setAlarm_on(boolean mode) { alarm_on = mode; }
}

