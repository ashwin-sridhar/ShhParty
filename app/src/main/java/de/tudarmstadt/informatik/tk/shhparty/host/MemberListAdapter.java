package de.tudarmstadt.informatik.tk.shhparty.host;

import android.net.wifi.p2p.WifiP2pInfo;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.R;

/**
 * Created by Ashwin on 1/17/2017.
 */

public class MemberListAdapter extends RecyclerView.Adapter<MemberListAdapter.ViewHolder> {

    private ArrayList<WifiP2pInfo> listOfMembers=new ArrayList<WifiP2pInfo>();

    public MemberListAdapter(ArrayList<WifiP2pInfo> listOfMembers) {
        this.listOfMembers = listOfMembers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView memberName;
        public ViewHolder(View itemView) {
            super(itemView);
            memberName=(TextView) itemView.findViewById(R.id.member_name);
        }
    }
}
