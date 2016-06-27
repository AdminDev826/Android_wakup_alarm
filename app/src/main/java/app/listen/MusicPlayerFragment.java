package app.listen;

import android.app.Fragment;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

import app.alarmModels.AlarmSetting;
import app.main.R;

public class MusicPlayerFragment extends Fragment implements View.OnClickListener {

    static MediaPlayer mp;
    View view;
    Thread playThread;
    Handler handler;
    Resources res;
    int soundID;
    int total = 0;
    int currentPosition = 0;
    TextView music_title;

    SeekBar sb;
    TextView txtcurrent, txttotal;
    ImageView imgSlow, imgPlay, imgStop, musicImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.alarm_sound_player, container, false);

        txtcurrent = (TextView)view.findViewById(R.id.txtSeekPlayTime);
        txttotal = (TextView)view.findViewById(R.id.txtSeekEndTime);

        imgSlow = (ImageView)view.findViewById(R.id.imgSlow);
        imgPlay = (ImageView)view.findViewById(R.id.imgPlay);
        imgStop = (ImageView)view.findViewById(R.id.imgStop);
        musicImage = (ImageView)view.findViewById(R.id.musicImage);
        music_title = (TextView)view.findViewById(R.id.music_title);

        imgSlow.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgStop.setOnClickListener(this);

        sb = (SeekBar)view.findViewById(R.id.seekBar);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int currentTime = seekBar.getProgress() + mp.getCurrentPosition();
                if(currentTime < total) {
                    txtcurrent.setText(getTimeString(currentTime));
                    txttotal.setText(getLimitString(currentTime));
                    mp.seekTo(seekBar.getProgress());
                }else{
                    txtcurrent.setText(getTimeString(total));
                    txttotal.setText(getLimitString(total));
                    mp.seekTo(total-currentTime);
                }
            }
        });

        init();
        loadMusicImage();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        loadMusicImage();
    }

    void init(){
        total = 0;
        if(playThread!=null){
            Thread t1 = playThread;
            playThread = null;
            t1.interrupt();
        }
        if(mp != null){
            mp.stop();
            mp.release();
            mp = null;
        }

        res = view.getContext().getResources();
        soundID = res.getIdentifier("sound" + AlarmSetting.alarm_index, "raw", view.getContext().getPackageName());
        mp = MediaPlayer.create(view.getContext(),soundID);
        String[] alarms = getResources().getStringArray(R.array.alarm_list);
        music_title.setText(alarms[AlarmSetting.alarm_index]);

        seekbar_init();
    }

    private String getTimeString(int t){
        int min = t/1000/60;
        int seconds = t/1000 - min * 60;
        String strMin = min < 10 ? "0" + min : min + "";
        String strSec = seconds < 10 ? "0" + seconds : seconds + "";
        return strMin + ":" + strSec;
    }

    private void PlayThreadInit() {
        handler = new Handler();
        Runnable runnable = new Runnable() {
            public void run() {
                while (playThread != null && currentPosition < total) {
                    try {
                        Thread.sleep(300);
//                        Log.e("Media Player State : ", mp + "==" + mp.isPlaying());
                        if(mp != null)
                            if(mp.isPlaying())
                                currentPosition = mp.getCurrentPosition();
                        sb.setProgress(currentPosition);
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    handler.post(new Runnable(){
                        public void run() {
                            txtcurrent.setText(getTimeString(currentPosition));
                            txttotal.setText(getLimitString(currentPosition));
                        }
                    });
                }
            }
        };
        playThread = new Thread(runnable);
        playThread.start();
    }

    private void loadMusicImage(){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + soundID);
        mmr.setDataSource(getActivity(),uri);

        byte [] data = mmr.getEmbeddedPicture();
        if(data != null)
        {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            musicImage.setImageBitmap(bitmap); //associated cover art in bitmap
        }
        else
        {
            musicImage.setImageResource(R.drawable.music_back); //any default cover resourse folder
        }
        musicImage.setScaleType(ImageView.ScaleType.FIT_XY);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgSlow:
                seekbar_init();
                break;
            case R.id.imgPlay:
                if(!mp.isPlaying()){
                    mp.start();
                    PlayThreadInit();
                }
                break;
            case R.id.imgStop:
                if(mp.isPlaying()){
                    mp.pause();
                }else{
                    seekbar_init();
                }
                break;
        }
    }
    void seekbar_init(){
        currentPosition = 0;
        sb.setProgress(0);
        txtcurrent.setText("00:00");
        mp.stop();
        try {
            mp.prepare();
            mp.seekTo(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        total = mp.getDuration();
        sb.setMax(total);
        txttotal.setText(getLimitString(0));
    }
    String getLimitString(int cc){
        String temp ="- ";
        int limit = Math.abs(total - cc);
        temp += getTimeString(limit);
        return temp;
    }
}
