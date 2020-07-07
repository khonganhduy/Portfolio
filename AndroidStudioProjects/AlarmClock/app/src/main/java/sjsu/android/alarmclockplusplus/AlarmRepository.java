package sjsu.android.alarmclockplusplus;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

// Holds reference to alarm data access object to perform database operations
public class AlarmRepository {
    private AlarmDAO dao;
    private LiveData<List<Alarm>> alarmList;

    AlarmRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        dao = db.alarmDao();
        alarmList = dao.getAll();
    }

    LiveData<List<Alarm>> getAlarmList(){
        return alarmList;
    }

    void insert(final Alarm alarm){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.insertAlarm(alarm);
            }
        });
    }

    void delete(final Alarm alarm){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAlarm(alarm);
            }
        });
    }

    void update(final int alarmId, final boolean alarmOn){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.update(alarmId, alarmOn);
            }
        });
    }

    void update(final int alarmId, final String time, final String path, final String days, final String date, final boolean snooze,
                final String desc, final int snooze_time, final boolean vibration, final boolean minigame, final boolean alarmOn){
        AppDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                dao.update(alarmId, time, path, days, date, snooze, desc, snooze_time, vibration, minigame, alarmOn);
            }
        });
    }
}
