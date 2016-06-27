package app.settings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import app.alarmModels.AlarmSetting;
import app.main.R;


/**
 * Created by Alex on 6/13/2016.
 */
public class OptionsActivity extends Activity implements View.OnClickListener {

    static FragmentManager fm;
    static Fragment frag;
    static FragmentTransaction fragmentTransaction;
    static TextView txtBack;
    static TextView txtTitle;
    static TextView txtDone;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionspage);

        txtBack = (TextView)findViewById(R.id.setting_txtBack);
        txtTitle = (TextView)findViewById(R.id.setting_title);
        txtDone = (TextView)findViewById(R.id.setting_txtAdd);

        txtBack.setOnClickListener(this);
        txtTitle.setOnClickListener(this);
        txtDone.setOnClickListener(this);

        fm = getFragmentManager();
        frag = new SettingMainFragment();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.setting_fragment, frag).commit();
    }

    public static void setFragment(int position){
        switch (position){
            case 0:
                txtBack.setVisibility(View.VISIBLE);
                AlarmSetting.alarm_win = 5;
                frag = new SettingAboutFragment();
                fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.setting_fragment,frag);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                break;
//            case 7:
//                txtBack.setVisibility(View.VISIBLE);
//                frag = new SettingFAQFragment();
//                fragmentTransaction = fm.beginTransaction();
//                fragmentTransaction.replace(R.id.setting_fragment,frag);
//                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.setting_txtBack :
                if(AlarmSetting.alarm_win == 5){
                    txtBack.setVisibility(View.INVISIBLE);
                }
                fm.popBackStack();
                break;
        }
    }
}
