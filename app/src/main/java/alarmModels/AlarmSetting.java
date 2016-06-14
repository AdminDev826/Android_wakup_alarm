package alarmModels;

/**
 * Created by Alex on 6/12/2016.
 */
public class AlarmSetting {

    public static String strAlarmName = " ";
    public static String strAlarmTime = " ";
    public static String strAlarmPath = " ";
    public static int alarm_state = 0;
    public static String weekDays = " ";
    public static String alarm_repeats = " ";

    public static int[] week_flag = new int[7];
    public static int[] alarm_ids = new int[7];

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
}
