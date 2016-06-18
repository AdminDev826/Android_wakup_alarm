package app.tabsample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by Alex on 6/13/2016.
 */
public class ListenAlarmActivity extends Activity {

    ListView lv;
    String[] items;
    ArrayList<File> mySongs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.music_layout);

        lv = (ListView)findViewById(R.id.lvPlaylist);
//        mySongs = findSongs(Environment.getExternalStorageDirectory());
//        items = new String[mySongs.size()];
//
//        for(int i = 0; i < mySongs.size(); i++){
//            toast(mySongs.get(i).getName().toString());
//            items[i] = mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
//        }

        String[] cmd = getResources().getStringArray(R.array.alarm_list);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,cmd);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("songlist",mySongs));
            }
        });
    }
    public ArrayList<File> findSongs(File root){
        ArrayList<File> al = new ArrayList<File>();
        File[] files = root.listFiles();
        for(File singleFile : files){
            if(singleFile.isDirectory() && singleFile.isHidden()){
                al.addAll(findSongs(singleFile));
            }else{
                if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                    al.add(singleFile);
                }
            }
        }

        return al;
    }
    public void toast(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }
}
