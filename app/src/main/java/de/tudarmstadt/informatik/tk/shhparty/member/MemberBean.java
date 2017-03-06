package de.tudarmstadt.informatik.tk.shhparty.member;

import android.graphics.Bitmap;

import java.io.Serializable;

import de.tudarmstadt.informatik.tk.shhparty.utils.SharedBox;

/**
 * Created by Ashwin on 3/2/2017.
 */

public class MemberBean implements Serializable {

    private String name;
    private Bitmap profilePicture;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }
}
