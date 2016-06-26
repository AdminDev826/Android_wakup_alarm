package app.alarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import app.alarmModels.AlarmSetting;
import app.listen.MusicListFragment;
import app.tabsample.R;


public class AlarmEditFragment extends Fragment {

    private TimePicker alarm;
    private TextView txtAlamName;
    private TextView alarmMusic;
    private TextView[] weekdays_lavel = new TextView[7];
    private int[] week_days;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alarm_edit_fragment, container, false);
        alarm = (TimePicker) view.findViewById(R.id.alarmTimer);
        txtAlamName = (TextView) view.findViewById(R.id.txt_alarm_label);
        alarmMusic = (TextView) view.findViewById(R.id.txtMusicName);
        RelativeLayout alarm_title_layout = (RelativeLayout) view.findViewById(R.id.alarm_label_layout);
        RelativeLayout alarm_Music_layout = (RelativeLayout) view.findViewById(R.id.music_text_layout);
        week_days = AlarmSetting.getWeek_flag();

        txtAlamName.setText(AlarmSetting.strAlarmName);
        alarmMusic.setText(getMusicName());
        if (AlarmSetting.strAlarmTime.length() > 1){
            setCurrentAlarmTime();
        }
        alarm_title_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmSetting.alarm_win = 2;
                AlarmMainActivity.txtAdd.setVisibility(View.INVISIBLE);
                AlarmMainActivity.txtBack.setVisibility(View.VISIBLE);

                Fragment frag = new AlarmTitleInputFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.alarm_fragment, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        alarm_Music_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlarmSetting.alarm_win = 3;
                AlarmMainActivity.txtAdd.setVisibility(View.INVISIBLE);
                AlarmMainActivity.txtBack.setVisibility(View.VISIBLE);

                Fragment frag = new MusicListFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.alarm_fragment, frag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        RelativeLayout weekDays_layout = (RelativeLayout)view.findViewById(R.id.weekday_layout);
        for (int i = 0; i < weekDays_layout.getChildCount(); i++) {
            if(weekDays_layout.getChildAt(i).getClass() == TextView.class){
                weekdays_lavel[i] = (TextView)weekDays_layout.getChildAt(i);
                final int finalI = i;
                weekdays_lavel[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        weekdays_lavel[finalI].setVisibility(View.INVISIBLE);
//                        weekdays_active[finalI].setVisibility(View.VISIBLE);
                        float yy = weekdays_lavel[finalI].getY();
                        if(week_days[finalI] == 0){
                            AlarmSetting.week_flag[finalI] = 1;
                            week_days[finalI] = 1;
                            weekdays_lavel[finalI].setY(yy - 70);
                            weekdays_lavel[finalI].setTextColor(getResources().getColor(R.color.white));
                        }else{
                            AlarmSetting.week_flag[finalI] = 0;
                            weekdays_lavel[finalI].setY(yy + 70);
                            weekdays_lavel[finalI].setTextColor(getResources().getColor(R.color.text_color));
                            week_days[finalI] = 0;
                        }
                        setDoneColor();
                    }
                });
            }
            float yy = weekdays_lavel[i].getY();
            if (week_days[i] == 1) {
                weekdays_lavel[i].setY(yy - 70);
                weekdays_lavel[i].setTextColor(getResources().getColor(R.color.white));
            } else {
               // weekdays_lavel[i].setY(yy + 70);
                weekdays_lavel[i].setTextColor(getResources().getColor(R.color.text_color));
            }
            setDoneColor();
        }

        alarm.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // TODO Auto-generated method stub
                AlarmSetting.strAlarmTime = hourOfDay + ":" + minute;

//                Toast.makeText(getActivity(), "Time is " + hourOfDay + " : "
//                        + minute, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    void setDoneColor(){
        for(int i = 0; i < 7; i++){
            if(week_days[i] == 1){
                AlarmMainActivity.txtAdd.setTextColor(getResources().getColor(R.color.white));
                return;
            }
        }
        AlarmMainActivity.txtAdd.setTextColor(getResources().getColor(R.color.disable_color));
    }
    void setCurrentAlarmTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = null;
        try{
            date = sdf.parse(AlarmSetting.strAlarmTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        alarm.setCurrentHour(c.get(Calendar.HOUR_OF_DAY));
        alarm.setCurrentMinute(c.get(Calendar.MINUTE));
    }
    String getMusicName(){
        String[] temp = getResources().getStringArray(R.array.alarm_list);
        return temp[AlarmSetting.alarm_index];
    }
}