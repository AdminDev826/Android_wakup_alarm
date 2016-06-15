package app.tabsample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
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
import java.util.Set;

import alarmModels.AlarmItem;
import alarmModels.AlarmItem;
import alarmModels.AlarmSetting;


public class AlarmListFragment extends Fragment {
    private List<AlarmItem> alarmList = new ArrayList<>();
    private AppAdapter mAdapter;
    private SwipeMenuListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.alarm_list_fragment, container, false);

        alarmList = AlarmSetting.getAlarmData(getActivity().getApplicationContext());
        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
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

        return view;
    }

    private void delete(ApplicationInfo item) {
        // delete app
        try {
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.fromParts("package", item.packageName, null));
            startActivity(intent);
        } catch (Exception e) {
        }
    }
    private void open(ApplicationInfo item) {
        // open app
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(item.packageName);
        List<ResolveInfo> resolveInfoList = getActivity().getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        if (resolveInfoList != null && resolveInfoList.size() > 0) {
            ResolveInfo resolveInfo = resolveInfoList.get(0);
            String activityPackageName = resolveInfo.activityInfo.packageName;
            String className = resolveInfo.activityInfo.name;

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(
                    activityPackageName, className);

            intent.setComponent(componentName);
            startActivity(intent);
        }
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
                convertView = View.inflate(getActivity().getApplicationContext(),
                        R.layout.item_list_app, null);
                new ViewHolder(convertView);
            }
            AlarmItem alarm = getAlarm(position);
            ViewHolder holder = (ViewHolder) convertView.getTag();

            holder.tv_time.setText(alarm.strAlarmTime);
            holder.tv_title.setText(alarm.strAlarmName);
            holder.tv_weekdays.setText(alarm.weekString);
            holder.sw_wake.setTag(Integer.valueOf(position));
            if(alarm.alarm_state == 1){
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
                    Toast.makeText(getActivity(), "iv_icon_click", Toast.LENGTH_SHORT).show();

                }
            });
            holder.tv_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getActivity(),"iv_icon_click" + position,Toast.LENGTH_SHORT).show();
                }
            });
            holder.sw_wake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Switch sw_wake = (Switch)v;
                    int index = ((Integer)sw_wake.getTag()).intValue();
                    AlarmItem alarm = getAlarm(index);
                    if(sw_wake.isChecked()){
                        alarm.alarm_state = 1;
                    }else{
                        alarm.alarm_state = 0;
                    }
                    Log.e("Switch Event:", position + "--->>>" + index + "==" + sw_wake.isChecked());

                    AlarmSetting.setAlarm(alarm);
                    AlarmSetting.saveAlarm(getActivity());
                    mAdapter.notifyDataSetChanged();
                }
            });
            holder.tv_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlarmItem alarm = getAlarm(position);
                    AlarmMainActivity.txtAdd.setText("Done");
                    AlarmMainActivity.txtBack.setVisibility(View.VISIBLE);
                    AlarmSetting.bool_edit_flag = 1;
                    AlarmSetting.setAlarm(alarm);
                    Fragment frag = new AlarmEditFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.alarm_fragment,frag);
                    fragmentTransaction.commit();
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

        @Override
        public boolean getSwipEnableByPosition(int position) {
            return true;
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}