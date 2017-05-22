package com.example.justakiss.stoberriibeautymusic.custom;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.AlbumDetailScreen;
import com.example.justakiss.stoberriibeautymusic.activity.ArrangeScreen;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;
import com.example.justakiss.stoberriibeautymusic.activity.SingerDetailScreen;
import com.example.justakiss.stoberriibeautymusic.fragment.SongListFragment;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;

import java.io.IOException;
import java.util.Random;

/**
 * Created by justakiss on 27/09/2016.
 */
public class ProgressBar implements SeekBar.OnSeekBarChangeListener,
        MediaPlayer.OnCompletionListener {
    private Drawable mGetImage;
    private Context mContext;
    private ProgressBar x;

    public ProgressBar(Context mContext, String[] name, String[] image,
                       String[] artist, String[] playlist, int position) {
        this.mContext = mContext;
        x = this;
        MainScreen.sSeekBarPlayer.setOnSeekBarChangeListener(this);
        MainScreen.sSeekBarPlayer.setVisibility(View.VISIBLE);
        MainScreen.sNowPlayingPlaylist = playlist;
        MainScreen.sIndex = position;
        MainScreen.sTitle = name;
        MainScreen.sArtist = artist;
        MainScreen.sImage = image;
        MainScreen.sBtnPlay.setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.ic_pause));
        if(image[position]!=null) {
            MainScreen.sImgNowPlaying.setImageDrawable(getImage(image[position]));
        } else {
            MainScreen.sImgNowPlaying.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.ic_music2));
        }
        MainScreen.sTitleNowPlaying.setText(name[position]);
        MainScreen.sArtistNowPlaying.setText(artist[position]);
        MainScreen.sSeekBarPlayer.setProgress(0);
        MainScreen.sSeekBarPlayer.setMax(300);

        MainScreen.sPrevious.add(position);
        Intent svc = new Intent(mContext, MusicService.class);
        svc.putExtra("path",playlist[position]);
        mContext.startService(svc);
//        mMediaPlayer.start();
        if(MainScreen.sIsInAlbumDetail==1) {
            AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
        } else if (MainScreen.sIsInSingerDetail==1) {
            SingerDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
        } else if (MainScreen.sIsInPlayScreen == 1) {
            new ProgressBar(PlayScreen.sContextPlay);
            autoNextPlayScreen();
        } else if (MainScreen.sIsInArrangeScreen == 1){
            ArrangeScreen.sSeekBar.setOnSeekBarChangeListener(this);
        }
        MainScreen.sPause = 0;
        MusicService.sPlayer.setOnCompletionListener(this);
        updateProgressBar();
    }

    public ProgressBar(Context mContext, String name, String image,
                       String artist, String path) {
        String[] tempname = new String[1];
        String[] tempimage = new String[1];
        String[] tempartist = new String[1];
        String[] temppath = new String[1];
        tempname[0]= name;
        tempimage[0] = image;
        tempartist[0] = artist;
        temppath[0] = path;
        this.mContext = mContext;
        x = this;
        if(image!=null) {
            MainScreen.sImgNowPlaying.setImageDrawable(getImage(image));
        } else {
            MainScreen.sImgNowPlaying.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.ic_music2));
        }
