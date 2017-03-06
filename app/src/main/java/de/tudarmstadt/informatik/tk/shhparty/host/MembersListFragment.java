
package de.tudarmstadt.informatik.tk.shhparty.host;

import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Member;
import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.R;
import de.tudarmstadt.informatik.tk.shhparty.member.MemberBean;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicAdapter;


public class MembersListFragment extends Fragment {

    public static ArrayList<MemberBean> listOfMembers=new ArrayList<MemberBean>();

    public static MemberListAdapter memListAdapter;
    public static RecyclerView rv;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_members_list, container, false);
        // Inflate the layout for this fragment
        MemberBean dummyMem1=new MemberBean();
        MemberBean dummyMem2=new MemberBean();
        dummyMem1.setName("Ashwin");
        dummyMem2.setName("Preneesh");
        listOfMembers.add(dummyMem1);
        listOfMembers.add(dummyMem2);
        rv = (RecyclerView) rootView.findViewById(R.id.membersRecycler);
        rv.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        memListAdapter=new MemberListAdapter(getActivity(),listOfMembers);
        rv.setAdapter(memListAdapter);
        return rootView;

    }


}

