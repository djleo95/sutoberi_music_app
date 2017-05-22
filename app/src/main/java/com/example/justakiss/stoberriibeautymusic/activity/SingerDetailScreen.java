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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.adapter.AlbumDetailAdap;
import com.example.justakiss.stoberriibeautymusic.adapter.AutocompleteCustomArrayAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.SingerDetailAdap;
import com.example.justakiss.stoberriibeautymusic.custom.MusicGetter;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.handler.CustomAutoCompleteListener2;
import com.example.justakiss.stoberriibeautymusic.handler.CustomAutoCompleteListener3;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;
import com.example.justakiss.stoberriibeautymusic.handler.OnSwipeTouchListener;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;
import com.example.justakiss.stoberriibeautymusic.object.Song;

import java.io.IOException;
import java.util.List;
import java.util.Random;


/**
 * Created by justakiss on 19/10/2016.
 */
public class SingerDetailScreen extends AppCompatActivity {

    public static TextView sNowPlayingTitle, sNowPlayingArtist,sToolbarTitleSinger, sTimeLeft;
    public static ImageView sNowPlayingImg;
    public static ImageButton sRepeat, sShuffle, sPlay;
    public static SeekBar sSeekBar;
    public static Handler sSeekBarHandler = new Handler();
    public static Context sContext;
    public static AutoCompleteTextView sAutoCompleteSinger;
    public static AutocompleteCustomArrayAdapter sAutoAdapSinger;
    public static ImageButton sBtnBack, sBtnSearch, sBtnClose, sBtnBack2;
    public static AlbumDetailAdap sSingerDetailAdap;
    public static SingerDetailAdap sSingerDetailAdap2;
    public static View sNowPlayingActionZone;
    private String singername;
    private TextView mSingerName;
    private View mSingerBackground;
    private Toolbar toolbar, toolbar2;
    private RecyclerView mRvSingerDetail, mRvSingerDetail2;
    private SongDBHandler mDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singerdetail_screen);

        setupToolbar(1);
        MainScreen.sIsInSingerDetail = 1;
        sContext = getApplicationContext();
        Intent i = getIntent();
        singername = i.getExtras().getString("singer");
        initView();
    }
    @Override
    public void onBackPressed() {
        finish();
        gotoMain();
    }
    public void initView() {
        sNowPlayingTitle = (TextView) findViewById(R.id.tv_name_nowplaying3);
        sNowPlayingArtist = (TextView) findViewById(R.id.tv_artist_nowplaying3);
        sNowPlayingImg = (ImageView) findViewById(R.id.iv_nowplaying3);
        sToolbarTitleSinger = (TextView) findViewById(R.id.toolbar2_label);
        sRepeat = (ImageButton) findViewById(R.id.btn_repeat3);
        sShuffle = (ImageButton) findViewById(R.id.btn_shuffle3);
        sPlay = (ImageButton) findViewById(R.id.btn_play3);
        sSeekBar = (SeekBar) findViewById(R.id.songProgressBar3);
        mSingerName = (TextView) findViewById(R.id.tv_singer_name);
        mSingerBackground = findViewById(R.id.rl_singerdetail_image);
        sTimeLeft = (TextView) findViewById(R.id.tv_timeleft3);
        sNowPlayingActionZone = findViewById(R.id.ll_now_playing_action_zone3);
        mSingerName.setText(singername);
        sNowPlayingTitle.setText(MainScreen.sTitle[MainScreen.sIndex]);
        sNowPlayingArtist.setText(MainScreen.sArtist[MainScreen.sIndex]);
        sSeekBar.setProgress(0);
        sSeekBar.setMax(300);
        if(MainScreen.sImage[MainScreen.sIndex]!=null){
            sNowPlayingImg.setImageDrawable(
                    getImage(MainScreen.sImage[MainScreen.sIndex],100,100));
        } else {
            sNowPlayingImg.setImageDrawable(
                    sContext.getResources().getDrawable(R.drawable.ic_music2));
        }
        setupRV1();
        setupRV2();
        setupButton();
        setupSwipe();
        new ProgressBar(sContext);
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
    public void setupRV1() {
        mDB = new SongDBHandler(sContext);
        mRvSingerDetail = (RecyclerView) findViewById(R.id.rv_SingerDetail2);
        List<Song> song = mDB.getAllSongOfSinger(singername);
        final String[] name = new String[song.size()];
        final String[] artist = new String[song.size()];
        final String[] image = new String[song.size()];
        final String[] path = new String[song.size()];
        String a, b, c, d;
        int index = song.size()-1;
        for (Song n : song) {
            a = n.getTitle();
            b = n.getArtist();
            c = n.getImage();
            d = n.getPath();
            name[index] = a;
            artist[index] = b;
            image[index] = c;
            path[index] = d;
            index--;
        }
        mDB.close();
        if(image[0]!=null){
            mSingerBackground.setBackground(getImage(image[0],320,420));
        } else {
            mSingerBackground.setBackground(
                    sContext.getResources().getDrawable(R.drawable.ic_music2));
        }
        sSingerDetailAdap = new AlbumDetailAdap(sContext,
                name, artist,image,path);
        mRvSingerDetail.setAdapter(sSingerDetailAdap);
        // Set layout manager to position the items
        mRvSingerDetail.setLayoutManager(new GridLayoutManager(MainScreen.sContext,1));
        mRvSingerDetail.setHasFixedSize(true);

        sSingerDetailAdap.setOnItemClickListener(new AlbumDetailAdap.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new ProgressBar(sContext,name,
                        image,artist,
                        path,position);
                sSingerDetailAdap.notifyDataSetChanged();
            }
        });

    }
    public void setupRV2() {
        mDB = new SongDBHandler(sContext);
        mRvSingerDetail2 = (RecyclerView) findViewById(R.id.rv_SingerDetail);
        List<Song> song = mDB.getEverythingOfSinger(singername);
        final String[] album = new String[song.size()];
        final String[] image = new String[song.size()];
        final int[] number = new int[song.size()];
        String a, c;
        int b;
        int index = song.size()-1;
        for (Song n : song) {
            a = n.getAlbum();
            b = n.getTrackId();
            c = n.getImage();
            album[index] = a;
            number[index] = b;
            image[index] = c;
            index--;
        }
        mDB.close();
        sSingerDetailAdap2 = new SingerDetailAdap(sContext,
                album, number,image);
        mRvSingerDetail2.setAdapter(sSingerDetailAdap2);
        // Set layout manager to position the items
        mRvSingerDetail2.setLayoutManager(new GridLayoutManager(MainScreen.sContext,4));
        mRvSingerDetail2.setHasFixedSize(true);
        sSingerDetailAdap2.setOnItemClickListener(new SingerDetailAdap.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Context c = getApplicationContext();
                Intent intent1 = new Intent(c, AlbumDetailScreen.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent1.putExtra("album",album[position]);
                c.startActivity(intent1);
            }
        });

    }
    public Drawable getImage(String path, int size1, int size2) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), size2, size1);
        return new BitmapDrawable(bitmap1);
    }
    public void gotoMain() {
        MainScreen.sIsInSingerDetail = 0;
//        Context c = getApplicationContext();
//        Intent intent1 = new Intent(c, MainScreen.class);
//        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        c.startActivity(intent1);
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
    public void setupSwipe() {
        mSingerBackground.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeTop() {
                hideBackground();
            }
            public void onSwipeRight() {
            }
            public void onSwipeLeft() {
            }
            public void onSwipeBottom() {
//                showBackground();
            }
        });
    }
    public void hideBackground() {
        setupToolbar(2);
        mSingerBackground.animate()
                .translationY(0)
                .alpha(0.0f)
                .setDuration(300);
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        mSingerBackground.setVisibility(View.GONE);
//                    }
//                });
        mSingerBackground.setVisibility(View.GONE);
    }

    public void showBackground() {
        setupToolbar(1);
        mSingerBackground.animate().alpha((1.0f));
//        mSingerBackground.animate()
////                .translationY(0)
//                .alpha(1.0f)
//                .setDuration(100)
//                .setListener(new AnimatorListenerAdapter() {
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        super.onAnimationEnd(animation);
//                        mSingerBackground.setVisibility(View.VISIBLE);
//                    }
//                });
        mSingerBackground.setVisibility(View.VISIBLE);
    }
    public void setupToolbar(int mode) {
        toolbar = (Toolbar) findViewById(R.id.toolbar_detail);
//        setSupportActionBar(toolbar);
        toolbar2 = (Toolbar) findViewById(R.id.toolbar_album2);
        if(mode == 1) {
            toolbar.setVisibility(View.VISIBLE);
            toolbar2.setVisibility(View.GONE);
            sToolbarTitleSinger = (TextView) findViewById(R.id.toolbar2_label);
            sBtnBack = (ImageButton) findViewById(R.id.btn_back2);
            sBtnSearch = (ImageButton) findViewById(R.id.btn_search2);
            sBtnClose = (ImageButton) findViewById(R.id.btn_close2);
            sBtnBack2 = (ImageButton) findViewById(R.id.btn_back2_2);
            sAutoCompleteSinger = (AutoCompleteTextView) findViewById(R.id.myautocomplete2);
//            mToolbarShow.setVisibility(View.VISIBLE);
//            mToolbarHide.setVisibility(View.GONE);
            sBtnBack2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    gotoMain();
                }
            });
            setSupportActionBar(toolbar);
        } else if (mode == 2) {
            toolbar2.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
            sToolbarTitleSinger = (TextView) findViewById(R.id.toolbar3_label);
            sBtnBack = (ImageButton) findViewById(R.id.btn_back3);
            sBtnSearch = (ImageButton) findViewById(R.id.btn_search3);
            sBtnClose = (ImageButton) findViewById(R.id.btn_close3);
            sBtnBack2 = (ImageButton) findViewById(R.id.btn_back3_2);
            sAutoCompleteSinger = (AutoCompleteTextView) findViewById(R.id.myautocomplete3);
//            toolbar2.setBackgroundColor(getAverageColor());
//            mToolbarShow.setVisibility(View.VISIBLE);
//            mToolbarHide.setVisibility(View.GONE);
            setSupportActionBar(toolbar2);
            toolbar2.setOnTouchListener(new OnSwipeTouchListener(this) {
                public void onSwipeTop() {
//                    hideBackground();
                }
                public void onSwipeRight() {
                }
                public void onSwipeLeft() {
                }
                public void onSwipeBottom() {
                    showBackground();
                }
            });
        }
        sBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSearch();
            }
        });
        sBtnBack2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                gotoMain();
            }
        });
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public SearchObject[] getItemsFromDb(String searchTerm){
        // add items on the array dynamically
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
            sAutoCompleteSinger.addTextChangedListener(new CustomAutoCompleteListener3(this));
            // ObjectItemData has no value at first
            SearchObject[] ObjectItemData = new SearchObject[0];

            // set the custom ArrayAdapter
            sAutoAdapSinger = new AutocompleteCustomArrayAdapter(this, R.layout.item_search,
                    ObjectItemData, ObjectItemData, ObjectItemData, ObjectItemData);
            sAutoCompleteSinger.setAdapter(sAutoAdapSinger);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        setupSearch();
    }
    public void clickSearch() {
        autoCompleteInit();
        sAutoCompleteSinger.setText("");
        sToolbarTitleSinger.setVisibility(View.GONE);
        sBtnSearch.setVisibility(View.GONE);

        sBtnClose.setVisibility(View.VISIBLE);
        sBtnBack2.setVisibility(View.GONE);
        sBtnBack.setVisibility(View.VISIBLE);
        sAutoCompleteSinger.setVisibility(View.VISIBLE);
        sBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickCloseSearch();
            }
        });
        sBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickClearSearch();
            }
        });
    }
    public static void clickCloseSearch() {
        sToolbarTitleSinger.setVisibility(View.VISIBLE);
        sBtnSearch.setVisibility(View.VISIBLE);
        sBtnClose.setVisibility(View.GONE);
        sBtnBack.setVisibility(View.GONE);
        sBtnBack2.setVisibility(View.VISIBLE);
        sAutoCompleteSinger.setVisibility(View.GONE);
    }
    public void clickClearSearch() {
        sAutoCompleteSinger.setText("");
    }

    public void playNext() {
        MusicService.sPlayer.reset();
        MainScreen.sPrevIndex =1;
        if(MainScreen.sShuffleMode ==0) {
            if(MainScreen.sIndex < MainScreen.sTitle.length - 1) {
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
