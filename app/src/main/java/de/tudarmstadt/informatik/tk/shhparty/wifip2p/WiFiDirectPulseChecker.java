package de.tudarmstadt.informatik.tk.shhparty.wifip2p;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;

import de.tudarmstadt.informatik.tk.shhparty.R;

/**
 * Created by Ashwin on 11/28/2016.
 */

public class WiFiDirectPulseChecker  extends BroadcastReceiver{

    private WifiP2pManager.Channel channel;
    private WifiP2pManager p2pManager;
    private ConnectionManager connectionActivity;

    public WiFiDirectPulseChecker(WifiP2pManager.Channel channel, WifiP2pManager p2pManager, ConnectionManager connectionActivity) {
        super();
        this.channel = channel;
        this.p2pManager = p2pManager;
        this.connectionActivity = connectionActivity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Determine if Wifi P2P mode is enabled or not, alert
            // the Activity.
            int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
            if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                connectionActivity.setIsWifiP2pEnabled(true);
            } else {
                connectionActivity.setIsWifiP2pEnabled(false);
            }
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {

            if(p2pManager!=null){
                p2pManager.requestPeers(channel,(WifiP2pManager.PeerListListener) connectionActivity);
            }
            Log.d("as28tuge","Peer discovery sucess");

            // The peer list has changed!  We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {

            // Connection state changed!  We should probably do something about
            // that.

        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
           //Perform some action when device changes

        }
    }
}
