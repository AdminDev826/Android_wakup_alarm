package app.notify;

import android.app.Activity;
import android.os.Bundle;

import com.kyleduo.switchbutton.SwitchButton;

import app.main.R;

/**
 * Created by Alex on 6/27/2016.
 */
public class TestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_layout);

        SwitchButton sb = (SwitchButton) findViewById(R.id.sb_ios);
        sb.setChecked(true);

    }
}
