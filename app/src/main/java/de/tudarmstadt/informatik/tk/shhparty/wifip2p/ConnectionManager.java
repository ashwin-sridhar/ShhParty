package de.tudarmstadt.informatik.tk.shhparty.wifip2p;

import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.tudarmstadt.informatik.tk.shhparty.R;

public class ConnectionManager extends Activity implements WifiP2pManager.ChannelListener,WifiP2pManager.PeerListListener {

    private final IntentFilter wifiStatesIntentFilter=new IntentFilter();
    private WifiP2pManager.Channel channel;
    private WifiP2pManager p2pManager;
    private boolean channelRetry=false;
    private boolean isWifiP2PEnabled=false;

    private List availablePeers=new ArrayList();

    private WiFiDirectPulseChecker p2ppulsechecker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection_manager);

        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        wifiStatesIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        p2pManager= (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        channel=p2pManager.initialize(this,getMainLooper(),null);

        p2pManager.discoverPeers(channel,new WifiP2pManager.ActionListener(){

            @Override
                    public void onSuccess(){
                Toast.makeText(ConnectionManager.this,"Discovery started",Toast.LENGTH_SHORT).show();
            }
            @Override
                    public void onFailure(int reasonCode){
                Toast.makeText(ConnectionManager.this,"Discovery couldn't be started"+reasonCode,Toast.LENGTH_SHORT).show();
            }

        });



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

    public void setIsWifiP2pEnabled(boolean wifip2pflag){
        this.isWifiP2PEnabled=wifip2pflag;
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

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peers) {

        availablePeers.clear();

        availablePeers.addAll(peers.getDeviceList());

        Log.d("PEERS-as28tuge","PEERS:"+availablePeers.toString());

    }
}


