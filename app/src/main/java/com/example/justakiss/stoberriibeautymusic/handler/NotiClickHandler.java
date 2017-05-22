package com.example.justakiss.stoberriibeautymusic.handler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;
import com.example.justakiss.stoberriibeautymusic.custom.CustomNotification;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;

import java.util.Random;

/**
 * Created by justakiss on 03/11/2016.
 */
public class NotiClickHandler extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int mode = intent.getExtras().getInt("noti");
        if(mode == 0) {
            if(MusicService.sPlayer.isPlaying()){
                MusicService.sPlayer.pause();
                MainScreen.sPause = MusicService.sPlayer.getCurrentPosition();
                new CustomNotification(getApplicationContext(),1);
                MainScreen.sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                if(MainScreen.sIsInPlayScreen==1){
                    PlayScreen.sBtnPlayScreen.setImageDrawable(
                            PlayScreen.sContextPlay.getResources().getDrawable(R.drawable.ic_play_play));
                }
            } else {
//                MusicService.sPlayer.seekTo(MainScreen.sPause);
//                MusicService.sPlayer.start();
//                MainScreen.sPause = 0;
                Intent svc = new Intent(getApplicationContext(), MusicService.class);
                svc.putExtra("path","play");
                getApplicationContext().stopService(svc);
                getApplicationContext().startService(svc);
//                new CustomNotification(getApplicationContext(),1);
                MainScreen.sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                if(MainScreen.sIsInPlayScreen==1) {
                    PlayScreen.sBtnPlayScreen.setImageDrawable(
                            PlayScreen.sContextPlay.getResources().getDrawable(R.drawable.ic_pause_play));
                }
            }
//            gotoMain();
        } else if(mode == 1) {
//            new ProgressBar(this, MainScreen.sTitle, MainScreen.sImage, MainScreen.sArtist,
//                    MainScreen.sNowPlayingPlaylist, MainScreen.sIndex+1);
//            gotoMain();
            playNext();
        } else if(mode == 2) {
//            new ProgressBar(this, MainScreen.sTitle, MainScreen.sImage, MainScreen.sArtist,
//                    MainScreen.sNowPlayingPlaylist, MainScreen.sIndex-1);
            playPrevious();
//            Toast.makeText(this,"next",Toast.LENGTH_SHORT).show();
//            gotoMain();
        } else if(mode == 3) {
            gotoPlay();
        }
        return 1;
    }
    public void gotoPlay() {
        Context c = getApplicationContext();
        Intent intent1 = new Intent(c, PlayScreen.class);
//        intent1.putExtra()
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent1);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        //unregister listeners
        //do any other cleanup if required
//        Toast.makeText(getApplicationContext(),"onTaskRemoved",Toast.LENGTH_SHORT).show();
        //stop service
        new CustomNotification(getApplicationContext(),1);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Toast.makeText(this,"ondestroynoti",Toast.LENGTH_SHORT).show();
    }

    public void playNext() {
        MusicService.sPlayer.reset();
        MainScreen.sPrevIndex =1;
        if(MainScreen.sShuffleMode ==0) {
            if(MainScreen.sIndex < MainScreen.sTitle.length-1) {
                new ProgressBar(this, MainScreen.sTitle, MainScreen.sImage,
                        MainScreen.sArtist, MainScreen.sNowPlayingPlaylist, MainScreen.sIndex+1);
            } else {
                MainScreen.sIndex = 0;
                new ProgressBar(this, MainScreen.sTitle, MainScreen.sImage,
                        MainScreen.sArtist, MainScreen.sNowPlayingPlaylist, MainScreen.sIndex);
            }
        } else if(MainScreen.sShuffleMode ==1) {
            int max = MainScreen.sNowPlayingPlaylist.length;
            Random r = new Random();
            int i1 = r.nextInt(max - 0) + 0;
            new ProgressBar(this, MainScreen.sTitle, MainScreen.sImage,
                    MainScreen.sArtist, MainScreen.sNowPlayingPlaylist, i1);
        }
    }

    public void playPrevious() {
        MusicService.sPlayer.reset();
        if(MainScreen.sShuffleMode==0){
            if(MainScreen.sPrevious.size()<2) {
//                Log.e("thutu_size ","is:"+sPrevious.size());
                if(MainScreen.sIndex == 0) {
                    MainScreen.sIndex = MainScreen.sNowPlayingPlaylist.length-1 ;
                    new ProgressBar(
                            this, MainScreen.sTitle,
                            MainScreen.sImage, MainScreen.sArtist,
                            MainScreen.sNowPlayingPlaylist, MainScreen.sIndex);
                } else {
                    MainScreen.sIndex -= 1;
                    new ProgressBar(
                            this, MainScreen.sTitle, MainScreen.sImage,
                            MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                            MainScreen.sIndex);
                }
            } else {
//                Log.e("size1 ","is:"+sPrevious.size());
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index);
            }
        } else if(MainScreen.sShuffleMode==1) {
            if(MainScreen.sPrevious.size()<2) {
//                Log.e("random_size ","is:"+sPrevious.size());
                int max = MainScreen.sNowPlayingPlaylist.length;
                Random r = new Random();
                int i1 = r.nextInt(max - 0) + 0;
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,i1);
            } else {
//                Log.e("size2 ","is:"+sPrevious.size());
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index);
            }
        }
    }
}
