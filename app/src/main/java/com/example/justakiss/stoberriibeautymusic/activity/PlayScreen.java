package com.example.justakiss.stoberriibeautymusic.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.justakiss.stoberriibeautymusic.adapter.AutocompleteCustomArrayAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.PlayScreenListAdapter;
import com.example.justakiss.stoberriibeautymusic.custom.CircularSeekBar;
import com.example.justakiss.stoberriibeautymusic.custom.CustomNotification;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.handler.CustomAutoCompleteListener4;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;

import java.util.List;
import java.util.Random;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by justakiss on 30/09/2016.
 */
public class PlayScreen extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageButton mBtnShuffle, mBtnRepeat, mBtnNext, mBtnPrev;
    private int mFirstTime = 0;
    private Menu menu;
    private SongDBHandler mDB;
    public static TextView sCurrentTime, sToolbarTitlePlayScreen;
    public static ImageButton sBtnPlayScreen, sBack, sBack2, sClose, sSearch;
    public static CircularSeekBar sCircularSeekbar;
    public static Context sContextPlay;
    public static View sBg;
    public static int sStart = 0;
    public static RecyclerView rvPlaylist;
    public static TextView sPlayingTitle, sPlayingArtist;
    public static AutoCompleteTextView sAutoCompletePlayScreen;
    public static AutocompleteCustomArrayAdapter sAutoAdapPlayScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        sPlayingTitle = (TextView) findViewById(R.id.tv_name_play);
        sPlayingArtist = (TextView) findViewById(R.id.tv_artist_play);
        sCurrentTime = (TextView) findViewById(R.id.tv_time);
        sBtnPlayScreen = (ImageButton) findViewById(R.id.btn_play_play);
        sCircularSeekbar = (CircularSeekBar) findViewById(R.id.sb_circular);
        sBg = findViewById(R.id.ll_playingscreen_upper);
        mBtnShuffle = (ImageButton) findViewById(R.id.btn_shuffle_play);
        mBtnRepeat = (ImageButton) findViewById(R.id.btn_repeat_play);
        mBtnNext = (ImageButton) findViewById(R.id.btn_next);
        mBtnPrev = (ImageButton) findViewById(R.id.btn_previous);
        sToolbarTitlePlayScreen = (TextView) findViewById(R.id.toolbar2_label);
        sToolbarTitlePlayScreen.setVisibility(View.GONE);
        sBack = (ImageButton) findViewById(R.id.btn_back2);
        sBack2 = (ImageButton) findViewById(R.id.btn_back2_2);
        sClose = (ImageButton) findViewById(R.id.btn_close2);
        sSearch = (ImageButton) findViewById(R.id.btn_search2);
        sSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSearch();
            }
        });
        sAutoCompletePlayScreen = (AutoCompleteTextView) findViewById(R.id.myautocomplete2);
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playNext();
            }
        });
        mBtnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPrev();
            }
        });
        sContextPlay = getApplicationContext();
        initView();
//        setupSearch();
        sBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainScreen();
            }
        });
