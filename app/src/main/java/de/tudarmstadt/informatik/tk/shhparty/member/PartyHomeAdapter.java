package de.tudarmstadt.informatik.tk.shhparty.member;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;


import de.tudarmstadt.informatik.tk.shhparty.chat.ChatFragment;

/**
 * Created by Ashwin on 1/7/2017.
 */

public class PartyHomeAdapter extends FragmentPagerAdapter {

    int numOfTabs;


    public PartyHomeAdapter(FragmentManager fm, int numofTabs) {
        super(fm);
        this.numOfTabs=numofTabs;

    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                PlaylistFragment plfrag = new PlaylistFragment();
                return plfrag;
            case 1:
                MusicLibrary mlibtab = new MusicLibrary();
                return mlibtab;
            case 2:
                ChatFragment chatfrag = new ChatFragment();
                return chatfrag;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
