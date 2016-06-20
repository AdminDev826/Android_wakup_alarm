package app.tabsample;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import alarmModels.AlarmSetting;

/**
 * Created by Alex on 6/20/2016.
 */
public class MusicPlayerFragment extends Fragment implements View.OnClickListener {

    static MediaPlayer mp;
    Handler handler;
    Resources res;
    int soundID;
    int total = 0;
    int currentPosition = 0;

    SeekBar sb;
    TextView txtcurrent, txttotal;
    ImageView imgSlow, imgPlay, imgStop, musicImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_sound_player, container, false);

        txtcurrent = (TextView)view.findViewById(R.id.txtSeekPlayTime);
        txttotal = (TextView)view.findViewById(R.id.txtSeekEndTime);

        imgSlow = (ImageView)view.findViewById(R.id.imgSlow);
        imgPlay = (ImageView)view.findViewById(R.id.imgPlay);
        imgStop = (ImageView)view.findViewById(R.id.imgStop);
        musicImage = (ImageView)view.findViewById(R.id.musicImage);

        imgSlow.setOnClickListener(this);
        imgPlay.setOnClickListener(this);
        imgStop.setOnClickListener(this);


        if(mp != null){
            mp.stop();
            mp.release();
        }

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
                int currentTime = mp.getCurrentPosition() + seekBar.getProgress();
                txtcurrent.setText(getTimeString(currentTime));
                mp.seekTo(seekBar.getProgress());
            }
        });
        res = view.getContext().getResources();
        soundID = res.getIdentifier("sound" + AlarmSetting.alarm_index, "raw", view.getContext().getPackageName());

        mp = MediaPlayer.create(view.getContext(),soundID);
//        mp.start();
        total = mp.getDuration();
        sb.setMax(total);
        txttotal.setText(getTimeString(total));
        startTimerThread();
        loadMusicImage();

        return view;
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

    private void loadMusicImage(){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        Uri uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + soundID);
        mmr.setDataSource(getActivity(),uri);

        byte [] data = mmr.getEmbeddedPicture();
        //coverart is an Imageview object

        // convert the byte array to a bitmap
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
                break;
            case R.id.imgPlay:
                if(!mp.isPlaying()){
                    mp.start();
                }
                break;
            case R.id.imgStop:
                if(mp.isPlaying()){
                    mp.pause();
                }else{
                    sb.setProgress(0);
                    txtcurrent.setText("00:00");
                    mp.stop();
                    try {
                        mp.prepare();
                        mp.seekTo(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    currentPosition = 0;
                }
                break;
        }
    }
}
