package de.tudarmstadt.informatik.tk.shhparty.utils;

import java.util.ArrayList;

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

    public synchronized static void  updateThePlaylist(ArrayList<String> songsToBeAdded) {
        ArrayList<MusicBean> originalPlaylist = SharedBox.getThePlaylist();

        for (int i = 0; i < songsToBeAdded.size(); i++) {
            if(originalPlaylist.get(i).getMusicID()==Long.parseLong(songsToBeAdded.get(i))) {
                originalPlaylist.get(i).setInPlayist(true);
            }
        }
        SharedBox.setThePlaylist(originalPlaylist);

    }
}
