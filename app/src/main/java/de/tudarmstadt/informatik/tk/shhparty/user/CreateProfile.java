package de.tudarmstadt.informatik.tk.shhparty.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import de.tudarmstadt.informatik.tk.shhparty.PartyInfoActivity;
import de.tudarmstadt.informatik.tk.shhparty.R;

public class CreateProfile extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
    }

    public void saveAndGotoPartyInfo(View view){

        // TODO: 11/28/2016 async method to save image and user's name
        Intent intentToPartyInfo = new Intent(this,PartyInfoActivity.class);
        startActivity(intentToPartyInfo);

    }
}
