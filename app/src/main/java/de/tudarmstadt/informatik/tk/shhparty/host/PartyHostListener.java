package de.tudarmstadt.informatik.tk.shhparty.host;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.chat.ChatMessage;
import de.tudarmstadt.informatik.tk.shhparty.member.MemberBean;
import de.tudarmstadt.informatik.tk.shhparty.utils.HostUtils;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;
import de.tudarmstadt.informatik.tk.shhparty.utils.SharedBox;

/**
 * Created by Ashwin on 1/24/2017.
 */

public class PartyHostListener extends Thread {

    private Socket clientSocket;
    private Handler handler;
    ObjectInputStream inStreamFromClient;
    private static final String LOG_TAG="SHH_PartyHostListener";

    public static final int PLAYLIST_UPDATED=108;
    public static final int CHATBOX_UPDATED=110;
    public static final int MEMBERDATA_RECEIVED=130;

    public PartyHostListener(Handler handler,Socket clientSocket) throws IOException
    {
        this.handler = handler;
        this.clientSocket=clientSocket;

    }

    @Override
    public void run() {

        Log.d(LOG_TAG,"Child thread started");
        while(true) {
            if (inStreamFromClient == null) {
                setupInputStream();
            }
            Object receivedObject = new Object();
            try {

                receivedObject = inStreamFromClient.readObject();
                if(receivedObject instanceof MusicBean){
                    HostUtils.updateTheVotes((MusicBean)receivedObject);
                    handler.obtainMessage(PLAYLIST_UPDATED).sendToTarget();
                }
                else if(receivedObject instanceof ArrayList<?>){
                    HostUtils.updateThePlaylist((ArrayList<MusicBean>) receivedObject);
                    handler.obtainMessage(PLAYLIST_UPDATED).sendToTarget();
                }
                else if(receivedObject instanceof ChatMessage){
                    HostUtils.setReceivedMessage((ChatMessage) receivedObject);
                    handler.obtainMessage(CHATBOX_UPDATED).sendToTarget();
                }
                else if(receivedObject instanceof MemberBean){
                    HostUtils.addNewMemberToParty((MemberBean)receivedObject);
                    HostUtils.addToNameSocketMap((MemberBean) receivedObject,clientSocket);
                    handler.obtainMessage(MEMBERDATA_RECEIVED).sendToTarget();
                }
              else{
                    Log.d(LOG_TAG,"Received object of unexpected type:"+receivedObject.getClass().getName());
                }

            } catch (ClassNotFoundException e) {
                //e.printStackTrace();
            } catch (IOException e) {
                //e.printStackTrace();
            }
           // Log.d(LOG_TAG, "Received object is" + receivedObject.toString());
        }

    }

    private void setupInputStream(){
        try {
            inStreamFromClient=new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
