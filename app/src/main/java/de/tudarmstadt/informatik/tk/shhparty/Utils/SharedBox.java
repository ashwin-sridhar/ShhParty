package de.tudarmstadt.informatik.tk.shhparty.utils;

import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.host.PartyHostServer;
import de.tudarmstadt.informatik.tk.shhparty.member.PartyMemberClient;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 1/25/2017.
 */

public class SharedBox {


    public static PartyMemberClient client;

    public static PartyHostServer getServer() {
        return server;
    }

    public static void setServer(PartyHostServer server) {
        SharedBox.server = server;
    }

    public static PartyHostServer server;

    public static ArrayList<MusicBean> getThePlaylist() {
        return thePlaylist;
    }

    public static void setThePlaylist(ArrayList<MusicBean> thePlaylist) {
        SharedBox.thePlaylist = thePlaylist;
    }

    public static ArrayList<MusicBean> thePlaylist=new ArrayList<MusicBean>();

    public static void setClient(PartyMemberClient client) {
        SharedBox.client = client;
    }

    public static PartyMemberClient getClient() {
        return client;
    }
}