//        updateProgressBar();
        new ProgressBar(mContext,tempname,tempimage,tempartist,temppath,0);
    }

    public ProgressBar(Context mContext, String[] name, String[] image,
                       String[] artist, String[] playlist, int position, int prev) {
        this.mContext = mContext;
        x = this;
        MainScreen.sSeekBarPlayer.setOnSeekBarChangeListener(this);
        MainScreen.sSeekBarPlayer.setVisibility(View.VISIBLE);
        MainScreen.sMediaPlayer.setOnCompletionListener(this);
        MediaPlayer mMediaPlayer = MainScreen.sMediaPlayer;
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(playlist[position]);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMediaPlayer.start();
        MainScreen.sBtnPlay.setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.ic_pause));
        if(image[position]!=null) {
            MainScreen.sImgNowPlaying.setImageDrawable(getImage(image[position]));
        } else {
            MainScreen.sImgNowPlaying.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.ic_music2));
        }
        MainScreen.sTitleNowPlaying.setText(name[position]);
        MainScreen.sArtistNowPlaying.setText(artist[position]);
        MainScreen.sSeekBarPlayer.setProgress(0);
        MainScreen.sSeekBarPlayer.setMax(300);
        MainScreen.sNowPlayingPlaylist = playlist;
        MainScreen.sIndex = position;
        MainScreen.sTitle = name;
        MainScreen.sArtist = artist;
        MainScreen.sImage = image;
        MainScreen.sPause = 0;
        mMediaPlayer.start();
        updateProgressBar();
    }

    public ProgressBar(String path, Context mContext) throws IOException {
        MainScreen.sSeekBarPlayer.setOnSeekBarChangeListener(this);
        MainScreen.sSeekBarPlayer.setVisibility(View.VISIBLE);
        MusicService.sPlayer.setOnCompletionListener(this);
        MusicService.sPlayer.seekTo(MainScreen.sPause);
        MusicService.sPlayer.start();
        MainScreen.sBtnPlay.setImageDrawable(
                mContext.getResources().getDrawable(R.drawable.ic_pause));
        MainScreen.sSeekBarPlayer.setProgress(0);
        MainScreen.sSeekBarPlayer.setMax(300);
        Intent svc = new Intent(mContext, MusicService.class);
        svc.putExtra("path",path);
        mContext.startService(svc);
//        mMediaPlayer.start();
        MusicService.sPlayer.setOnCompletionListener(this);
        if(MainScreen.sIsInAlbumDetail==1) {
            MainScreen.sIsInAlbumDetail = 1;
            int totalDuration = MusicService.sPlayer.getDuration();
            int currentPosition = MusicService.sPlayer.getCurrentPosition();
            AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
            if(!MusicService.sPlayer.isPlaying()){
                AlbumDetailScreen.sSeekBar.setProgress(0);
                AlbumDetailScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_play));
            } else {
                AlbumDetailScreen.sSeekBar.setProgress((
                        MainScreen.sUtils.getProgressPercentage(
                                currentPosition,totalDuration)));
                AlbumDetailScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_pause));
//                updateProgressBar();
            }
        } else if(MainScreen.sIsInSingerDetail==1) {
            MainScreen.sIsInSingerDetail = 1;
            int totalDuration = MusicService.sPlayer.getDuration();
            int currentPosition = MusicService.sPlayer.getCurrentPosition();
            SingerDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
            if(!MusicService.sPlayer.isPlaying()){
                SingerDetailScreen.sSeekBar.setProgress(0);
                SingerDetailScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_play));
            } else {
                SingerDetailScreen.sSeekBar.setProgress((
                        MainScreen.sUtils.getProgressPercentage(
                                currentPosition,totalDuration)));
                SingerDetailScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_pause));
//                updateProgressBar();
            }
        } else if(MainScreen.sIsInArrangeScreen==1) {
            MainScreen.sIsInArrangeScreen = 1;
            int totalDuration = MusicService.sPlayer.getDuration();
            int currentPosition = MusicService.sPlayer.getCurrentPosition();
            ArrangeScreen.sSeekBar.setOnSeekBarChangeListener(this);
            if(!MusicService.sPlayer.isPlaying()){
                ArrangeScreen.sSeekBar.setProgress(0);
                ArrangeScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_play));
            } else {
                ArrangeScreen.sSeekBar.setProgress((
                        MainScreen.sUtils.getProgressPercentage(
                                currentPosition,totalDuration)));
                ArrangeScreen.sPlay.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_pause));
