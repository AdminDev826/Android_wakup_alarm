package app.tabsample;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingDeque;

import alarmModels.AlarmSetting;

/**
 * @author Adil Soomro
 *
 */
public class AlarmMainActivity extends Activity {
    private boolean bool_edit_flag;
    public static final String LocalTmpPath = "my_alarm_path";
    private SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm aa");

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        initSharedReference();//clear all sharedpreferences====================================================


        setContentView(R.layout.alarm_main);
        bool_edit_flag = true;


        final TextView txtAdd = (TextView)findViewById(R.id.txtAdd);
        final TextView txtBack = (TextView)findViewById(R.id.txtBack);
        txtAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment frag;
                if(bool_edit_flag == false){
                    if(!checkAlarmParams()) {
                        Toast.makeText(getApplicationContext(), "Please correct insert alarm informations!!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    txtBack.setText("");
                    txtAdd.setText("Add");
                    bool_edit_flag = true;
                    addAlarm();
                    frag = new AlarmListFragment();

                }else {
                    bool_edit_flag = false;
                    txtBack.setText("Back");
                    txtAdd.setText("Done");
                    frag = new AlarmEditFragment();

                }
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.alarm_fragment, frag);
                fragmentTransaction.commit();
            }
        });
        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bool_edit_flag == false){
                    bool_edit_flag = true;
                    txtBack.setText("");
                    txtAdd.setText("Add");
                    Fragment frag = new AlarmListFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.alarm_fragment,frag);
                    fragmentTransaction.commit();
                }
            }
        });
    }
    private int scheduleNotification(Notification notification, long mili) {

        final int _id = (int) System.currentTimeMillis();

        Intent notificationIntent = new Intent(this, NotificationPublisher.class);

        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, _id);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, _id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = mili;
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, futureInMillis, pendingIntent);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, futureInMillis, 1000 * 5, pendingIntent);//1000 * 60 * 60 * 24 * 7
        return _id;
    }

    private Notification getNotification(String title, String content) {
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.icon);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        builder.setAutoCancel(true);
        return builder.build();
    }

    private void addAlarm(){

        int flag = 1;
        AlarmSetting.alarm_state = flag;

        String[] intervals = getIntervals();
        for(int i = 0; i < 7; i++){
            if(intervals[i] != ""){
                ParsePosition pos = new ParsePosition(0);
                Date date1 = mFormatter.parse(intervals[i] + " " + AlarmSetting.strAlarmTime, pos);
                System.out.println("in milliseconds: " + (int)date1.getTime());
                int s_id = scheduleNotification(getNotification("my alarm title (Wake up)", "alarm content" + AlarmSetting.strAlarmTime), date1.getTime());
                AlarmSetting.alarm_ids[i] = s_id;
            }else{
                AlarmSetting.alarm_ids[i] = 0;
            }
        }

//        Date date1 = mFormatter.parse("2016-06-13 " + AlarmSetting.strAlarmTime, pos);
//        System.out.println("in milliseconds: " + (int)date1.getTime());
//        scheduleNotification(getNotification("my alarm title (Wake up)", "alarm content" + AlarmSetting.strAlarmTime), date1.getTime());
        saveAlarms();
    }

    void saveAlarms(){
        SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences(LocalTmpPath, 0);
        String tmpPath;
        tmpPath = sharedPreference.getString("audio_path", "");

        int size = sharedPreference.getInt("Array_Size", 0);

        Set<String> currentSet = new HashSet<String>();
        currentSet.add("time:::"+AlarmSetting.strAlarmTime);

        currentSet.add("title:::"+"my test alarm" + size);
        currentSet.add("path:::" + tmpPath + "temp path");
        currentSet.add("alarm_state:::" + AlarmSetting.alarm_state);
        currentSet.add("weekdays:::" + AlarmSetting.weekDays);
        currentSet.add("alarm_repeats:::" + AlarmSetting.getAlarmRepeats());

        SharedPreferences.Editor editor = sharedPreference.edit();
        String setName = "Set"+size;
        editor.remove(setName);
        size = size+1;
        editor.putInt("Array_Size", size);

        editor.putStringSet(setName, currentSet);
        // Commit the edits!
        editor.commit();
    }

    String[] getIntervals(){
        SimpleDateFormat df;
        Date today = new Date();
//        int[] weekflags = AlarmSetting.getWeek_flag();
        String[] dateAry = new String[7];

        df = new SimpleDateFormat("yyyy-MM");
        String YM = df.format(today);
        df = new SimpleDateFormat("dd");
        String day = df.format(today);
        df = new SimpleDateFormat("EEE");
        String weekday = df.format(today);
        int curWeek = getDayofWeek(weekday);
        for(int i = 0; i < 7; i++){
            if(AlarmSetting.week_flag[i] == 1){
                if(curWeek > i){
                    dateAry[i] = YM + "-" + (Integer.parseInt(day) + 7 - curWeek + i);
                }else{
                    dateAry[i] = YM + "-" + (Integer.parseInt(day) - curWeek + i);
                }
            }else{
                dateAry[i] = "";
            }
        }
        return dateAry;
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
    void initSharedReference(){
        SharedPreferences sharedPreference = getApplicationContext().getSharedPreferences(LocalTmpPath, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        editor.clear();
        editor.commit();
    }
    Boolean checkAlarmParams(){
        if(AlarmSetting.strAlarmName == " " || AlarmSetting.strAlarmTime == " "|| !AlarmSetting.checkWeek())
            return false;
        else
            return true;
    }
}
