package app.android.rynz.imoviecatalog.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import app.android.rynz.imoviecatalog.R;

public class EasyNotif
{
    private Context context;
    private NotificationManager notificationManager;
    private String title = null, message = null;
    private int notifID = 0;
    private int iconResourceID = 0, colorResourceID = 0;
    private long[] vibratePattern;
    private Uri notifSound = null;
    private PendingIntent pendingIntent;
    private boolean autoCancel = true;

    public EasyNotif with(Context context, String title, String message, int notifID)
    {
        this.context = context;
        this.notifID = notifID;
        this.title = title;
        this.message = message;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return this;
    }

    public EasyNotif setSmallIcon(int resourceID)
    {
        iconResourceID = resourceID;
        return this;
    }

    public EasyNotif setSound(Uri uri)
    {
        notifSound = uri;
        return this;
    }

    public EasyNotif setColor(int resourceID)
    {
        iconResourceID = resourceID;
        return this;
    }

    public EasyNotif setVibratePattern(long[] vibratePattern)
    {
        this.vibratePattern = vibratePattern;
        return this;
    }

    public EasyNotif setAutoCancel(boolean autoCancel)
    {
        this.autoCancel = autoCancel;
        return this;
    }

    public EasyNotif setContentIntent(PendingIntent pendingIntent)
    {
        this.pendingIntent = pendingIntent;
        return this;
    }

    public void show()
    {
        if (iconResourceID == 0)
        {
            iconResourceID = R.mipmap.ic_launcher;
        }
        if (notifSound == null)
        {
            notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        if (colorResourceID == 0)
        {
            colorResourceID = ContextCompat.getColor(context, android.R.color.black);
        }
        if (vibratePattern == null)
        {
            vibratePattern = new long[]{1000, 1000, 1000, 1000, 1000};
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(iconResourceID)
                .setColor(colorResourceID)
                .setSound(notifSound)
                .setVibrate(vibratePattern)
                .setAutoCancel(autoCancel);

        if (pendingIntent != null)
        {
            notificationBuilder.setContentIntent(pendingIntent);
        }

        if (notificationManager != null)
        {
            notificationManager.notify(notifID, notificationBuilder.build());
        }
    }
}