//                updateProgressBar();
            }
        }
        updateProgressBar();
    }

    //--
    public ProgressBar(Context mContext) {
        int totalDuration = 100;
        int currentPosition = 0;
        if (MusicService.sPlayer.isPlaying()) {
            totalDuration = MusicService.sPlayer.getDuration();
            currentPosition = MusicService.sPlayer.getCurrentPosition();
        }

        if (MainScreen.sImage[MainScreen.sIndex] != null) {
            MainScreen.sImgNowPlaying.setImageDrawable(getImage(
                    MainScreen.sImage[MainScreen.sIndex]));
        } else {
            MainScreen.sImgNowPlaying.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.ic_music2));
        }
        if (MainScreen.sIsInPlayScreen == 0) {
            if (MainScreen.sIsInAlbumDetail == 1) {
                MainScreen.sIsInAlbumDetail = 1;
                AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
                if (!MusicService.sPlayer.isPlaying()) {
                    AlbumDetailScreen.sSeekBar.setProgress(0);
                    AlbumDetailScreen.sPlay.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.ic_play));
                } else {
                    AlbumDetailScreen.sSeekBar.setProgress((
                            MainScreen.sUtils.getProgressPercentage(
                                    currentPosition, totalDuration)));
                    AlbumDetailScreen.sPlay.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.ic_pause));
                    updateProgressBar();
                }
