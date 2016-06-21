package app.listen;


import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import app.alarm.AlarmMainActivity;
import app.alarmModels.AlarmSetting;
import app.tabsample.R;

public class MusicListFragment extends ListFragment implements AdapterView.OnItemClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.alarm_list, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//        TabSample.tabHost.setCurrentTab(2);
        AlarmSetting.alarm_index = position;
        AlarmSetting.alarm_win = 1;
        AlarmMainActivity.txtAdd.setVisibility(View.VISIBLE);
        getActivity().getFragmentManager().popBackStack();

//        AlarmSetting.alarm_win = 4;
//        Fragment frag = new MusicPlayerFragment();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.alarm_fragment, frag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
    }
}
