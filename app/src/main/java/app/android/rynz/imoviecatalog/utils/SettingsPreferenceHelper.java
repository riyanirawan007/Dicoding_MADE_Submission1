package app.android.rynz.imoviecatalog.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SettingsPreferenceHelper
{
    private static final String PREFERENCE_NAME = "iMovieSettingPreference";
    private static final String KEY_UPCOMING_NOTIFIER_STATUS = "setting_upcoming_notifier_stat";
    private static final String KEY_DAILY_REMIDER_TIME = "daily_remider_time";
    private static final String KEY_DAILY_REMIDER_MSG = "daily_remider_msg";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SettingsPreferenceHelper(Context context)
    {
        preferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void setUpComingNotifierStat(@NonNull boolean isEnabled)
    {
        editor.putBoolean(KEY_UPCOMING_NOTIFIER_STATUS, isEnabled);
        editor.commit();
    }

    public boolean isUpComingNotifierEnabled()
    {
        return preferences.getBoolean(KEY_UPCOMING_NOTIFIER_STATUS, false);
    }


    public void setDailyReminder(@NonNull String time, @NonNull String message)
    {
        editor.putString(KEY_DAILY_REMIDER_TIME, time);
        editor.putString(KEY_DAILY_REMIDER_MSG, message);
        editor.commit();
    }

    public String getDailyReminderTime()
    {
        return preferences.getString(KEY_DAILY_REMIDER_TIME, null);
    }

    public String getDailyReminderMsg()
    {
        return preferences.getString(KEY_DAILY_REMIDER_MSG, null);
    }

    public void resetDailyReminder()
    {
        editor.remove(KEY_DAILY_REMIDER_TIME);
        editor.remove(KEY_DAILY_REMIDER_MSG);
        editor.commit();
    }

    public void resetUpComingStat()
    {
        editor.remove(KEY_UPCOMING_NOTIFIER_STATUS);
        editor.commit();
    }

    public void resetAll()
    {
        editor.clear();
        editor.commit();
    }
}
