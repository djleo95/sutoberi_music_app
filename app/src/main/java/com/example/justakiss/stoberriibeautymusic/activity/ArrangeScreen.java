package com.example.justakiss.stoberriibeautymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.adapter.ArrangeScreenAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.ItemTouchHelperAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.SingerDetailAdap;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;
import com.example.justakiss.stoberriibeautymusic.handler.OnSwipeTouchListener;
import com.example.justakiss.stoberriibeautymusic.handler.SimpleItemTouchHelperCallback;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.Random;

/**
 * Created by justakiss on 01/11/2016.
 */
public class ArrangeScreen extends AppCompatActivity{

    ItemTouchHelper touchHelper;
    public static TextView sNowPlayingTitle, sNowPlayingArtist, sTimeLeft;
    public static ImageView sNowPlayingImg;
    public static ImageButton sRepeat, sShuffle, sPlay;
    public static SeekBar sSeekBar;
    public static Handler sSeekBarHandler = new Handler();
    public static Context sContext;
    public static View sNowPlayingActionZone;
    private RecyclerView mRvArrange;
    private ArrangeScreenAdapter mArrangeAdap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arrange_screen);
        MainScreen.sIsInArrangeScreen = 1;
        mRvArrange = (RecyclerView) findViewById(R.id.rv_ArrangeScreen);
        mArrangeAdap = new ArrangeScreenAdapter(getApplicationContext());
        mRvArrange.setAdapter(mArrangeAdap);
        mRvArrange.setLayoutManager(new GridLayoutManager(getApplicationContext(),1));
        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(mArrangeAdap);
        touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRvArrange);
        mArrangeAdap.setOnItemClickListener(new ArrangeScreenAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new ProgressBar(getApplicationContext(),MainScreen.sTitle,
                        MainScreen.sImage, MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,position);
            }
        });
        initView();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
    public void initView() {
        sContext = getApplicationContext();
        sNowPlayingImg = (ImageView) findViewById(R.id.iv_nowplaying4);
        sNowPlayingTitle = (TextView) findViewById(R.id.tv_name_nowplaying4);
        sNowPlayingArtist = (TextView) findViewById(R.id.tv_artist_nowplaying4);
        sTimeLeft = (TextView) findViewById(R.id.tv_timeleft4);
        sRepeat = (ImageButton) findViewById(R.id.btn_repeat4);
        sShuffle = (ImageButton) findViewById(R.id.btn_shuffle4);
        sPlay = (ImageButton) findViewById(R.id.btn_play4);
        sSeekBar = (SeekBar) findViewById(R.id.songProgressBar4);
        sNowPlayingActionZone = findViewById(R.id.ll_now_playing_action_zone4);
        sNowPlayingTitle.setText(MainScreen.sTitle[MainScreen.sIndex]);
        sNowPlayingArtist.setText(MainScreen.sArtist[MainScreen.sIndex]);
        sSeekBar.setProgress(0);
        sSeekBar.setMax(300);
        if(MainScreen.sImage[MainScreen.sIndex]!=null){
            sNowPlayingImg.setImageDrawable(
                    getImage(MainScreen.sImage[MainScreen.sIndex],100,100));
        } else {
            sNowPlayingImg.setImageDrawable(
                    getApplicationContext().getResources().getDrawable(R.drawable.ic_music2));
        }
        setupButton();
        new ProgressBar(getApplicationContext());
        sNowPlayingActionZone.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                gotoPlayScreen();
            }
            public void onSwipeRight() {
                playPrevious();
            }
            public void onSwipeLeft() {
                playNext();
            }
            public void onSwipeBottom() {
                Toast.makeText(getApplicationContext(), "Under Construct!", Toast.LENGTH_SHORT).show();
            }
        });
        sNowPlayingImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoPlayScreen();
            }
        });
    }
    public void setupButton(){
        if(MainScreen.sRepeatMode == 0) {
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
        } else if(MainScreen.sRepeatMode ==1) {
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
        }
        else if(MainScreen.sRepeatMode ==2){
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
        }
        if(MainScreen.sShuffleMode == 0) {
            sShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
        } else if (MainScreen.sShuffleMode ==1){
            sShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
        }
    }
    public Drawable getImage(String path, int size1, int size2) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), size2, size1);
        return new BitmapDrawable(bitmap1);
    }
    public void playSong(View view) throws IOException {
        if (!MusicService.sPlayer.isPlaying()) {
//            sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            if (!(MainScreen.sNowPlayingPlaylist == null)) {
                if(MainScreen.sPause !=0) {
                    new ProgressBar(
                            MainScreen.sNowPlayingPlaylist[MainScreen.sIndex],this);
                } else {
                    new ProgressBar(this, MainScreen.sTitle,
                            MainScreen.sImage, MainScreen.sArtist,
                            MainScreen.sNowPlayingPlaylist, MainScreen.sIndex);
                }
            }
        } else if (MusicService.sPlayer.isPlaying()) {
            MusicService.sPlayer.pause();
            MainScreen.sPause = MusicService.sPlayer.getCurrentPosition();
            MainScreen.sBtnPlay.setImageDrawable(
                    MainScreen.sContext.getResources().getDrawable(R.drawable.ic_play));
            sPlay.setImageDrawable(sContext.getResources().getDrawable(R.drawable.ic_play));
        }
    }

    public void clickRepeat (View view) {
        if(MainScreen.sRepeatMode == 0) {
            MainScreen.sRepeatMode = 1;
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
        } else if(MainScreen.sRepeatMode == 1) {
            MainScreen.sRepeatMode = 2;
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
        }
        else if(MainScreen.sRepeatMode == 2){
            MainScreen.sRepeatMode = 0;
            sRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
        }
        MainScreen.editor = getSharedPreferences(MainScreen.MY_PREFS_NAME, MODE_PRIVATE).edit();
        MainScreen.editor.putInt("Repeat Mode", MainScreen.sRepeatMode);
        MainScreen.editor.putInt("Shuffle Mode", MainScreen.sShuffleMode);
        MainScreen.editor.commit();
    }

    public void clickShuffle (View view) {
        if(MainScreen.sShuffleMode == 0) {
            MainScreen.sShuffleMode = 1;
            sShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
        } else if(MainScreen.sShuffleMode == 1){
            MainScreen.sShuffleMode = 0;
            sShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
        }
        MainScreen.editor = getSharedPreferences(MainScreen.MY_PREFS_NAME, MODE_PRIVATE).edit();
        MainScreen.editor.putInt("Shuffle Mode", MainScreen.sShuffleMode);
        MainScreen.editor.putInt("Repeat Mode", MainScreen.sRepeatMode);
        MainScreen.editor.commit();
    }
    public void playNext() {
        MusicService.sPlayer.reset();
        MainScreen.sPrevIndex =1;
        if(MainScreen.sShuffleMode ==0) {
            if(MainScreen.sIndex < MainScreen.sTitle.length -1) {
                new ProgressBar(this, MainScreen.sTitle,
                        MainScreen.sImage, MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist, ++MainScreen.sIndex);
            } else {
                MainScreen.sIndex = 0;
                new ProgressBar(this, MainScreen.sTitle,
                        MainScreen.sImage, MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist, MainScreen.sIndex);
            }
        } else if(MainScreen.sShuffleMode == 1) {
            int max = MainScreen.sNowPlayingPlaylist.length;
            Random r = new Random();
            int i1 = r.nextInt(max - 0) + 0;
            new ProgressBar(this, MainScreen.sTitle,
                    MainScreen.sImage, MainScreen.sArtist,
                    MainScreen.sNowPlayingPlaylist, i1);
        }
    }

    public void playPrevious() {
        MusicService.sPlayer.reset();
        if(MainScreen.sShuffleMode==0){
            if(MainScreen.sPrevious.size()<2) {
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
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index);
            }
        } else if(MainScreen.sShuffleMode==1) {
            if(MainScreen.sPrevious.size()<2) {
                int max = MainScreen.sNowPlayingPlaylist.length;
                Random r = new Random();
                int i1 = r.nextInt(max - 0) + 0;
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,i1);
            } else {
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        this,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index);
            }
        }
    }
    public void gotoPlayScreen() {
        Context c = getApplicationContext();
        Intent intent1 = new Intent(c, PlayScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent1);
    }
}
