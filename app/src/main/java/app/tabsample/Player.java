package app.tabsample;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;

import java.io.File;
import java.util.ArrayList;

public class Player extends Activity {

    MediaPlayer mp;
    ArrayList<File> mySongs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        Intent i = getIntent();
        Bundle b = i.getExtras();
        int position = b.getInt("pos",0);
//        mySongs = (ArrayList)b.getParcelableArrayList("songlist");
//        Uri u = Uri.parse(mySongs.get(position).toString());
        Resources res = getApplicationContext().getResources();
        int soundId = res.getIdentifier("sound" + position, "raw", getApplicationContext().getPackageName());

        mp = MediaPlayer.create(getApplicationContext(),soundId);
        mp.start();

    }

}
