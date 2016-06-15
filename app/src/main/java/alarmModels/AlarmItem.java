package alarmModels;

public class AlarmItem {

    public String alarmID = " ";
    public  String strAlarmName = " ";
    public  String strAlarmTime = " ";
    public  String strAlarmPath = " ";
    public  int alarm_state = 0;
    public  int noti_state = 0;
    public  String weekDays = " ";
    public String weekString = " ";
    public  String alarm_repeats = " ";

    public AlarmItem(String id, String name, String alarm_time, String path, int alarmState, int notiState, String week_days, String week_string, String repeats){
        alarmID = id;
        strAlarmName = name;
        strAlarmTime = alarm_time;
        strAlarmPath = path;
        alarm_state = alarmState;
        noti_state = notiState;
        weekDays = week_days;
        weekString = week_string;
        alarm_repeats = repeats;
    }
}
