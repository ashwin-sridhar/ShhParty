package de.tudarmstadt.informatik.tk.shhparty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.tudarmstadt.informatik.tk.shhparty.wifip2p.ConnectionManager;

public class PartyInfoActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_party_info);
    }

    public void saveAndGotoConnMgr(View view){
        //// TODO: 11/28/2016 Async method call to save party info
        Intent toSelectSongsAct=new Intent(this, SelectSongsActivity.class);
        startActivity(toSelectSongsAct);
    }
}
