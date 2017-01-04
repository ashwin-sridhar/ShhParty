package de.tudarmstadt.informatik.tk.shhparty.host;

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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.jar.Manifest;

import de.tudarmstadt.informatik.tk.shhparty.R;
import de.tudarmstadt.informatik.tk.shhparty.Utils.ItemClickSupport;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicAdapter;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 11/17/2016.
 * -Fetches all the music from the device
 * -displays as a list for the party host to select the songs
 * -The selected songs are saved as playlist
 * -Transitions to Connection initiation
 */

public class SelectSongsActivity extends Activity {


    private String LOG_TAG="Shh_selectSongs";
    private ListView musiclist;
    private int REQUEST_CODE_READEXTSTORAGE=123;
    private ArrayList<MusicBean> listOfMusic=new ArrayList<MusicBean>();
    private ArrayList<MusicBean> musicInfoToShare=new ArrayList<MusicBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_select_songs);

        // I/O call to fetch Music info
        getMusicFromDevice();
        musicInfoToShare.addAll(listOfMusic);

        //Mapping data to UI with custom Adapter - MusicAdapter
        RecyclerView musicRecyclerView= (RecyclerView) findViewById(R.id.musicrecycler);
        final MusicAdapter musicAdapter=new MusicAdapter(listOfMusic,this);

        musicRecyclerView.setAdapter(musicAdapter);
        Log.d(LOG_TAG,"SHIT");
        ItemClickSupport.addTo(musicRecyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            //retrieves the specific musicitem, sets inplaylist true and adds to parcel list
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Log.d(LOG_TAG,"Clicked!");
                //MusicBean selectedMusic=musicAdapter.getItem(position);
                MusicBean selectedMusic=musicInfoToShare.get(position);
                selectedMusic.setInPlayist(true);
                musicInfoToShare.add(selectedMusic);
            }
        });
        musicRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Implementing onClick on recyclerview item



    }

    //Checks for READ permission, requests for it otherwise and fetches the music from the device
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
                listOfMusic.add(new MusicBean(thisId, thisTitle, thisArtist,false,0));
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

    public void packMusicInfoAndTransition(View view){
        Intent toConnWorks=new Intent(this,ConnectionManager.class);
        toConnWorks.putExtra("musicAndPlaylist",musicInfoToShare);
        startActivity(toConnWorks);
    }
}
