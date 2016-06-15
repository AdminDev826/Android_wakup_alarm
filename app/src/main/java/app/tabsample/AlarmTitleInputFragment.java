package app.tabsample;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import alarmModels.AlarmSetting;

/**
 * Created by Alex on 6/14/2016.
 */
public class AlarmTitleInputFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.alarm_title_input,container,false);

        final TextView txtTitle = (TextView)view.findViewById(R.id.text_alarm_title);
        Button btnTitle = (Button)view.findViewById(R.id.btnTitle);
        btnTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //condition check---

                AlarmMainActivity.txtAdd.setVisibility(View.VISIBLE);
                AlarmSetting.strAlarmName = txtTitle.getText().toString();
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(view.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);

                Fragment frag = new AlarmEditFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.alarm_fragment, frag);
                fragmentTransaction.commit();
            }
        });
        return view;
    }
}
