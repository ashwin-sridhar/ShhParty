package de.tudarmstadt.informatik.tk.shhparty;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;

import de.tudarmstadt.informatik.tk.shhparty.music.MusicAdapter;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 11/17/2016.
 */

public class SelectSongsActivity extends Activity {


    private ListView musiclist;
    private int REQUEST_CODE_READEXTSTORAGE=123;
    private ArrayList<MusicBean> listOfMusic=new ArrayList<MusicBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        //String debugtest=intent.getStringExtra("test");
        //Log.d("SOme message",debugtest);
        setContentView(R.layout.activity_recycler_select_songs);

        getMusicFromDevice();

       // musiclist=(ListView) findViewById(R.id.songslist);
        RecyclerView musicRecyclerView= (RecyclerView) findViewById(R.id.musicrecycler);
        MusicAdapter musicAdapter=new MusicAdapter(listOfMusic,this);

        musicRecyclerView.setAdapter(musicAdapter);
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        Log.d("MUSIC RET",listOfMusic.get(3).getMusicTitle());

        /*MusicAdapter musicadapter=new MusicAdapter(listOfMusic,this);
        musiclist.setAdapter(musicadapter);*/

       /* List<String> listOfMusic= Arrays.asList("Bohemian Rhapsody",
                "Stairway to Heaven",
                "Lover's spit",
                "Beethoven's Symphony",
                "Sound of Silence",
                "Game of thrones",
                "Harry Potter theme",
                "Random stupid song",
                "Running out of ideas",
                "Could well be a title",
                "Hope this is enough",
                "Just in case..");

        ArrayAdapter musicListAdapter=new ArrayAdapter(this,R.layout.musiclist,R.id.musiclist_item,listOfMusic);
        musiclist.setAdapter(musicListAdapter);*/
    }

    public void getMusicFromDevice(){

        int hasReadStoragePermission=checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if(hasReadStoragePermission!= PackageManager.PERMISSION_GRANTED){
            if(shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)){
                    showMessageOKCancel("You need to allow access to storage",
                            new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface dialog,int which){
                                    requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READEXTSTORAGE);
                                }
                            });
                return;
            }
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READEXTSTORAGE);
                return;

        }
        ContentResolver musicResolver=getContentResolver();
        Uri musicUri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor= musicResolver.query(musicUri,null,null,null,null);

        if(musicCursor!=null && musicCursor.moveToFirst()){
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                listOfMusic.add(new MusicBean(thisId, thisTitle, thisArtist));
            }
            while (musicCursor.moveToNext());
        }


    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
