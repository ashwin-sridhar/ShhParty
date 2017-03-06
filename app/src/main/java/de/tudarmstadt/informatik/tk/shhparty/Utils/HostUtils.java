package de.tudarmstadt.informatik.tk.shhparty.utils;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.chat.ChatMessage;
import de.tudarmstadt.informatik.tk.shhparty.host.ConnectionManager;
import de.tudarmstadt.informatik.tk.shhparty.member.MemberBean;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 1/27/2017.
 */

public class HostUtils {

    public synchronized static void updateTheVotes(MusicBean musicBean){
        ArrayList<MusicBean> originalPlaylist=SharedBox.getThePlaylist();

        for(int i=0;i<originalPlaylist.size();i++){
            if(originalPlaylist.get(i).getMusicID()==musicBean.getMusicID()){
                int currVotes=originalPlaylist.get(i).getVotes();
               currVotes++;
                originalPlaylist.get(i).setVotes(currVotes);
                SharedBox.setThePlaylist(originalPlaylist);
            }
        }
    }

    public synchronized static void  updateThePlaylist(ArrayList<MusicBean> songsToBeAdded) {
        ArrayList<MusicBean> originalPlaylist = SharedBox.getThePlaylist();
        for (int i=0;i<originalPlaylist.size();i++) {
            for (int j = 0; j < songsToBeAdded.size(); j++) {
                if ((originalPlaylist.get(i).getMusicID()) == songsToBeAdded.get(j).getMusicID()){
                    originalPlaylist.get(i).setInPlayist(true);
                    SharedBox.setThePlaylist(originalPlaylist);
                }
            }
        }
        for(int i=0;i<originalPlaylist.size();i++){
            if(originalPlaylist.get(i).isInPlayist())
            Log.d("HostUtils",originalPlaylist.get(i).getMusicTitle()+" "+originalPlaylist.get(i).getMusicID());
        }



    }

    public synchronized static void setReceivedMessage(ChatMessage message){
        SharedBox.setMessage(message);
    }

    public synchronized  static void addNewMemberToParty(MemberBean member){
        SharedBox.listOfMembers.add(member);
    }

    public synchronized  static void addToNameSocketMap(MemberBean member){

    }

    public void buildAndSendCommand(Uri trackUri){

       Log.d("HostUtils","Calling Asynctask to copy track and send command");
        new copyTrackAndSendCommand().execute(trackUri);
    }

    private class copyTrackAndSendCommand extends AsyncTask<Uri,Integer,String>{

        @Override
        protected String doInBackground(Uri... trackUri) {

            File wwwroot=SharedBox.getWwwroot();
            File localMusic= new File(trackUri[0].getPath());
            File hostedFile = new File(wwwroot,localMusic.getName());
            Log.d("HostUtils","file to copy:"+localMusic.getAbsolutePath());
            try {
                CommonUtils.copyFile(localMusic, hostedFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Uri webMusicURI = Uri.parse("http://" + SharedBox.getHttpHostIP() + ":"
                    + String.valueOf(ConnectionManager.HTTP_PORT) + "/" + hostedFile.getName());
            Log.d("HostUtils","WEB MUSIC URI:"+webMusicURI.toString());
            SharedBox.getServer().sendPlay(webMusicURI.toString(), 0, 0);
            return "Copied and sent";
        }
    }
}
