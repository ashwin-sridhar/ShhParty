package de.tudarmstadt.informatik.tk.shhparty.music;

/**
 * Created by Ashwin on 11/23/2016.
 */

public class MusicBean {


    private long musicID;
    private String musicTitle;
    private String artist;

    public MusicBean(long musicID,String musicTitle,String artist) {
        this.musicID = musicID;
        this.musicTitle=musicTitle;
        this.artist=artist;
    }

    public long getMusicID() {
        return musicID;
    }

    public void setMusicID(long musicID) {
        this.musicID = musicID;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getMusicTitle() {
        return musicTitle;
    }

    public void setMusicTitle(String musicTitle) {
        this.musicTitle = musicTitle;
    }


}
