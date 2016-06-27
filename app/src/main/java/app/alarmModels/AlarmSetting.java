package app.alarmModels;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import app.main.NotificationPublisher;

/**
 * Created by Alex on 6/12/2016.
 */
public class AlarmSetting {

    public static int alarm_win = 0;// 0:alarm list, 1: alarm edit, 2: alarm title enter, 3: alarm_sound List, 4: alarm_player, 5: setting_about
    public static boolean alarm_update = false;
    public static final String LocalTmpPath = "my_alarm_path";

    public static String alarmID  = " ";
    public static int alarm_identifier = 0;
    public static String strAlarmName = "Wake Up";
    public static String strAlarmTime = " ";
    public static int alarm_index = 0;
    public static int alarm_state = 1;
    public static int noti_state = 1;
    public static String weekDays = " ";
    public static int[] week_flag = new int[7];

    public static void init(){
        alarm_update = false;
        alarmID  = " ";
        alarm_identifier = 0;
        strAlarmName = "Wake Up";
        strAlarmTime = getCurrentTime();
        alarm_index = 0;
        alarm_state = 1;
        noti_state = 1;
        weekDays = " ";
        week_flag = new int[7];
    }
    private static String getCurrentTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String curdate = sdf.format(new Date());
        String[] tmpAry = curdate.split(":");
        String temp = "";
        int temp1 = Integer.valueOf(tmpAry[0]);
        int temp2 = Integer.valueOf(tmpAry[1]);
        temp = temp1 + ":" + temp2;
        return temp;
    }
    public static String getWeekdays(){
        String temp = "";
        Boolean bool = false;
        for(int i = 0; i < 7; i++){
            if(week_flag[i] == 1) {
                if(bool == false) temp = i + "";
                else temp += "," + i;
                bool = true;
            }
        }
        weekDays = temp;
        return temp;
    }

    public static int[] getWeek_flag(){
        if(weekDays.equals(" ")){
            return week_flag;
        }
        int[] temp = new int[7];
        String[] tempAry = weekDays.split(",");
        for(int i = 0; i < 7; i++) {
            for (int j = 0; j < tempAry.length; j++) {
                if(i == Integer.parseInt(tempAry[j])) {
                    temp[i] = 1;
                    i++;
                }else
                    temp[i] = 0;
            }
        }
        week_flag = temp;
        return temp;
    }
    public static Boolean checkWeek(){
        for(int i = 0; i < 7; i++){
            if(week_flag[i] == 1)
                return true;
        }
        return false;
    }
    public static void setAlarm(AlarmItem alarm){
        alarmID = alarm.alarmID;
        alarm_identifier = alarm.alarm_identifier;
        strAlarmName = alarm.strAlarmName;
        strAlarmTime = alarm.strAlarmTime;
        alarm_index = alarm.alarm_index;
        alarm_state = alarm.alarm_state;
        noti_state = alarm.noti_state;
        weekDays = alarm.weekDays;
        week_flag = getWeek_flag();
    }

    public static Boolean saveAlarm(Context context){
        SharedPreferences sharedPreference = context.getSharedPreferences(LocalTmpPath, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        int size = sharedPreference.getInt("Array_Size", 0);

        Set<String> currentSet = new HashSet<String>();
        currentSet.add("identifier:::" + alarm_identifier);
        currentSet.add("time:::" + strAlarmTime);
        currentSet.add("title:::" + strAlarmName);
        currentSet.add("alarm_index:::" + alarm_index);
        currentSet.add("alarm_state:::" + alarm_state);
        currentSet.add("noti_state:::" + noti_state);
        currentSet.add("weekdays:::" + getWeekdays());

        if(alarmID.equals(" ")){
            alarmID = "Set"+size;
            editor.remove(alarmID);

            size = size+1;
            editor.putInt("Array_Size", size);
            editor.putStringSet(alarmID, currentSet);
        }else{
            editor.remove(alarmID);
            editor.putStringSet(alarmID, currentSet);
        }
        editor.commit();
        return true;
    }

    public static void deleteAlarm(Context context, AlarmItem alarmItem){
        String alarm_id = "";
        String new_id = "";
        boolean bool = false;
        cancelAlarm(context, alarmItem.alarm_identifier);
        SharedPreferences sharedPreference = context.getSharedPreferences(LocalTmpPath, 0);
        SharedPreferences.Editor editor = sharedPreference.edit();
        int size = sharedPreference.getInt("Array_Size", 0);
        for(int i = 0; i < size; i++){
            alarm_id = "Set" + i;
            if(bool == true){
                new_id = "Set" + (i - 1);
                Set<String> hashSet = sharedPreference.getStringSet(alarm_id, null);
                editor.putStringSet(new_id, hashSet);
                editor.remove(alarm_id);
            }
            if(alarm_id.equals(alarmItem.alarmID)){
                editor.remove(alarmID);
                bool = true;
            }
        }
        if(bool == true)
            editor.putInt("Array_Size", (size - 1));
        editor.commit();
    }

    private static void cancelAlarm(Context context, int identifier)
    {
        Intent intent = new Intent(context, NotificationPublisher.class);
        PendingIntent sender = PendingIntent.getBroadcast(context,identifier, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }

    public static List getAlarmData(Context context){
        List<AlarmItem> alarmList = new ArrayList<>();
        String alarm_id = " ";
        SharedPreferences sharedPreference = context.getSharedPreferences(LocalTmpPath, 0);
        int size = sharedPreference.getInt("Array_Size", 0);
        for (int i = 0; i < size; i++) {
            alarm_id = "Set" + i;
            Set<String> hashSet = sharedPreference.getStringSet(alarm_id, null);
            alarmList.add(getDataList(hashSet, alarm_id));
        }
        return alarmList;
    }

    public static AlarmItem getCurrentAlarmData(Context context, String alarm_id){
        AlarmItem alarmItem;
        SharedPreferences sharedPreference = context.getSharedPreferences(LocalTmpPath, 0);
        Set<String> hashSet = sharedPreference.getStringSet(alarm_id, null);
        alarmItem = getDataList(hashSet, alarm_id);
        return alarmItem;
    }

    private static AlarmItem getDataList(Set<String> hashSet, String alarm_id){

        String [] alarmAry = hashSet.toArray(new String[hashSet.size()]);
        int identifier = 0;
        String time = " ";
        String title = " ";
        int alarm_index = 0;
        int alarmState = 0;
        int notiState = 0;
        String weekdays = " ";

        for(int i = 0; i < alarmAry.length; i++){
            String[] keys = alarmAry[i].split(":::");
            switch(keys[0]){
                case "identifier" :
                    identifier = Integer.valueOf(keys[1]);
                    break;
                case "time" :
                    time = keys[1];
                    break;
                case "title" :
                    title = keys[1];
                    break;
                case "alarm_index" :
                    alarm_index = Integer.valueOf(keys[1]);
                    break;
                case "alarm_state" :
                    alarmState = keys[1].length() > 0 ? Integer.parseInt(keys[1]) : 0;
                    break;
                case "noti_state" :
                    notiState = keys[1].length() > 0 ? Integer.parseInt(keys[1]) : 0;
                    break;
                case "weekdays" :
                    weekdays = keys[1];
                    break;
                default:
                    break;
            }
        }
        AlarmItem item = new AlarmItem(alarm_id, identifier, title, time, alarm_index, alarmState, notiState, weekdays, getWeekString(weekdays));
        return item;
    }
    public static String getWeekString(String weekDays){
        String[] temp = weekDays.split(",");
        String weekString;
        weekString = getDayofWeek(temp[0]);
        for(int i = 1; i < temp.length; i++){
            weekString += " " + getDayofWeek(temp[i]);
        }
        return weekString;
    }
    public static String getDayofWeek(String tt){
        String temp = " ";
        int t = Integer.parseInt(tt);
        if(t == 0) temp = "Mon";
        else if(t == 1) temp = "Tue";
        else if(t == 2) temp = "Wed";
        else if(t == 3) temp = "Thu";
        else if(t == 4) temp = "Fri";
        else if(t == 5) temp = "Sat";
        else if(t == 6) temp = "Sun";
        return temp;
    }
}
