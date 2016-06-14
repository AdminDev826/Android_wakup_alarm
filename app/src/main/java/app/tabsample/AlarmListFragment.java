package app.tabsample;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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


public class AlarmListFragment extends Fragment {
    private List<AlarmItem> alarmList = new ArrayList<>();
    private AppAdapter mAdapter;+

    private SwipeMenuListView mListView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(
                R.layout.alarm_list_fragment, container, false);

        SharedPreferences sharedPreference = getActivity().getApplicationContext().getSharedPreferences(AlarmMainActivity.LocalTmpPath, 0);
        int size = sharedPreference.getInt("Array_Size", 0);
        for (int i = 0; i < size; i++) {
            Set<String> hashSet = sharedPreference.getStringSet("Set" + i, null);
            alarmList.add(getDataList(hashSet));
        }

        mListView = (SwipeMenuListView) view.findViewById(R.id.listView);
        mListView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);
        mAdapter = new AppAdapter();
        mListView.setAdapter(mAdapter);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(
//                        getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
//                        0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity().getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);

        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
//                ApplicationInfo item = mAppList.get(position);
//                switch (index) {
//                    case 1:
//                        // open
//                        open(item);
//                        break;
//                    case 0:
                // delete
//					delete(item);
//                        mAppList.remove(position);
                alarmList.remove(position);
                mAdapter.notifyDataSetChanged();
//                        break;
//                }
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

    AlarmItem getDataList(Set<String> hashSet){

        String [] alarmAry = hashSet.toArray(new String[hashSet.size()]);
        String time = " ";
        String title = " ";
        String path = " ";
        int state = 0;
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
                case "path" :
                    path = keys[1];
                    break;
                case "alarm_state" :
                    state = keys[1].length() > 0 ? Integer.parseInt(keys[1]) : 0;
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
        AlarmItem item = new AlarmItem(title, time, path, state, weekdays, repeats);
        return item;
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

            holder.tv_name.setText(alarm.strAlarmName + " ->" + position);
            holder.sw_wake.setTag(Integer.valueOf(position));

            if(alarm.alarm_state == 1){
                holder.tv_name.setTextColor(getResources().getColor(R.color.activecolor));
                holder.tv_title.setTextColor(getResources().getColor(R.color.activecolor));
                holder.tv_time.setTextColor(getResources().getColor(R.color.activecolor));
                holder.sw_wake.setChecked(true);
            }else{
                holder.tv_name.setTextColor(getResources().getColor(R.color.alarm_list_color));
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
            holder.tv_name.setOnClickListener(new View.OnClickListener() {
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
                    mAdapter.notifyDataSetChanged();
                }
            });


            return convertView;
        }

        class ViewHolder {
            ImageView iv_icon;
            TextView tv_name;
            TextView tv_title;
            TextView tv_time;
            Switch sw_wake;

            public ViewHolder(View view) {
                iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
                tv_name = (TextView) view.findViewById(R.id.tv_name);
                sw_wake = (Switch) view.findViewById(R.id.sw_wake);
                tv_title = (TextView) view.findViewById(R.id.top_text);
                tv_time = (TextView) view.findViewById(R.id.bottom_text);

                tv_name.setTextSize(20);
                view.setTag(this);
            }
        }

        @Override
        public boolean getSwipEnableByPosition(int position) {
//            if(position % 2 == 0){
//                return false;
//            }
            return true;
        }
    }
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }


}