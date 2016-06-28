package app.main;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import app.alarm.AlarmMainActivity;
import app.listen.ListenActivity;
import app.notify.NotifyListActivity;
import app.settings.OptionsActivity;


public class MainActivity extends TabActivity {
	/** Called when the activity is first created. */

	public static TabHost tabHost;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTabs() ;
	}
	private void setTabs()
	{
		addTab("Alarm", R.drawable.tab_alarm, AlarmMainActivity.class);
		addTab("Notify", R.drawable.tab_notify, NotifyListActivity.class);
		
		addTab("Listen", R.drawable.tab_listen, ListenActivity.class);
		addTab("Settings", R.drawable.tab_settings, OptionsActivity.class);
	}
	
	private void addTab(String labelId, int drawableId, Class<?> c)
	{
		tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);	
		
		View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
		//set back coloor
//		tabIndicator.setBackgroundColor(getResources().getColor(R.color.nonactivecolor));

		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}