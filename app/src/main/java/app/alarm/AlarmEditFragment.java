package app.alarm;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
    private String alarmTime;
    private TextView alarmMusic;
    private TextView[] weekdays_active = new TextView[7];
    private TextView[] weekdays_lavel = new TextView[7];

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.alarm_edit_fragment, container, false);
        alarm = (TimePicker) view.findViewById(R.id.alarmTimer);
        txtAlamName = (TextView) view.findViewById(R.id.txt_alarm_label);
        alarmMusic = (TextView) view.findViewById(R.id.txtMusicName);
        RelativeLayout alarm_title_layout = (RelativeLayout) view.findViewById(R.id.alarm_label_layout);
        RelativeLayout alarm_Music_layout = (RelativeLayout) view.findViewById(R.id.music_text_layout);
        final int[] week_days = AlarmSetting.getWeek_flag();

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
        RelativeLayout active_layout = (RelativeLayout) view.findViewById(R.id.weekday1_layout);
        final RelativeLayout weekDays_layout = (RelativeLayout)view.findViewById(R.id.weekday2_layout);
        for (int i = 0; i < active_layout.getChildCount(); i++) {
            if (active_layout.getChildAt(i).getClass() == TextView.class) {
                weekdays_active[i] = (TextView)active_layout.getChildAt(i);

                final int finalI2 = i;
                weekdays_active[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        weekdays_active[finalI2].setVisibility(View.INVISIBLE);
                        weekdays_lavel[finalI2].setVisibility(View.VISIBLE);
                        AlarmSetting.week_flag[finalI2] = 0;
                    }
                });
            }
            if(weekDays_layout.getChildAt(i).getClass() == TextView.class){
                weekdays_lavel[i] = (TextView)weekDays_layout.getChildAt(i);

                final int finalI = i;
                weekdays_lavel[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        weekdays_lavel[finalI].setVisibility(View.INVISIBLE);
//                        weekdays_active[finalI].setVisibility(View.VISIBLE);
//                        AlarmSetting.week_flag[finalI] = 1;
//                        float yy = weekdays_lavel[finalI].getY();
//                        weekdays_lavel[finalI].setY();
                    }
                });
            }
            if (week_days[i] == 1) {
                weekdays_active[i].setVisibility(View.VISIBLE);
                weekdays_lavel[i].setVisibility(View.INVISIBLE);
            } else {
                weekdays_active[i].setVisibility(View.INVISIBLE);
                weekdays_lavel[i].setVisibility(View.VISIBLE);
            }

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