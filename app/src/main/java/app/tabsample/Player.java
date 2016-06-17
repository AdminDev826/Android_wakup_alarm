package app.tabsample;

import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;

public class Player extends Activity implements View.OnClickListener {

    static MediaPlayer mp;
    Handler handler;
    ArrayList<File> mySongs;
    int position;
    Resources res;
    int total = 0;
    int currentPosition = 0;

    SeekBar sb;
    Button btPlay, btFF, btFB, btNxt, btPv;
    TextView txtcurrent, txttotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btPlay = (Button)findViewById(R.id.btPlay);
        btFF = (Button)findViewById(R.id.btFF);
        btFB = (Button)findViewById(R.id.btFB);
        btNxt = (Button)findViewById(R.id.btNxt);
        btPv = (Button)findViewById(R.id.btPv);
        txtcurrent = (TextView)findViewById(R.id.txtSeekPlayTime);
        txttotal = (TextView)findViewById(R.id.txtSeekEndTime);

        btPlay.setOnClickListener(this);
        btFF.setOnClickListener(this);
        btFB.setOnClickListener(this);
        btNxt.setOnClickListener(this);
        btPv.setOnClickListener(this);

        if(mp != null){
            mp.stop();
            mp.release();
        }

        sb = (SeekBar)findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentTime = mp.getCurrentPosition() + seekBar.getProgress();
                txtcurrent.setText(getTimeString(currentTime));
                mp.seekTo(seekBar.getProgress());
            }
        });
        Intent i = getIntent();
        Bundle b = i.getExtras();
        position = b.getInt("pos",0);
//        mySongs = (ArrayList)b.getParcelableArrayList("songlist");
//        Uri u = Uri.parse(mySongs.get(position).toString());
        res = getApplicationContext().getResources();
        int soundId = res.getIdentifier("sound" + position, "raw", getApplicationContext().getPackageName());

        mp = MediaPlayer.create(getApplicationContext(),soundId);
        mp.start();
        total = mp.getDuration();
        sb.setMax(total);
        txttotal.setText(getTimeString(total));
        startTimerThread();
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){
            case R.id.btPlay:
                if(mp.isPlaying()){
                    btPlay.setText((">"));
                    mp.pause();
                }else{
                    btPlay.setText(("||"));
                    mp.start();
                }
                break;
            case R.id.btFF:
                mp.seekTo(mp.getCurrentPosition() + 5000);
                break;
            case R.id.btFB:
                mp.seekTo(mp.getCurrentPosition() - 5000);
                break;
            case R.id.btNxt:
                mp.stop();
                mp.release();
                position = (position + 1) % 5;
                int soundId = res.getIdentifier("sound" + position, "raw", getApplicationContext().getPackageName());
                mp = MediaPlayer.create(getApplicationContext(),soundId);
                mp.start();
                total = mp.getDuration();
                sb.setMax(total);
                txttotal.setText(getTimeString(total));
                break;
            case R.id.btPv:
                mp.stop();
                mp.release();
                position = (position - 1 < 0) ? 4 : (position - 1);
                int sound_Id = res.getIdentifier("sound" + position, "raw", getApplicationContext().getPackageName());
                mp = MediaPlayer.create(getApplicationContext(),sound_Id);
                mp.start();
                total = mp.getDuration();
                sb.setMax(total);
                txttotal.setText(getTimeString(total));
                break;
        }
    }

    private String getTimeString(int t){
        int min = t/1000/60;
        int seconds = t/1000 - min * 60;
        String strMin = min < 10 ? "0" + min : min + "";
        String strSec = seconds < 10 ? "0" + seconds : seconds + "";
        return strMin + ":" + strSec;
    }

    private void startTimerThread() {
        handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                currentPosition = 0;
                while (currentPosition < total) {
                    try {
                        Thread.sleep(200);
                        currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            txtcurrent.setText(getTimeString(currentPosition));
//                            if(currentPosition >= total){
//                                sb.setProgress(0);
//                                txtcurrent.setText("00:00");
//                                btPlay.setText(">");
//                            }
                        }
                    });
                }
            }
        };
        new Thread(runnable).start();
    }
}
