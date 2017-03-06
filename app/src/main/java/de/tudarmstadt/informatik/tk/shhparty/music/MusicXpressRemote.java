package de.tudarmstadt.informatik.tk.shhparty.music;

import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by rohit on 03-03-2017.
 */

public class MusicXpressRemote extends Service implements
        MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {


    private MediaPlayer player; //media player

   // private ArrayList<MusicBean> songs; // song list

  //  private int songPosn;   //current position

    public static final int FIRST_SONG = 0;

    private final IBinder musicBind = new RemoteMusicBinder();

    public void onCreate() {
        //create the service
        super.onCreate();
        //initialize position
     //   songPosn = 0;

        //create player
        player = new MediaPlayer();
        initMusicPlayer();
        Log.v("MusicXpress Remote","onCreate()");
    }

    public void initMusicPlayer(){
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

  /*  public void setList(ArrayList<MusicBean> theSongs){
        songs=theSongs;
    }
*/
    public class RemoteMusicBinder extends Binder {
        public MusicXpressRemote getService() {
            return MusicXpressRemote.this;
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent){
        player.stop();
        player.release();
        return false;
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        //start playback
        mediaPlayer.start();
    }

    // TODO : No local list, just stream from host
    /*public void setSong(int songIndex){
        songPosn=songIndex;
    }*/


    public void onStop(View view) {
        player.stop();
    }

    public void onPlay(View view){
        player.reset();
        //setSong(FIRST_SONG); //
        //get song
        //MusicBean playSong = songs.get(songPosn);
        //get id
        //long currSong = playSong.getMusicID();
        //set uri
        /*Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                currSong);*/
        // TODO: 3/6/2017 Get the command Bean and set data source, prepareasync
        try{
            //player.setDataSource(getApplicationContext(), trackUri);
        }
        catch(Exception e){
            Log.e("MusicXpress Remote", "Error setting data source", e);
        }

        player.prepareAsync();
    }

    public void onPause(View view){
        player.pause();
    }
}