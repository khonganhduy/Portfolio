package sjsu.android.alarmclockplusplus;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

// Class for the creation and return of the database object based on Room API standards
@Database(entities={Alarm.class}, version = 4, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AlarmDAO alarmDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 3;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);
    static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "alarm_database")
                            .addCallback(dbCallback).fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback dbCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }

        public void onCreate(@NonNull SupportSQLiteDatabase db){
            super.onCreate(db);
            databaseWriteExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    int[] alarmId = {12321, 12322, 12323, 12324, 12325};
                    String[] defaultAlarmTime = {"7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM"};
                    AlarmDAO dao = INSTANCE.alarmDao();
                    for(int i = 0; i < 5; i++) {
                        Alarm alarm = new Alarm(alarmId[i], defaultAlarmTime[i], null, "M T W Th F Sa Su",
                                null, false, null,1, false, false, false);
                        dao.insertAlarm(alarm);
                    }
                }
            });
        }
    };
}