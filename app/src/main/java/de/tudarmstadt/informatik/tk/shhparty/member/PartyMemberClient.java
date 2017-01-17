package de.tudarmstadt.informatik.tk.shhparty.member;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

import de.tudarmstadt.informatik.tk.shhparty.AppTimer;
import de.tudarmstadt.informatik.tk.shhparty.host.PartyHostServer;
import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;

/**
 * Created by Ashwin on 12/11/2016.
 */

public class PartyMemberClient extends Thread {

    private static final String LOG_TAG="SHH_recvmusic";

    private Handler handler;
    private InetAddress mAddress;
    private Socket socket;

    // for syncing time with the server
    private AppTimer timer = new AppTimer(10);

    // a time out for connecting to a server, unit is in milliseconds, 0 for
    // never timing out
    private static final int CONN_TIMEOUT = 0;

    private static final int BUFFER_SIZE = 256;

    public static final int EVENT_RECEIVE_MSG = 100;
    public static final int CLIENT_CALLBACK = 101;
    public static final int PLAYLIST_RECEIVE=103;

    public PartyMemberClient(Handler handler, InetAddress groupOwnerAddress,
                               AppTimer timer)
    {
        this.handler = handler;
        this.mAddress = groupOwnerAddress;
        this.timer = timer;
    }

    @Override
    public void run()
    {
        // let the UI thread control the server
        handler.obtainMessage(CLIENT_CALLBACK, this).sendToTarget();

        // connect the socket first
        connect();

        // thread will stop when disconnect is called, at that point the socket
        // should be closed and nullified
        while (socket != null)
        {
            try
            {
                InputStream iStream = socket.getInputStream();
                OutputStream oStream = socket.getOutputStream();

                ObjectInputStream objInStream=new ObjectInputStream(socket.getInputStream());
                ArrayList<MusicBean> receivedMusicInfo=new ArrayList<MusicBean>();
                try {
                    receivedMusicInfo=(ArrayList<MusicBean>) objInStream.readObject();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                   if(!receivedMusicInfo.isEmpty()){
                        Log.d(LOG_TAG,"yes! received something!");
                        handler.obtainMessage(PLAYLIST_RECEIVE,receivedMusicInfo).sendToTarget();
                    }


                // clear the buffer before reading
                byte[] buffer = new byte[BUFFER_SIZE];
                int bytes;

                // Read from the InputStream
                bytes = iStream.read(buffer);
                if (bytes == -1)
                {
                    continue;
                }

                // need to handle sync messages
                String recMsg = new String(buffer);

                String[] cmdString = recMsg
                        .split(PartyHostServer.CMD_DELIMITER);

                Log.d(LOG_TAG, "Command received: " + recMsg);

                // *** Time Sync here ***
                // receiving messages should be as fast as possible to ensure
                // the successfulness of time synchronization
                if (cmdString[0].equals(PartyHostServer.SYNC_CMD)
                        && cmdString.length > 1)
                {
                    // check if we have received a timer parameter, if
                    // so, set the time, then send back an
                    // Acknowledgment
                    timer.setCurrTime(Long.parseLong(cmdString[1]));

                    // just send the same message back to the server
                    oStream.write(recMsg.getBytes());
                    // Send the obtained bytes to the UI Activity
                    Log.d(LOG_TAG, "Command sent: " + recMsg);
                }

                handler.obtainMessage(EVENT_RECEIVE_MSG, buffer).sendToTarget();
            }
            // this is an ok exception, because someone could have wanted this
            // connection to be closed in the middle of socket read
            catch (SocketException e)
            {
                Log.d(LOG_TAG, "Socket connection has ended.", e);
                disconnect();
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Unexpectedly disconnected during socket read.", e);
                disconnect();
            }
            catch (NumberFormatException e)
            {
                Log.e(LOG_TAG, "Cannot parse time received from server", e);
                disconnect();
            }
        }
    }

    public void connect()
    {
        if (socket == null || socket.isClosed())
        {
            socket = new Socket();
        }

        try
        {
            socket.bind(null);

            socket.connect(new InetSocketAddress(mAddress.getHostAddress(),
                    PartyHostServer.SERVER_PORT), CONN_TIMEOUT);

            Log.d(LOG_TAG, "Connected to server");

            socket.setSoTimeout(CONN_TIMEOUT);
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Cannot connect to server.", e);
            disconnect();
        }
    }

    public void disconnect()
    {
        if (socket == null)
        {
            return;
        }
        try
        {
            socket.close();
            socket = null;
        }
        catch (IOException e)
        {
            Log.e(LOG_TAG, "Could not close socket upon disconnect.", e);
            socket = null;
        }
    }

}
