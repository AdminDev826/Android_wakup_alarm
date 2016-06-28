package app.settings;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import app.main.R;
import app.customlistview.CustomAdapter;

/**
 * Created by Alex on 6/21/2016.
 */
public class SettingMainFragment extends ListFragment implements AdapterView.OnItemClickListener  {

    private CustomAdapter mAdapter;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.options_main_layout, container, false);
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAdapter = new CustomAdapter(getActivity());
        mAdapter.addItem("About Barry Zecca",1);
        mAdapter.addItem("Subscribe to newsletter",1);
        mAdapter.addSectionHeaderItem("");
        mAdapter.addItem("Tell a friend", 0);
        mAdapter.addItem("Share on Facebook", 0);
        mAdapter.addItem("Share on Twitter", 0);
        mAdapter.addSectionHeaderItem("");
        mAdapter.addItem("FAQ", 1);
        mAdapter.addItem("Terms of use", 1);

        setListAdapter(mAdapter);
//        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.setting_main_list, android.R.layout.simple_list_item_1);
//        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(getActivity(), "Item: " + position, Toast.LENGTH_SHORT).show();
//        MainActivity.tabHost.setCurrentTab(2);
//        AlarmSetting.alarm_index = position;
//        AlarmSetting.alarm_win = 1;
//        AlarmMainActivity.txtAdd.setVisibility(View.VISIBLE);
//        getActivity().getFragmentManager().popBackStack();

//        AlarmSetting.alarm_win = 4;
//        Fragment frag = new MusicPlayerFragment();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.alarm_fragment, frag);
//        fragmentTransaction.addToBackStack(null);
//        fragmentTransaction.commit();
        OptionsActivity.setFragment(position);
    }
}
