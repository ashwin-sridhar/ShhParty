package de.tudarmstadt.informatik.tk.shhparty.host;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.utils.HostUtils;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 1/24/2017.
 */

public class PartyHostListener extends Thread {

    private Socket clientSocket;
    private Handler handler;
    ObjectInputStream inStreamFromClient;
    private static final String LOG_TAG="SHH_PartyHostListener";

    public static final int PLAYLIST_UPDATED=108;

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
                    HostUtils.updateThePlaylist((ArrayList<String>) receivedObject);
                    handler.obtainMessage(PLAYLIST_UPDATED).sendToTarget();
                }
                // TODO: 1/28/2017 Another condition to receive chat message

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
