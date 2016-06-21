package app.alarmModels;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Alex on 6/12/2016.
 */
public class AlarmSetting {

    public static int alarm_win = 0;// 0:alarm list, 1: alarm edit, 2: alarm title enter, 3: alarm_sound List, 4: alarm_player
    public static final String LocalTmpPath = "my_alarm_path";

    public static String alarmID  = " ";
    public static String strAlarmName = "Wake Up";
    public static String strAlarmTime = " ";
    public static int alarm_index = 0;
    public static int alarm_state = 0;
    public static int noti_state = 1;
    public static String weekDays = " ";
    public static String alarm_repeats = " ";

    public static int[] week_flag = new int[7];
    public static int[] alarm_ids = new int[7];

    public static void init(){
        alarmID  = " ";
        strAlarmName = "Wake Up";
        strAlarmTime = " ";
        alarm_index = 0;
        alarm_state = 0;
        noti_state = 1;
        weekDays = " ";
        alarm_repeats = " ";
        week_flag = new int[7];
        alarm_ids = new int[7];
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

    public static String getAlarmRepeats(){
        String temp = "" + AlarmSetting.alarm_ids[0];
        for(int i = 1; i < 7; i++){
            temp += "," + AlarmSetting.alarm_ids[i];
        }
        alarm_repeats = temp;
        return temp;
    }
    public static int[] getAlarm_ids(){
        int[] temp = new int[7];
        String[] tmpAry = alarm_repeats.split(",");
        for(int i = 0; i < 7; i++){
            temp[i] = Integer.parseInt(tmpAry[i]);
        }
        alarm_ids = temp;
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
        currentSet.add("time:::" + strAlarmTime);
        currentSet.add("title:::" + strAlarmName);
        currentSet.add("alarm_index:::" + alarm_index);
        currentSet.add("alarm_state:::" + alarm_state);
        currentSet.add("noti_state:::" + noti_state);
        currentSet.add("weekdays:::" + getWeekdays());
        currentSet.add("alarm_repeats:::" + alarm_repeats);

        if(alarmID.equals(" ")){
            String setName = "Set"+size;
            editor.remove(setName);

            size = size+1;
            editor.putInt("Array_Size", size);
            editor.putStringSet(setName, currentSet);
        }else{
            editor.remove(alarmID);
            editor.putStringSet(alarmID, currentSet);
        }
        editor.commit();
        return true;
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
    private static AlarmItem getDataList(Set<String> hashSet, String alarm_id){

        String [] alarmAry = hashSet.toArray(new String[hashSet.size()]);
        String time = " ";
        String title = " ";
        int alarm_index = 0;
        int alarmState = 0;
        int notiState = 0;
        String weekdays = " ";
        String repeats = " ";

        for(int i = 0; i < alarmAry.length; i++){
            String[] keys = alarmAry[i].split(":::");
            switch(keys[0]){
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
                case "alarm_repeats" :
                    repeats = keys[1];
                    break;
                default:
                    break;
            }
        }
        AlarmItem item = new AlarmItem(alarm_id, title, time, alarm_index, alarmState, notiState, weekdays, getWeekString(weekdays), repeats);
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
