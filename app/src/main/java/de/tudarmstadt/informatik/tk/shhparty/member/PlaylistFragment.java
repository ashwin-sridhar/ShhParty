package de.tudarmstadt.informatik.tk.shhparty.member;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.R;
import de.tudarmstadt.informatik.tk.shhparty.SongRecyclerAdapter;
import de.tudarmstadt.informatik.tk.shhparty.member.PartyHome;
import de.tudarmstadt.informatik.tk.shhparty.utils.ItemClickSupport;
import de.tudarmstadt.informatik.tk.shhparty.utils.SharedBox;
import de.tudarmstadt.informatik.tk.shhparty.member.PartyMemberClient;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

public class PlaylistFragment extends Fragment {
    private static final String LOG_TAG="Shh_plfragment";
    private ArrayList<MusicBean> allSongs=new ArrayList<MusicBean>();
    private ArrayList<String> votedSongs=new ArrayList<String>();

    @Nullable

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView=inflater.inflate(R.layout.fragment_playlist,null);

        allSongs=getPlaylistSongs();
        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.mRecyclerCrime);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        SongRecyclerAdapter songAdapter=new SongRecyclerAdapter(this.getActivity(),allSongs);
        rv.setAdapter(songAdapter);
        PartyHome partyHome=(PartyHome)getActivity();

        final PartyMemberClient  client= SharedBox.getClient();

        ItemClickSupport.addTo(rv).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            //retrieves the specific musicitem, sets inplaylist true and adds to parcel list
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                // do it
                Log.d(LOG_TAG,"Clicked!");
                MusicBean selectedMusic=allSongs.get(position);
                votedSongs.add(Long.toString(selectedMusic.getMusicID()));

                int voteCount=selectedMusic.getVotes();
                voteCount++;
                selectedMusic.setVotes(voteCount);
                client.sendVote(selectedMusic);

            }
        });

      return rootView;
    }


  private ArrayList<MusicBean> getPlaylistSongs()
  {
      ArrayList<MusicBean> allSongs = new ArrayList<MusicBean>();
      ArrayList<MusicBean> playlist = new ArrayList<MusicBean>();
      allSongs=SharedBox.getThePlaylist();
      for(int i=0;i<allSongs.size();i++){
          if(allSongs.get(i).isInPlayist())
            playlist.add(allSongs.get(i));
      }
    return playlist;
  }

  public String toString() {
    return "Playlist";
  }
}
