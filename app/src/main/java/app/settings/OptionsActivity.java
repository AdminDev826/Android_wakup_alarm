package app.settings;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import app.tabsample.R;


/**
 * Created by Alex on 6/13/2016.
 */
public class OptionsActivity extends Activity {

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.optionspage);

        fm = getFragmentManager();

        Fragment frag = new SettingMainFragment();
        fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.setting_fragment, frag).commit();
    }
}