//                updateProgressBar();
            } else if (MainScreen.sIsInSingerDetail == 1) {
                MainScreen.sIsInSingerDetail = 1;
                SingerDetailScreen.sSeekBar.setOnSeekBarChangeListener(this);
                if (!MusicService.sPlayer.isPlaying()) {
                    SingerDetailScreen.sSeekBar.setProgress(0);
                    SingerDetailScreen.sPlay.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.ic_play));
                } else {
                        SingerDetailScreen.sSeekBar.setProgress((
                                MainScreen.sUtils.getProgressPercentage(
                                        currentPosition, totalDuration)));
                        SingerDetailScreen.sPlay.setImageDrawable(
                                mContext.getResources().getDrawable(R.drawable.ic_pause));
                        updateProgressBar();
                    }
            } else if (MainScreen.sIsInArrangeScreen == 1) {
                    MainScreen.sIsInArrangeScreen = 1;
                    ArrangeScreen.sSeekBar.setOnSeekBarChangeListener(this);
                    if (!MusicService.sPlayer.isPlaying()) {
                        ArrangeScreen.sSeekBar.setProgress(0);
                        ArrangeScreen.sPlay.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.ic_play));
                    } else {
                        ArrangeScreen.sSeekBar.setProgress((
                            MainScreen.sUtils.getProgressPercentage(
                                    currentPosition, totalDuration)));
                        ArrangeScreen.sPlay.setImageDrawable(
                            mContext.getResources().getDrawable(R.drawable.ic_pause));
                        updateProgressBar();
                    }
            } else {
                    MainScreen.sSeekBarPlayer.setOnSeekBarChangeListener(this);
                    MainScreen.sTitleNowPlaying.setText(MainScreen.sTitle[MainScreen.sIndex]);
                    MainScreen.sArtistNowPlaying.setText(MainScreen.sArtist[MainScreen.sIndex]);
                    if (!MusicService.sPlayer.isPlaying()) {
                        MainScreen.sBtnPlay.setImageDrawable(
                                mContext.getResources().getDrawable(R.drawable.ic_play));
                    } else {
                        MainScreen.sBtnPlay.setImageDrawable(
                                mContext.getResources().getDrawable(R.drawable.ic_pause));
                    }
                    MainScreen.sSeekBarPlayer.setVisibility(View.VISIBLE);
                    MainScreen.sSeekBarPlayer.setMax(300);
                    MainScreen.sSeekBarPlayer.setProgress((
                            MainScreen.sUtils.getProgressPercentage(
                                    currentPosition, totalDuration)));
                }
        } else if (MainScreen.sIsInPlayScreen == 1) {
                MainScreen.sIsInPlayScreen = 1;
                PlayScreen.sBtnPlayScreen.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.ic_pause_play));
                if (MainScreen.sImage[MainScreen.sIndex] != null) {
                    PlayScreen.sBg.setBackground(getBlurredImage(MainScreen.sImage[MainScreen.sIndex]));
                } else {
                    PlayScreen.sBg.setBackground(getBlurredImage2());
                }
                if (!MusicService.sPlayer.isPlaying()) {
                    PlayScreen.sCircularSeekbar.setProgress(0);
                } else {
                    PlayScreen.sCircularSeekbar.setProgress((
                            MainScreen.sUtils.getProgressPercentage(currentPosition, totalDuration)) / 3);
                }
            updateProgressBar();
        }
    }
    public ProgressBar() {
        MainScreen.sIsInPlayScreen = 1;
        PlayScreen.sCurrentTime.setText("0:00");
        PlayScreen.sBtnPlayScreen.setImageDrawable(
                PlayScreen.sContextPlay.getResources().getDrawable(R.drawable.ic_play_play));
        if(MainScreen.sImage[MainScreen.sIndex]!=null) {
            PlayScreen.sBg.setBackground(getBlurredImage(MainScreen.sImage[MainScreen.sIndex]));
        } else {
            PlayScreen.sBg.setBackground(getBlurredImage2());
        }
    }

    public ProgressBar(int progress) {
        MusicService.sPlayer.setOnCompletionListener(this);
        MainScreen.sIsInPlayScreen =1;
        PlayScreen.sCircularSeekbar.removeCallbacks(mUpdateTimeTask);
        MainScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = MusicService.sPlayer.getDuration();
        int currentPosition = MainScreen.sUtils.progressToTimer(progress, totalDuration);
        updateProgressBar();
        MusicService.sPlayer.seekTo(currentPosition);
    }

    public ProgressBar(String a) {
        MainScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        AlbumDetailScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        SingerDetailScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        ArrangeScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        int rp = MainScreen.sRepeatMode, sf = MainScreen.sShuffleMode, max;
        if(rp==0&&sf==0) {
            if(MainScreen.sIndex > MainScreen.sTitle.length - 1) {
                    new ProgressBar(MainScreen.sContext,
                            MainScreen.sTitle, MainScreen.sImage,
                            MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                            MainScreen.sIndex+1);
                SongListFragment.sSongListAdap.notifyDataSetChanged();
                if(MainScreen.sIsInPlayScreen == 1) {
                    autoNextPlayScreen();
                }
            } else {
                MainScreen.sIndex = 0;
                MainScreen.sBtnPlay.setImageDrawable(
                        MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
                if(MainScreen.sIsInPlayScreen == 1) {
                    PlayScreen.sBtnPlayScreen.setImageDrawable(
                            PlayScreen.sContextPlay.getResources().getDrawable(
                                    R.drawable.ic_play_play));
                }
            }
        } else if(rp==1&&sf==0) {
            if(MainScreen.sIndex > MainScreen.sTitle.length -1) {
                new ProgressBar(MainScreen.sContext, MainScreen.sTitle,
                            MainScreen.sImage, MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                            MainScreen.sIndex + 1);
                SongListFragment.sSongListAdap.notifyDataSetChanged();
                if(MainScreen.sIsInPlayScreen == 1){
                    autoNextPlayScreen();
                }
            } else {
                MainScreen.sIndex = 0;
                new ProgressBar(MainScreen.sContext, MainScreen.sTitle,
                            MainScreen.sImage, MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                            MainScreen.sIndex);
                SongListFragment.sSongListAdap.notifyDataSetChanged();
//                } else
                if(MainScreen.sIsInPlayScreen ==1){
                    autoNextPlayScreen();
                }
            }
        } else if((rp==0&&sf==1)||(rp==1&&sf==1)) {
            max = MainScreen.sNowPlayingPlaylist.length;
            Random r = new Random();
            int i1 = r.nextInt(max - 0) + 0;
            new ProgressBar(
                    MainScreen.sContext, MainScreen.sTitle,
                    MainScreen.sImage, MainScreen.sArtist,
                    MainScreen.sNowPlayingPlaylist, i1);
            SongListFragment.sSongListAdap.notifyDataSetChanged();
            if(MainScreen.sIsInPlayScreen == 1){
                autoNextPlayScreen();

            }
//            MainScreen.sMediaPlayer.start();
        } else if(rp==2) {
            new ProgressBar(MainScreen.sContext, MainScreen.sTitle,
                    MainScreen.sImage, MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                    MainScreen.sIndex);
            SongListFragment.sSongListAdap.notifyDataSetChanged();
            if(MainScreen.sIsInPlayScreen ==1){
                autoNextPlayScreen();
            }
        } else {
            Log.e("error",":OnCompletion Error");
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
    }
    /**
     * Update timer on seekbar
     * */
    public void updateProgressBar() {
        if(MainScreen.sIsInPlayScreen == 0) {
            if(MainScreen.sIsInAlbumDetail == 1){
                AlbumDetailScreen.sSeekBarHandler.postDelayed(mUpdateTimeTask, 100);
            } else if(MainScreen.sIsInArrangeScreen == 1) {
                ArrangeScreen.sSeekBarHandler.postDelayed(mUpdateTimeTask, 100);
            } else if(MainScreen.sIsInSingerDetail == 1){
                SingerDetailScreen.sSeekBarHandler.postDelayed(mUpdateTimeTask, 100);
            } else if(MainScreen.sIsInAlbumDetail == 0) {
                MainScreen.sSeekBarHandler.postDelayed(mUpdateTimeTask, 100);
            }
        } else if(MainScreen.sIsInPlayScreen == 1) {
            PlayScreen.sCurrentTime.postDelayed(mUpdateTimeTask, 100);
            Log.e("inPlay","value"+MainScreen.sIsInPlayScreen);
        }

    }

    /**
     * Background Runnable thread
     * */
    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            long totalDuration = MusicService.sPlayer.getDuration();
            long currentDuration = MusicService.sPlayer.getCurrentPosition();
            if(MainScreen.sIsInPlayScreen == 1) {
                PlayScreen.sCurrentTime.setText(
                        (MainScreen.sUtils.milliSecondsToTimer(currentDuration)));
                int progress = (MainScreen.sUtils.getProgressPercentage(
                        currentDuration, totalDuration));
                PlayScreen.sCircularSeekbar.setProgress(progress);
                // Running this thread after 100 milliseconds
                PlayScreen.sCircularSeekbar.postDelayed(this, 100);
            } else if (MainScreen.sIsInPlayScreen == 0) {
                if(MainScreen.sIsInAlbumDetail == 1){
                    AlbumDetailScreen.sTimeLeft.setText("-"+
                            (MainScreen.sUtils.milliSecondsToTimer(totalDuration-currentDuration)));
                    AlbumDetailScreen.sNowPlayingTitle.setText(
                            MainScreen.sTitle[MainScreen.sIndex]);
                    AlbumDetailScreen.sNowPlayingArtist.setText(
                            MainScreen.sArtist[MainScreen.sIndex]);
                    if(MainScreen.sImage[MainScreen.sIndex]!=null) {
                        AlbumDetailScreen.sNowPlayingImg.setImageDrawable(getImage(
                                MainScreen.sImage[MainScreen.sIndex]));
                    } else {
                        AlbumDetailScreen.sNowPlayingImg.setImageDrawable(
                                AlbumDetailScreen.sContext.getResources().getDrawable(R.drawable.ic_music2));
                    }
                    if(!MusicService.sPlayer.isPlaying()) {
                        AlbumDetailScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
                    } else {
                        AlbumDetailScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_pause));
                    }
//                    AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(x);
                    // Updating progress bar
                    int progress = (MainScreen.sUtils.getProgressPercentage(
                            currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    AlbumDetailScreen.sSeekBar.setProgress(progress);
                    // Running this thread after 100 milliseconds
//                    AlbumDetailScreen.sSeekBarHandler.postDelayed(this, 100);
                    updateProgressBar();
                } else if(MainScreen.sIsInArrangeScreen == 1){
                    ArrangeScreen.sTimeLeft.setText("-"+
                            (MainScreen.sUtils.milliSecondsToTimer(totalDuration-currentDuration)));
                    ArrangeScreen.sNowPlayingTitle.setText(
                            MainScreen.sTitle[MainScreen.sIndex]);
                    ArrangeScreen.sNowPlayingArtist.setText(
                            MainScreen.sArtist[MainScreen.sIndex]);
                    if(MainScreen.sImage[MainScreen.sIndex]!=null) {
                        ArrangeScreen.sNowPlayingImg.setImageDrawable(getImage(
                                MainScreen.sImage[MainScreen.sIndex]));
                    } else {
                        ArrangeScreen.sNowPlayingImg.setImageDrawable(
                                ArrangeScreen.sContext.getResources().getDrawable(R.drawable.ic_music2));
                    }
                    if(!MusicService.sPlayer.isPlaying()) {
                        ArrangeScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
                    } else {
                        ArrangeScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_pause));
                    }
//                    AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(x);
                    // Updating progress bar
                    int progress = (MainScreen.sUtils.getProgressPercentage(
                            currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    ArrangeScreen.sSeekBar.setProgress(progress);
                    // Running this thread after 100 milliseconds
//                    AlbumDetailScreen.sSeekBarHandler.postDelayed(this, 100);
                    updateProgressBar();
                } else if(MainScreen.sIsInSingerDetail == 1) {
                    SingerDetailScreen.sTimeLeft.setText("-"+
                            (MainScreen.sUtils.milliSecondsToTimer(totalDuration-currentDuration)));
                    SingerDetailScreen.sNowPlayingTitle.setText(
                            MainScreen.sTitle[MainScreen.sIndex]);
                    SingerDetailScreen.sNowPlayingArtist.setText(
                            MainScreen.sArtist[MainScreen.sIndex]);
                    if(MainScreen.sImage[MainScreen.sIndex]!=null) {
                        SingerDetailScreen.sNowPlayingImg.setImageDrawable(getImage(
                                MainScreen.sImage[MainScreen.sIndex]));
                    } else {
                        SingerDetailScreen.sNowPlayingImg.setImageDrawable(
                                SingerDetailScreen.sContext.getResources().getDrawable(R.drawable.ic_music2));
                    }
                    if(!MusicService.sPlayer.isPlaying()) {
                        SingerDetailScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
                    } else {
                        SingerDetailScreen.sPlay.setImageDrawable(
                                MainScreen.sContext.getResources().getDrawable(R.drawable.ic_pause));
                    }
//                    AlbumDetailScreen.sSeekBar.setOnSeekBarChangeListener(x);
                    // Updating progress bar
                    int progress = (MainScreen.sUtils.getProgressPercentage(
                            currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    SingerDetailScreen.sSeekBar.setProgress(progress);
                    // Running this thread after 100 milliseconds
//                    AlbumDetailScreen.sSeekBarHandler.postDelayed(this, 100);
                    updateProgressBar();
                } else if(MainScreen.sIsInAlbumDetail == 0) {
                    MainScreen.sTimeLeft.setText("-"+
                            (MainScreen.sUtils.milliSecondsToTimer(totalDuration-currentDuration)));
                    if(MainScreen.sTitleNowPlaying.getText().toString().matches("Unknown")) {
                        MainScreen.sTitleNowPlaying.setText(MainScreen.sTitle[MainScreen.sIndex]);
                        MainScreen.sArtistNowPlaying.setText(MainScreen.sArtist[MainScreen.sIndex]);
                        if(MainScreen.sImage[MainScreen.sIndex]!=null) {
                            MainScreen.sImgNowPlaying.setImageDrawable(
                                    getImage(MainScreen.sImage[MainScreen.sIndex]));
                        } else {
                            MainScreen.sImgNowPlaying.setImageDrawable(
                                    MainScreen.sContext.getResources().getDrawable(R.drawable.ic_music2));
                        }
                        if(!MusicService.sPlayer.isPlaying()) {
                            MainScreen.sBtnPlay.setImageDrawable(
                                    MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
                        } else {
                            MainScreen.sBtnPlay.setImageDrawable(
                                    MainScreen.sContext.getResources().getDrawable(R.drawable.ic_pause));
                        }
                        MainScreen.sSeekBarPlayer.setOnSeekBarChangeListener(x);
                        MainScreen.sSeekBarPlayer.setVisibility(View.VISIBLE);
                        MainScreen.sSeekBarPlayer.setMax(300);
                        updateProgressBar();
                    }
                    // Updating progress bar
                    int progress = (int)(MainScreen.sUtils.getProgressPercentage(
                            currentDuration, totalDuration));
                    //Log.d("Progress", ""+progress);
                    MainScreen.sSeekBarPlayer.setProgress(progress);
                    // Running this thread after 100 milliseconds
                    MainScreen.sSeekBarHandler.postDelayed(this, 100);
                }
            }
        }
    };

    /**
     * When user starts moving the progress handler
     * */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        // remove message Handler from updating progress bar

        if(MainScreen.sIsInAlbumDetail == 0) {
            MainScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        } else {
//            AlbumDetailScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
//            MainScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        }
    }
    /**
     * When user stops moving the progress hanlder
     * */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        MainScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        if(MainScreen.sIsInAlbumDetail==1){
            AlbumDetailScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        } else if (MainScreen.sIsInArrangeScreen == 1) {
           ArrangeScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        } else if(MainScreen.sIsInSingerDetail==1) {
            SingerDetailScreen.sSeekBarHandler.removeCallbacks(mUpdateTimeTask);
        } else if(MainScreen.sIsInAlbumDetail==0) {
        }
        int totalDuration = MusicService.sPlayer.getDuration();
        int currentPosition = MainScreen.sUtils.progressToTimer(
                seekBar.getProgress(), totalDuration);
        updateProgressBar();
        MusicService.sPlayer.seekTo(currentPosition);
    }
    public Drawable getBlurredImage(String path) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 2;
        Bitmap bitmap1 = BitmapFactory.decodeFile(path, o);
        Bitmap blurred = BlurBuilder.blur(PlayScreen.sContextPlay,bitmap1);
        mGetImage = new BitmapDrawable(blurred);
        return mGetImage;
    }
    public Drawable getBlurredImage2() {
        Drawable drawable = PlayScreen.sContextPlay.getResources().getDrawable(R.drawable.ic_music2);
        Bitmap mutableBitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, 400, 400);
        drawable.draw(canvas);
//        return mutableBitmap;
//        Bitmap bitmap = BitmapFactory.decodeResource(PlayScreen.sContextPlay.getResources(), R.drawable.2);
        Bitmap blurred = BlurBuilder.blur(PlayScreen.sContextPlay,mutableBitmap);
        mGetImage = new BitmapDrawable(blurred);
        return mGetImage;
    }

    public Drawable getImage(String path) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 3;
        Bitmap bitmap1 = BitmapFactory.decodeFile(path, o);
        mGetImage = new BitmapDrawable(bitmap1);
        return mGetImage;
    }
    public void autoNextPlayScreen() {
//        Intent intent1 = new Intent(PlayScreen.sContextPlay, PlayScreen.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PlayScreen.sContextPlay.startActivity(intent1);
        PlayScreen.sStart = 0;
        PlayScreen.sPlayingTitle.setText(MainScreen.sTitleNowPlaying.getText().toString());
        PlayScreen.sPlayingArtist.setText(MainScreen.sArtistNowPlaying.getText().toString());
        MainScreen.sIsInPlayScreen = 1;
        new ProgressBar(PlayScreen.sContextPlay);
        PlayScreen.rvPlaylist.setLayoutManager(new LinearLayoutManager( PlayScreen.sContextPlay));
        PlayScreen.rvPlaylist.scrollToPosition(MainScreen.sIndex);
    }
    public void gotoPlay(Context c) {
        Intent intent1 = new Intent(c, PlayScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent1);
    }
    public void gotoMain(Context c) {
        Intent intent1 = new Intent(c, MainScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        c.startActivity(intent1);
    }
}
