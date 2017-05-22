package com.example.justakiss.stoberriibeautymusic.object;

import android.graphics.Color;

import java.util.ArrayList;

/**
 * Created by justakiss on 20/09/2016.
 */
public class Song {
    //private variables
    private String mPath, mTitle, mArtist, mAlbum, mArt = null;
    private int mAlbumId,mTrackId, mId;
    private long mDuration;
    // Empty constructor
    public Song() {}
    // constructor
    public Song(String mPath, String mTitle, String mAlbum, String mArtist,
                int mAlbumId, int mTrackId, long mDuration, String mArt) {
        this.mPath = mPath;
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mAlbum = mAlbum;
        this.mAlbumId = mAlbumId;
        this.mTrackId = mTrackId;
        this.mDuration = mDuration;
        this.mArt = mArt;
    }
    public Song(int mId, String mPath, String mTitle, String mAlbum, String mArtist,
                int mAlbumId, int mTrackId, long mDuration, String mArt) {
        this.mId = mId;
        this.mPath = mPath;
        this.mTitle = mTitle;
        this.mArtist = mArtist;
        this.mAlbum = mAlbum;
        this.mAlbumId = mAlbumId;
        this.mTrackId = mTrackId;
        this.mDuration = mDuration;
        this.mArt = mArt;
    }

    // Set and Get
    public void setId(int mId) {
        this.mId = mId;
    }
    public int getId() {
        return this.mId;
    }
    public void setPath(String mPath){
        this.mPath = mPath;
    }
    public String getPath(){
        return this.mPath;
    }
    public void setTitle(String mTitle){
        this.mTitle = mTitle;
    }
    public String getTitle(){
        return this.mTitle;
    }
    public void setArtist(String mArtist){
        this.mArtist = mArtist;
    }
    public String getArtist(){
        return this.mArtist;
    }
    public void setAlbum(String mAlbum){
        this.mAlbum = mAlbum;
    }
    public String getAlbum(){
        return this.mAlbum;
    }
    public void setAlbumId(int mAlbumId){
        this.mAlbumId = mAlbumId;
    }
    public int getAlbumId(){
        return this.mAlbumId;
    }
    public void setTrackId(int mTrackId){
        this.mTrackId = mTrackId;
    }
    public int getTrackId(){
        return this.mTrackId;
    }
    public void setDuration(long mDuration) {
        this.mDuration = mDuration;
    }
    public long getDuration() {
        return this.mDuration;
    }
    public void setImage(String mArt) {
        this.mArt = mArt;
    }
    public String getImage() {
        return this.mArt;
    }
}
