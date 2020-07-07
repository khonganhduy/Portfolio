package sjsu.android.alarmclockplusplus;

import android.content.Context;
import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.core.util.Preconditions.checkNotNull;
import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("personal.android.sandbox", appContext.getPackageName());
    }

    @Test
    public void alarmCreate(){
        Alarm alarm = new Alarm((int)Math.round(Math.random() * 100000), "6:00 AM",
                null, null, null, false, null,1, false, false, false);
        assertNotNull(alarm);
        assertEquals(alarm.getAlarmTime(), "6:00 AM");
        assertNull(alarm.getRingtonePath());
        assertNull(alarm.getRepeatableDays());
        assertNull(alarm.getTriggerDate());
        assertFalse(alarm.isSnoozeMode());
        assertNull(alarm.getDescription());
        assertEquals(alarm.getSnoozeTime(), 1);
        assertFalse(alarm.isVibration_on());
        assertFalse(alarm.isMinigame_on());
        assertFalse(alarm.isAlarm_on());
    }

    @Test
    public void alarmSetValues(){
        Alarm alarm = new Alarm((int)Math.round(Math.random() * 100000), "6:00 AM",
                null, null, null, false, null,1, false, false, false);
        alarm.setAlarmId(1);
        alarm.setAlarmTime("10:00 PM");
        alarm.setRingtonePath("storage/0914-2F11/Music");
        alarm.setRepeatableDays("M T W TH F");
        alarm.setTriggerDate("2/20/20");
        alarm.setSnoozeMode(true);
        alarm.setDescription("description");
        alarm.setSnoozeTime(2);
        alarm.setVibration_on(true);
        alarm.setMinigame_on(true);
        alarm.setAlarm_on(true);

        assertNotNull(alarm);
        assertEquals(alarm.getAlarmTime(), "10:00 PM");
        assertEquals(alarm.getRingtonePath(), "storage/0914-2F11/Music");
        assertEquals(alarm.getRepeatableDays(), "M T W TH F");
        assertEquals(alarm.getTriggerDate(), "2/20/20");
        assertTrue(alarm.isSnoozeMode());
        assertEquals(alarm.getDescription(), "description");
        assertEquals(alarm.getSnoozeTime(), 2);
        assertTrue(alarm.isVibration_on());
        assertTrue(alarm.isMinigame_on());
        assertTrue(alarm.isAlarm_on());
    }

    @Rule
    public IntentsTestRule<AlarmListDisplayActivity> intentsTestRule =
            new IntentsTestRule<>(AlarmListDisplayActivity.class);

    @Test
    public void testAlarmClockIntent(){
        Alarm alarm = new Alarm((int)Math.round(Math.random() * 100000), "6:00 AM",
                null, null, null, false, null,1, false, false, false);
        alarm.setAlarmId(1);
        alarm.setAlarmTime("10:00 PM");
        alarm.setRingtonePath("storage/0914-2F11/Music");
        alarm.setRepeatableDays("M T W TH F");
        alarm.setTriggerDate("2/20/20");
        alarm.setSnoozeMode(true);
        alarm.setDescription("description");
        alarm.setSnoozeTime(2);
        alarm.setVibration_on(true);
        alarm.setMinigame_on(true);
        alarm.setAlarm_on(true);

        Intent i = new Intent();
        i.putExtra(AlarmListDisplayActivity.ALARM_RING_PATH, alarm.getRingtonePath());
        i.putExtra(AlarmListDisplayActivity.ALARM_SNOOZE_TIME, alarm.getSnoozeTime());
        i.putExtra(AlarmListDisplayActivity.ALARM_ID, alarm.getAlarmId());
        i.putExtra(AlarmListDisplayActivity.ALARM_VIBRATION, alarm.isVibration_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_MINIGAME, alarm.isMinigame_on());
        i.putExtra(AlarmListDisplayActivity.ALARM_REPEAT_DAYS, alarm.getRepeatableDays());

        assertEquals(i.getExtras().getString(AlarmListDisplayActivity.ALARM_RING_PATH), "storage/0914-2F11/Music");
        assertEquals(i.getExtras().getString(AlarmListDisplayActivity.ALARM_REPEAT_DAYS), "M T W TH F");
        assertEquals(i.getExtras().getInt(AlarmListDisplayActivity.ALARM_SNOOZE_TIME), 2);
        assertTrue(i.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_VIBRATION));
        assertTrue(i.getExtras().getBoolean(AlarmListDisplayActivity.ALARM_MINIGAME));
    }


    @Test
    public void testAddAlarmUI() {
        onView(withId(R.id.addAlarmButton)).perform(click()).check(matches(isDisplayed()));
    }

    @Test
    public void testMainClockUI(){
        onView(withId(R.id.textClock)).check(matches(isDisplayed()));
    }

    @Test
    public void testClickAlarmUI(){
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.time_picker)).check(matches(isDisplayed()));
    }

    @Test
    public void testSoundSelect(){
        closeSoftKeyboard();
        onView(withId(R.id.recyclerview)).check(matches(isDisplayed()));
        onView(withId(R.id.recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.sound_selector)).perform(scrollTo()).perform(click());
        onView(withId(R.id.sound_selector_recycler)).check(matches(isDisplayed()));
    }

    @Test
    public void testToggleAlarm(){
        onView(withId(R.id.recyclerview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.onOffSwitch)));

    }

}