//        sBtnPlayScreen.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        TODO
    }
    @Override
    protected void onStop() {
        super.onStop();
        MainScreen.sIsInPlayScreen =0;
    }
    @Override
    public void onBackPressed() {
        MainScreen.sIsInPlayScreen = 0;
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_play, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_arrange:
                Context c = getApplicationContext();
                Intent intent1 = new Intent(c, ArrangeScreen.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                c.startActivity(intent1);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void initView() {
        sStart = 0;
        sPlayingTitle.setText(MainScreen.sTitleNowPlaying.getText().toString());
        sPlayingArtist.setText(MainScreen.sArtistNowPlaying.getText().toString());
        MainScreen.sIsInPlayScreen = 1;
        setButton();
        if((!MusicService.sPlayer.isPlaying())&&(MainScreen.sPause==0)&&(mFirstTime==0)){
            new ProgressBar();
            mFirstTime=1;
        } else if(!MusicService.sPlayer.isPlaying()) {
            new ProgressBar();
        } else {
            new ProgressBar(sContextPlay);
        }
        if(!MusicService.sPlayer.isPlaying()){
            sBtnPlayScreen.setBackground(getResources().getDrawable(R.drawable.ic_play_play));
        } else {
            sBtnPlayScreen.setBackground(getResources().getDrawable(R.drawable.ic_pause_play));
        }
        rvPlaylist = (RecyclerView) findViewById(R.id.rv_PlayScreenList);
        PlayScreenListAdapter playListAdap = new PlayScreenListAdapter(
                sContextPlay,
                MainScreen.sTitle,
                MainScreen.sArtist,
                MainScreen.sImage,
                MainScreen.sNowPlayingPlaylist);
        rvPlaylist.setAdapter(playListAdap);
        rvPlaylist.setLayoutManager(new LinearLayoutManager(sContextPlay));
        rvPlaylist.setHasFixedSize(true);
        rvPlaylist.setItemAnimator(new SlideInUpAnimator());
        rvPlaylist.scrollToPosition(MainScreen.sIndex);
        playListAdap.setOnItemClickListener(new PlayScreenListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,position);
                MainScreen.sIndex = position;
                Log.e("ICON", "clicked");
                initView();
                rvPlaylist.scrollToPosition(position);
                sBtnPlayScreen.setImageDrawable(
                        sContextPlay.getResources().getDrawable(R.drawable.ic_pause_play));
            }
        });
    }

    public static void gotoMainScreen() {
        MainScreen.sIsInPlayScreen = 0;
        Intent intent1 = new Intent(sContextPlay, MainScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        sContextPlay.startActivity(intent1);
    }

    public void setButton() {
        if(MainScreen.sShuffleMode ==0) {
            mBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
        } else if(MainScreen.sShuffleMode ==1) {
            mBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
        }
        if(MainScreen.sRepeatMode ==0) {
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
        } else if(MainScreen.sRepeatMode ==1) {
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
        } else if(MainScreen.sRepeatMode ==2) {
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
        }
    }
    public void playSongInPlayScreen(View view) {
        if(!MusicService.sPlayer.isPlaying()&&MainScreen.sPause == 0){
            new ProgressBar(
                    sContextPlay,MainScreen.sTitle,
                    MainScreen.sImage,MainScreen.sArtist,
                    MainScreen.sNowPlayingPlaylist,MainScreen.sIndex);
            initView();
            sBtnPlayScreen.setImageDrawable(
                    sContextPlay.getResources().getDrawable(R.drawable.ic_pause_play));
            Log.e("ICON", "clicked");
        } else if(!MusicService.sPlayer.isPlaying()){
//            new ProgressBar(sContextPlay);
//            MusicService.sPlayer.seekTo(MainScreen.sPause);
//            MusicService.sPlayer.start();
            new ProgressBar(
                    sContextPlay,MainScreen.sTitle,
                    MainScreen.sImage,MainScreen.sArtist,
                    MainScreen.sNowPlayingPlaylist,MainScreen.sIndex);
            MainScreen.sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            sBtnPlayScreen.setImageDrawable(
                    sContextPlay.getResources().getDrawable(R.drawable.ic_pause_play));
        }
        else {
            sBtnPlayScreen.setImageDrawable(
                    sContextPlay.getResources().getDrawable(R.drawable.ic_play_play));
            MusicService.sPlayer.pause();
            MainScreen.sPause =  MusicService.sPlayer.getCurrentPosition();
            MainScreen.sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            new CustomNotification(getApplicationContext(),1);
        }
    }
    public void playNext() {
        MusicService.sPlayer.reset();
        MainScreen.sPrevIndex =1;
        if(MainScreen.sShuffleMode == 0) {
            if(MainScreen.sIndex < MainScreen.sNowPlayingPlaylist.length-1) {
                MainScreen.sIndex += 1;
                 new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,MainScreen.sIndex);
                initView();
            } else {
                MainScreen.sIndex = 0;
                new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,MainScreen.sIndex);
                initView();
            }
        } else if(MainScreen.sShuffleMode ==1) {
            int max = MainScreen.sNowPlayingPlaylist.length;
            Random r = new Random();
            int i1 = r.nextInt(max - 0) + 0;
            new ProgressBar(
                    sContextPlay,MainScreen.sTitle,
                    MainScreen.sImage,MainScreen.sArtist,
                    MainScreen.sNowPlayingPlaylist,i1);
            initView();
        }
    }
    public void playPrev() {
        MusicService.sPlayer.reset();
        if(MainScreen.sShuffleMode==0){
            if(MainScreen.sPrevious.size()<2) {
                if(MainScreen.sIndex == 0) {
                    MainScreen.sIndex = MainScreen.sNowPlayingPlaylist.length-1 ;
                    new ProgressBar(
                            this, MainScreen.sTitle,
                            MainScreen.sImage, MainScreen.sArtist,
                            MainScreen.sNowPlayingPlaylist, MainScreen.sIndex);
                    initView();
                } else {
                    MainScreen.sIndex -= 1;
                    new ProgressBar(
                        this, MainScreen.sTitle, MainScreen.sImage,
                        MainScreen.sArtist, MainScreen.sNowPlayingPlaylist,
                        MainScreen.sIndex);
                    initView();
                 }
            } else {
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index,0);
                initView();
            }
        } else if(MainScreen.sShuffleMode==1) {
            if(MainScreen.sPrevious.size()<2) {
                int max = MainScreen.sNowPlayingPlaylist.length;
                Random r = new Random();
                int i1 = r.nextInt(max - 0) + 0;
                new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,i1);
                initView();
            } else {
                int index = MainScreen.sPrevious.get(MainScreen.sPrevious.size()-2);
                MainScreen.sPrevious.remove(MainScreen.sPrevious.size()-2);
                new ProgressBar(
                        sContextPlay,MainScreen.sTitle,
                        MainScreen.sImage,MainScreen.sArtist,
                        MainScreen.sNowPlayingPlaylist,index,0);
                initView();
            }
        }
    }
    public void clickRepeat2 (View view) {
        if(MainScreen.sRepeatMode ==0) {
            MainScreen.sRepeatMode =1;
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
            MainScreen.sBtnRepeat.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_repeat_on));
        } else if(MainScreen.sRepeatMode ==1) {
            MainScreen.sRepeatMode =2;
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
            MainScreen.sBtnRepeat.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_repeat_one));
        }
        else if(MainScreen.sRepeatMode ==2){
            MainScreen.sRepeatMode = 0;
            mBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
            MainScreen.sBtnRepeat.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_repeat_off));
        }
        MainScreen.editor = getSharedPreferences(
                MainScreen.MY_PREFS_NAME, MODE_PRIVATE).edit();
        MainScreen.editor.putInt("Repeat Mode", MainScreen.sRepeatMode);
        MainScreen.editor.putInt("Shuffle Mode", MainScreen.sShuffleMode);
        MainScreen.editor.commit();
    }
    public void clickShuffle2 (View view) {
        if(MainScreen.sShuffleMode ==0) {
            MainScreen.sShuffleMode =1;
            mBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
            MainScreen.sBtnShuffle.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_shuffle_on));
        } else if (MainScreen.sShuffleMode ==1){
            MainScreen.sShuffleMode = 0;
            mBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
            MainScreen.sBtnShuffle.setImageDrawable(
                    getResources().getDrawable(R.drawable.ic_shuffle_off));
        }
        MainScreen.editor = getSharedPreferences(
                MainScreen.MY_PREFS_NAME, MODE_PRIVATE).edit();
        MainScreen.editor.putInt("Shuffle Mode", MainScreen.sShuffleMode);
        MainScreen.editor.putInt("Repeat Mode", MainScreen.sRepeatMode);
        MainScreen.editor.commit();
    }
    public SearchObject[] getItemsFromDb(String searchTerm){
        // add items on the array dynamically
        mDB = new SongDBHandler(getApplicationContext());
        List<SearchObject> products = null;
        products = mDB.readSong(searchTerm);

        int rowCount = products.size();
//        if(rowCount>10) {
//            return null;
//        }
        SearchObject[] item = new SearchObject[rowCount];
        int x = 0;
        for (SearchObject record : products) {
            item[x] = record;
            x++;
        }
        return item;
    }

    public void autoCompleteInit() {
        try{
            // add the listener so it will tries to suggest while the user types
            sAutoCompletePlayScreen.addTextChangedListener(new CustomAutoCompleteListener4(this));
            // ObjectItemData has no value at first
            SearchObject[] ObjectItemData = new SearchObject[0];
//        sToolbarTitlePlayScreen.setVisibility(View.VISIBLE);

            // set the custom ArrayAdapter
            sAutoAdapPlayScreen = new AutocompleteCustomArrayAdapter(this, R.layout.item_search,
                    ObjectItemData, ObjectItemData, ObjectItemData, ObjectItemData);
            sAutoCompletePlayScreen.setAdapter(sAutoAdapPlayScreen);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setupSearch();
    }
    public void clickSearch() {
        autoCompleteInit();
        sAutoCompletePlayScreen.setText("");
        sToolbarTitlePlayScreen.setVisibility(View.GONE);
        sSearch.setVisibility(View.GONE);

        sClose.setVisibility(View.VISIBLE);
        sBack.setVisibility(View.GONE);
        sBack2.setVisibility(View.VISIBLE);
        sAutoCompletePlayScreen.setVisibility(View.VISIBLE);
        sBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCloseSearch();
            }
        });
        sClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickClearSearch();
            }
        });
    }
    public static void clickCloseSearch() {
        sSearch.setVisibility(View.VISIBLE);
        sClose.setVisibility(View.GONE);
        sBack.setVisibility(View.GONE);
        sBack2.setVisibility(View.VISIBLE);
        sBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoMainScreen();
            }
        });
        sAutoCompletePlayScreen.setVisibility(View.GONE);
    }
    public void clickClearSearch() {
        sAutoCompletePlayScreen.setText("");
    }
}
