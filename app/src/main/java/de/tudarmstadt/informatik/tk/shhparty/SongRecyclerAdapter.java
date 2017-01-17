package de.tudarmstadt.informatik.tk.shhparty;

/**
 * Created by Admin on 1/3/2017.
 */

import android.content.Context;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

public class SongRecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

  Context c;
  ArrayList<MusicBean> movies;
  private LayoutInflater mLayoutInflater;
  private SortedList <MusicBean> mMovies;
  Map<String, String> map = new HashMap<String, String>();
  private String LOG_TAG="Shh_SortSongsAdapter";

  public SongRecyclerAdapter(Context c, ArrayList<MusicBean> movies) {
    this.c = c;
   this.movies = movies;
    mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    mMovies = new SortedList<>(MusicBean.class, new MovieListCallback());
    mMovies.addAll(movies);
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,null);
    ViewHolder holder = new ViewHolder(v);
    return holder;


  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, final int position) {

    final MusicBean movies = mMovies.get(position);
    holder.songTitle.setText(movies.getMusicTitle());
    holder.vote.setText(String.valueOf(movies.getVotes()));
    holder.artist.setText(movies.getArtist());
    holder.img.setImageResource(R.drawable.like);

    final String title = movies.getMusicTitle();
    final int votes = movies.getVotes();
    final String artist = movies.getArtist();
    final long musicId = movies.getMusicID();
    final boolean playlist = movies.isInPlayist();

   /*holder.img.setOnClickListener(new View.OnClickListener(){
     @Override
     public void onClick(View v) {

       int count = votes;
       if (count == 0) {
         count = count + 1;
         holder.vote.setText(""+count);
         String voteCount = holder.vote.getText().toString();
         MusicBean song = new MusicBean(musicId,title, artist,playlist, count);
         mMovies.updateItemAt(position,song);
         notifyDataSetChanged();
         //Add into an arraylist

         map.put(Long.toString(musicId),Integer.toString(count));
         System.out.println(map);
         Log.d(LOG_TAG,"The map is"+map.toString());

       }
     }

   });*/
   }

  @Override
  public int getItemCount() {
    return movies.size();
  }

  private class MovieListCallback extends SortedList.Callback<MusicBean> {

    @Override
    public int compare(MusicBean s1, MusicBean s2) {
      int val1=Integer.valueOf(s1.getVotes());
      int val2=Integer.valueOf(s2.getVotes());
      return val2 > val1 ? 1 : (val2 < val1 ? -1 : 0);


      //  return Integer.valueOf(s2.getVote().compareTo(s1.getVote()));
    }

    @Override
    public void onInserted(int position, int count) {
      notifyItemInserted(position);
    }

    @Override
    public void onRemoved(int position, int count) {
      notifyItemRemoved(position);
    }

    @Override
    public void onMoved(int fromPosition, int toPosition) {
    }

    @Override
    public void onChanged(int position, int count) {
    }

    @Override
    public boolean areContentsTheSame(MusicBean oldItem, MusicBean newItem) {
      return false;
    }

    @Override
    public boolean areItemsTheSame(MusicBean item1, MusicBean item2) {
      return false;
    }

  }

}

