
package de.tudarmstadt.informatik.tk.shhparty.host;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.tudarmstadt.informatik.tk.shhparty.R;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicAdapter;


public class MembersListFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_members_list, container, false);
        return rootView;

    }


}

