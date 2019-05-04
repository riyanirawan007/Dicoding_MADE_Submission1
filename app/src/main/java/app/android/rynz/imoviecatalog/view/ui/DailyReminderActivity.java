package app.android.rynz.imoviecatalog.view.ui;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;

import app.android.rynz.imoviecatalog.R;
import app.android.rynz.imoviecatalog.broadcaster.AlarmReceiver;
import app.android.rynz.imoviecatalog.utils.SettingsPreferenceHelper;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DailyReminderActivity extends AppCompatActivity
{
    private SettingsPreferenceHelper settingsPreference;
    private AlarmReceiver alarmReceiver;
    private View currentView;
    @BindView(R.id.btn_set_daily_reminder)
    Button btnSetReminder;
    @BindView(R.id.time_pick_daily_reminder)
    TimePicker timePicker;
    @BindView(R.id.btn_cancel_daily_reminder)
    Button btnCancelReminder;

    @OnClick(R.id.btn_set_daily_reminder)
    public void setDailyReminder()
    {
        String time;
        String msg = getResources().getString(R.string.notification_daily_reminder_msg);
        if (Build.VERSION.SDK_INT >= 23)
        {
            time = String.valueOf(timePicker.getHour() + ":" + timePicker.getMinute());
        } else
        {
            time = String.valueOf(timePicker.getCurrentHour() + ":" + timePicker.getCurrentMinute());
        }
        settingsPreference.setDailyReminder(time, msg);
        alarmReceiver.setDailyAlarm(currentView, this, AlarmReceiver.TYPE_DAILY_REMINDER, time, msg);
        btnCancelReminder.setEnabled(true);
        btnSetReminder.setEnabled(false);
    }

    @OnClick(R.id.btn_cancel_daily_reminder)
    public void cancelDailyReminder()
    {
        settingsPreference.resetDailyReminder();
        alarmReceiver.cancelAlarm(currentView, this, AlarmReceiver.TYPE_DAILY_REMINDER);
        btnSetReminder.setEnabled(true);
        btnCancelReminder.setEnabled(false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_reminder);
        ButterKnife.bind(this);
        settingsPreference = new SettingsPreferenceHelper(this);
        alarmReceiver = new AlarmReceiver();
        currentView = getWindow().getDecorView().getRootView();
        setUpTimePicker();
    }


    public void setUpTimePicker()
    {
        //load time daily reminder
        timePicker.setIs24HourView(true);
        String reminderTime = settingsPreference.getDailyReminderMsg();
        int mhour, mMinute;
        mhour = Calendar.getInstance().getTime().getHours();
        mMinute = Calendar.getInstance().getTime().getMinutes();

        if (reminderTime != null)
        {
            String time[] = settingsPreference.getDailyReminderTime().split(":");
            mhour = Integer.valueOf(time[0]);
            mMinute = Integer.valueOf(time[1]);

            btnSetReminder.setEnabled(false);
            btnCancelReminder.setEnabled(true);
        } else
        {
            btnSetReminder.setEnabled(true);
            btnCancelReminder.setEnabled(false);
        }

        if (Build.VERSION.SDK_INT >= 23)
        {
            timePicker.setHour(mhour);
            timePicker.setMinute(mMinute);
        } else
        {
            timePicker.setCurrentHour(mhour);
            timePicker.setCurrentMinute(mMinute);
        }

    }
}
