package sjsu.android.alarmclockplusplus;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

// View model for the MVC design pattern
public class AlarmViewModel extends AndroidViewModel {
    private AlarmRepository alarmRepository;
    private LiveData<List<Alarm>> alarmList;

    public AlarmViewModel(Application app){
        super(app);
        alarmRepository = new AlarmRepository(app);
        alarmList = alarmRepository.getAlarmList();
    }

    LiveData<List<Alarm>> getAlarmList(){
        return alarmList;
    }

    void insert(Alarm alarm){
        alarmRepository.insert(alarm);
    }

    void delete(Alarm alarm){
        alarmRepository.delete(alarm);
    }

    void update(int id, boolean alarmOn){
        alarmRepository.update(id, alarmOn);
    }

    void update(int id, String time, String path, String days, String date, boolean snooze, String desc, int snooze_time, boolean vibration, boolean minigame, boolean alarmOn){
        alarmRepository.update(id, time, path, days, date, snooze, desc, snooze_time, vibration, minigame, alarmOn);
    }


}
