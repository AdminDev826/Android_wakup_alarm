package alarmModels;

/**
 * Created by Alex on 6/14/2016.
 */
public class AlarmItem {

    public  String strAlarmName = " ";
    public  String strAlarmTime = " ";
    public  String strAlarmPath = " ";
    public  int alarm_state = 0;
    public  String weekDays = " ";
    public  String alarm_repeats = " ";

    public  int[] week_flag = new int[7];
    public  int[] alarm_ids = new int[7];

    public AlarmItem(String name, String alarm_time, String path, int state, String week_days, String repeats){
        strAlarmName = name;
        strAlarmTime = alarm_time;
        strAlarmPath = path;
        alarm_state = state;
        weekDays = week_days;
        alarm_repeats = repeats;
    }


    public  String getWeekdays(){
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

    public  int[] getWeek_flag(){
        String[] tempAry = weekDays.split(",");
        int[] temp = new int[7];
        Boolean bool = false;
        for(int i = 0; i < 7; i++) {
            for (int j = 0; j < tempAry.length; j++) {
                if(i == Integer.parseInt(tempAry[j]))
                    temp[i] = 1;
                else
                    temp[i] = 0;
            }
        }
        week_flag = temp;
        return temp;
    }

    public  String getAlarmRepeats(){
        String temp = "" + AlarmSetting.alarm_ids[0];
        for(int i = 1; i < 7; i++){
            temp += "," + AlarmSetting.alarm_ids[i];
        }
        alarm_repeats = temp;
        return temp;
    }
    public  int[] getAlarm_ids(){
        int[] temp = new int[7];
        String[] tmpAry = alarm_repeats.split(",");
        for(int i = 0; i < 7; i++){
            temp[i] = Integer.parseInt(tmpAry[i]);
        }
        alarm_ids = temp;
        return temp;
    }
    public  Boolean checkWeek(){
        for(int i = 0; i < 7; i++){
            if(week_flag[i] == 1)
                return true;
        }
        return false;
    }
}
