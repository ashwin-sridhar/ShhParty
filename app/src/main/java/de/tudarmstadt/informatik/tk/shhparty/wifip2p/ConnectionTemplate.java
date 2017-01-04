package de.tudarmstadt.informatik.tk.shhparty.wifip2p;

import android.app.Activity;

/**
 * Created by Ashwin on 12/3/2016.
 */

public abstract class ConnectionTemplate extends Activity {

    private boolean isWifiP2PEnabled=false;
    public void setIsWifiP2pEnabled(boolean wifip2pflag){
        this.isWifiP2PEnabled=wifip2pflag;
    }
}
