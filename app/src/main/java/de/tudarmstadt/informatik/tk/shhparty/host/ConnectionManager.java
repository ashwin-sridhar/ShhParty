package de.tudarmstadt.informatik.tk.shhparty.host;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceInfo;
import android.net.wifi.p2p.nsd.WifiP2pDnsSdServiceRequest;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.tudarmstadt.informatik.tk.shhparty.music.MusicBean;
import de.tudarmstadt.informatik.tk.shhparty.wifip2p.ConnectionTemplate;
import de.tudarmstadt.informatik.tk.shhparty.R;
import de.tudarmstadt.informatik.tk.shhparty.Utils.CommonUtils;
import de.tudarmstadt.informatik.tk.shhparty.Utils.ConnectionUtils;
import de.tudarmstadt.informatik.tk.shhparty.wifip2p.WiFiDirectPulseChecker;
import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.SimpleWebServer;

public class ConnectionManager extends ConnectionTemplate implements WifiP2pManager.ChannelListener,WifiP2pManager.ConnectionInfoListener,Handler.Callback {

    private final IntentFilter wifiStatesIntentFilter=new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2pManager p2pManager;
    private boolean channelRetry=false;
    private boolean isWifiP2PEnabled=false;

    private NanoHTTPD httpServer;
    public static final int HTTP_PORT=9002;
    private String httpHostIP="";

    private PartyHostServer serverThread;
    private final Handler handler = new Handler(this);
    private File wwwroot = null;

    private List availablePeers=new ArrayList();

    private WiFiDirectPulseChecker p2ppulsechecker;

    private static final String LOG_TAG="SHH_ConnMgr";

    private WifiP2pDnsSdServiceRequest serviceRequest;
    private ArrayList<MusicBean> musicInfoParcel=new ArrayList<MusicBean>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_manager);

        wwwroot = getApplicationContext().getFilesDir();

        Intent receivedIntent=getIntent();
        musicInfoParcel=(ArrayList<MusicBean>) receivedIntent.getSerializableExtra("musicAndPlaylist");
        Log.d(LOG_TAG,"The parcel received from selectmusic"+musicInfoParcel.toString());

        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        p2pManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=p2pManager.initialize(this,getMainLooper(),null);

        //advertise the service
        spreadWordAboutService();



    }

    @Override
    public void onChannelDisconnected() {
        if(p2pManager!=null&&!channelRetry){
            Toast.makeText(this,"Channel lost. Trying again",Toast.LENGTH_LONG).show();
            channelRetry=true;
            channel=p2pManager.initialize(this,getMainLooper(),null);
        }
        else{
            Toast.makeText(this,"Something seems very wrong. Try disabling and re-enabling Wifi P2p",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        p2ppulsechecker=new WiFiDirectPulseChecker(channel,p2pManager,this);
        registerReceiver(p2ppulsechecker,wifiStatesIntentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(p2ppulsechecker);
    }

    public void spreadWordAboutService(){
        Map<String,String> partyServiceData=new HashMap<String,String>();
        partyServiceData.put("partyhost","Ashwin");
        partyServiceData.put("devicestatus","available");
        partyServiceData.put("portnumber", String.valueOf(ConnectionUtils.getPort(ConnectionManager.this)));
        partyServiceData.put("wifiip", CommonUtils.getWiFiIPAddress(ConnectionManager.this));
        WifiP2pDnsSdServiceInfo partyServiceInfo=WifiP2pDnsSdServiceInfo.newInstance("_shhpartymusic","_shhparty._tcp",partyServiceData);
        p2pManager.addLocalService(channel, partyServiceInfo, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {

                Log.d(LOG_TAG,"Advertising service sucess!");
            }

            @Override
            public void onFailure(int reason) {
                Log.d(LOG_TAG,"Advertising service failed!Reason code is"+reason);
            }
        });

        /*have to discover services to be discoverable
         * not doing anything after discovering really */
        serviceRequest = WifiP2pDnsSdServiceRequest.newInstance();
        p2pManager.addServiceRequest(channel, serviceRequest,
                new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        //Nothing done as of now
                    }
                    @Override
                    public void onFailure(int arg0) {
                        //nothing done as of now
                    }
                });
        p2pManager.discoverServices(channel, new WifiP2pManager.ActionListener() {

            @Override
            public void onSuccess() {

                Log.d(LOG_TAG,"Discovery initiated successfully..");
            }

            @Override
            public void onFailure(int arg0) {
                Log.d(LOG_TAG,"Discovery initiation failed-reason code is:"+arg0);

            }
        });

    }

    @Override
    public void onConnectionInfoAvailable(WifiP2pInfo info) {
        if(info.groupFormed&&info.isGroupOwner){
            Log.d(LOG_TAG,"Connected and acquired GO status");
            try
            {
                // WARNING:
                // depends on the timing, if we don't get a server back in time,
                // we may end up running multiple threads of the server
                // instance!
                if (this.serverThread == null)
                {
                    Thread server = new PartyHostServer(this.handler);
                    server.start();

                    if (wwwroot != null)
                    {
                        if (httpServer == null)
                        {
                            httpHostIP = info.groupOwnerAddress
                                    .getHostAddress();

                            boolean quiet = false;

                            httpServer = new SimpleWebServer(httpHostIP,
                                    HTTP_PORT, wwwroot, quiet);
                            try
                            {
                                httpServer.start();
                                Log.d("HTTP Server",
                                        "Started web server with IP address: "
                                                + httpHostIP);

                            }
                            catch (IOException ioe)
                            {
                                Log.e("HTTP Server", "Couldn't start server:\n");
                                ioe.printStackTrace();
                            }
                        }
                    }
                    else
                    {
                        Log.e("HTTP Server",
                                "Could not retrieve a directory for the HTTP server.");
                    }
                }
            }
            catch (IOException e)
            {
                Log.e(LOG_TAG, "Cannot start server.", e);
            }
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what)
        {
            case PartyHostServer.SERVER_CALLBACK:
                serverThread = (PartyHostServer) msg.obj;
                Log.d(LOG_TAG, "Retrieved server thread.");
                makePlayCall(serverThread);
                break;

            default:
                Log.d(LOG_TAG, "I thought we heard something? Message type: "
                        + msg.what);
                break;
        }
        return true;
    }

    public void makePlayCall(PartyHostServer sThread){
        //Copy file to www method call
        File localMusic= new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/sample.wav");
        File webFile = new File(wwwroot,localMusic.getName());
        Log.d(LOG_TAG,"file to copy:"+localMusic.getAbsolutePath());
        try {
            CommonUtils.copyFile(localMusic, webFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Uri webMusicURI = Uri.parse("http://" + httpHostIP + ":"
                + String.valueOf(HTTP_PORT) + "/" + webFile.getName());
        Log.d(LOG_TAG,"WEB MUSIC URI:"+webMusicURI.toString());
        sThread.sendPlay(webMusicURI.toString(), 0, 0);
    }

    public void shareMusicAndPlaylist(PartyHostServer sThread){
        //send musicInfoToShare to thread which sends as bytes
    }
}


