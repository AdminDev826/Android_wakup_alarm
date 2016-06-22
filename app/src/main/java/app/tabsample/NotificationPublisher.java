package app.tabsample;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import app.alarmModels.AlarmItem;
import app.alarmModels.AlarmSetting;

public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String alarmID = "alarm_id";
    public static String NOTIFICATION = "notification";
    final public static String ONE_TIME = "onetime";

    public void onReceive(Context context, Intent intent) {
        SimpleDateFormat df = new SimpleDateFormat("EEE");
        String weekday = df.format(new Date());

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String alarm_id = intent.getStringExtra(alarmID);

        AlarmItem alarmItem = AlarmSetting.getCurrentAlarmData(context,alarm_id);
        if(alarmItem.weekString.contains(weekday))
            notificationManager.notify(id, notification);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
        wl.acquire();

        if(alarmItem.noti_state != 0) {
            Bundle extras = intent.getExtras();
            StringBuilder msgStr = new StringBuilder();

            if (extras != null && extras.getBoolean(ONE_TIME, Boolean.FALSE)) {
                //Make sure this intent has been sent by the one-time timer button.
                msgStr.append("One time Timer : ");
            }

            msgStr.append(alarmItem.strAlarmName);

            Toast.makeText(context, msgStr, Toast.LENGTH_LONG).show();
        }
        wl.release();
    }
    int getDayofWeek(String day){
        if(day.equals("Mon"))
            return 0;
        else if(day.equals("Tue"))
            return 1;
        else if(day.equals("Wed"))
            return 2;
        else if(day.equals("Thu"))
            return 3;
        else if(day.equals("Fri"))
            return 4;
        else if(day.equals("Sat"))
            return 5;
        else return 6;
    }
    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 5 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 1000 * 5 , pi);
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public void setOnetimeTimer(Context context){
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationPublisher.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);
    }
}