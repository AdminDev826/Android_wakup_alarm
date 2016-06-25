package app.notify;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import app.alarm.AlarmEditFragment;
import app.alarm.AlarmMainActivity;
import app.alarmModels.AlarmItem;
import app.alarmModels.AlarmSetting;
import app.tabsample.R;

/**
 * Created by Alex on 6/23/2016.
 */
public class NotifyListActivity extends Activity {

    private List<AlarmItem> alarmList = new ArrayList<>();
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.notify_layout);

        alarmList = AlarmSetting.getAlarmData(getApplicationContext());
        mListView = (SwipeMenuListView) findViewById(R.id.listView);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                AlarmItem alarm = alarmList.get(position);
                AlarmSetting.deleteAlarm(getApplicationContext(),alarm);
                alarmList.remove(position);
                mAdapter.notifyDataSetChanged();
                return false;
            }
        });
        // set SwipeListener
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }
            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });
    }
    class AppAdapter extends BaseSwipListAdapter {
        @Override
        public int getCount() {
            return alarmList.size();
        }
        @Override
        public AlarmItem getItem(int position) {
            return alarmList.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        public AlarmItem getAlarm(int position){
            return alarmList.get(position);
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            AlarmItem alarm = getAlarm(position);
            ViewHolder holder = (ViewHolder) convertView.getTag();

            holder.tv_time.setText(getFormatTime(alarm.strAlarmTime));
            holder.tv_title.setText(alarm.strAlarmName);
            holder.tv_weekdays.setText(alarm.weekString);
            holder.sw_wake.setTag(Integer.valueOf(position));
            if(alarm.noti_state == 1){
                holder.tv_time.setTextColor(getResources().getColor(R.color.activecolor));
                holder.tv_title.setTextColor(getResources().getColor(R.color.activecolor));
                holder.tv_weekdays.setTextColor(getResources().getColor(R.color.activecolor));
                holder.sw_wake.setChecked(true);
            }else{
                holder.tv_weekdays.setTextColor(getResources().getColor(R.color.alarm_list_color));
                holder.tv_title.setTextColor(getResources().getColor(R.color.alarm_list_color));
                holder.tv_time.setTextColor(getResources().getColor(R.color.alarm_list_color));
                holder.sw_wake.setChecked(false);
            }
            holder.iv_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(NotifyListActivity.this, "iv_icon_click", Toast.LENGTH_SHORT).show();

                }
            });
            holder.tv_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(NotifyListActivity.this,"iv_icon_click" + position,Toast.LENGTH_SHORT).show();
                }
            });
            holder.sw_wake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch sw_wake = (Switch)v;
                    int index = ((Integer)sw_wake.getTag()).intValue();
                    AlarmItem alarm = getAlarm(index);
                    if(sw_wake.isChecked()){
                        alarm.noti_state = 1;
                    }else{
                        alarm.noti_state = 0;
                    }
                    Log.e("Switch Event:", position + "--->>>" + index + "==" + sw_wake.isChecked());

                    AlarmSetting.setAlarm(alarm);
                    AlarmSetting.saveAlarm(NotifyListActivity.this);
                    mAdapter.notifyDataSetChanged();
                }
            });
            return convertView;
        }
        class ViewHolder {
            ImageView iv_icon;
            TextView tv_time;
            TextView tv_title;
            TextView tv_weekdays;
            Switch sw_wake;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_time = (TextView) view.findViewById(R.id.tv_name);
                sw_wake = (Switch) view.findViewById(R.id.sw_wake);
                tv_title = (TextView) view.findViewById(R.id.top_text);
                tv_weekdays = (TextView) view.findViewById(R.id.bottom_text);

                tv_time.setTextSize(20);
                view.setTag(this);
            }
        }
        String getFormatTime(String t){
            Log.e("Set time is : " , t);
            String temp;
            int hour;
            String[] tmp = t.split(":");
            hour = Integer.valueOf(tmp[0]);
            if(hour > 12){
                temp = (hour - 12) + ":" + tmp[1]  + "  PM";
            }else{
                temp = t + "  AM";
            }
            return temp;
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
            return false;
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}