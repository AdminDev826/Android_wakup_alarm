package app.listen;


import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;

import app.alarm.AlarmMainActivity;
import app.alarmModels.AlarmSetting;
import app.main.R;

public class MusicListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    ListView listview;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = getListView();
        listview.setChoiceMode(listview.CHOICE_MODE_MULTIPLE);

//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.alarm_list, android.R.layout.simple_list_item_checked);
//        setListAdapter(adapter);

        String[] songs = getActivity().getResources().getStringArray(R.array.alarm_list);
        AppAdapter adapter = new AppAdapter(getActivity(),songs);
        setListAdapter(adapter);
        listview.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//        MainActivity.tabHost.setCurrentTab(2);
        AlarmSetting.alarm_index = position;
        AlarmSetting.alarm_win = 1;
        AlarmMainActivity.txtAdd.setVisibility(View.VISIBLE);
        getActivity().getFragmentManager().popBackStack();
    }

    class AppAdapter extends ArrayAdapter {
        String[] songList;

        public AppAdapter(Context context, String[] songs) {
            super(context, R.layout.check_text_layout, songs);
            songList = songs;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity().getApplicationContext(),
                        R.layout.check_text_layout, null);
                new ViewHolder(convertView);
            }

            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.txtView.setText(songList[position]);
            if(AlarmSetting.alarm_index == position)
                holder.txtView.setChecked(true);

            return convertView;
        }
        class ViewHolder {
            CheckedTextView txtView;

            public ViewHolder(View view) {
                txtView = (CheckedTextView) view.findViewById(R.id.check_text);
                view.setTag(this);
            }
        }
    }
}
