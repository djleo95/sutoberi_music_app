package com.example.justakiss.stoberriibeautymusic.activity;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.provider.SyncStateContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.justakiss.stoberriibeautymusic.adapter.AutocompleteCustomArrayAdapter;
import com.example.justakiss.stoberriibeautymusic.custom.CustomAutoCompleteView;
import com.example.justakiss.stoberriibeautymusic.custom.CustomNotification;
import com.example.justakiss.stoberriibeautymusic.custom.MusicGetter;
import com.example.justakiss.stoberriibeautymusic.fragment.AlbumListFragment;
import com.example.justakiss.stoberriibeautymusic.handler.CustomAutoCompleteListener;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;
import com.example.justakiss.stoberriibeautymusic.handler.OnSwipeTouchListener;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.fragment.SingerListFragment;
import com.example.justakiss.stoberriibeautymusic.fragment.SongListFragment;
import com.example.justakiss.stoberriibeautymusic.custom.Utilities;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;
import com.example.justakiss.stoberriibeautymusic.object.Constants;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainScreen extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int mFirstRun = 0;
    private SongDBHandler mDB;
    private NotificationManager mNotificationManager;
    private List<Fragment> mFragmentList2;
    private static Menu menu;
    public static Toolbar sToolbar;
    public static TextView sToolbarTitle;
    public static ImageButton sBtnSearch, sBtnClose, sBtnBack;
    public static CustomAutoCompleteView sAutoComplete;
    public static AutocompleteCustomArrayAdapter sMyAdapter;
    public static View sNowPlayingAction;
//    public static String mLastPlayed = null;
    public static String[] sNowPlayingPlaylist = null, sTitle, sArtist, sImage;
    public static ImageView sImgNowPlaying;
    public static TextView sTitleNowPlaying, sArtistNowPlaying, sTimeLeft;
    public static ImageButton sBtnPlay, sBtnRepeat, sBtnShuffle;
    public static int sIndex = 0,
            sPause = 0, sMode = 1, sShuffleMode = 1,
            sRepeatMode = 1, sPrevIndex = 1, sIsInPlayScreen = 0, sIsInAlbumDetail = 0,
            sFirstOpen = 0, sTab = 0, sSearchSong, sSearchAlbum, sSearchArtist,
            sFirstLoad = 0, sIsInSingerDetail = 0, sIsInArrangeScreen = 0;
    public static int sLastScreen;
    public static ArrayList<Integer> sPrevious = new ArrayList<>();
//    public static ArrayList<String> sNowPlaylistArrange = new ArrayList<>();
    public static MediaPlayer sMediaPlayer = new MediaPlayer();
    public static SeekBar sSeekBarPlayer;
    public static android.os.Handler sSeekBarHandler = new android.os.Handler();
    public static Utilities sUtils = new Utilities();
    public static Context sContext;
//    public static boolean sFirstSave = true;
    public static SharedPreferences.Editor editor;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final int NOTIFICATION_ID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        sToolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(sToolbar);
//        getSupportActionBar().setDisplayShowTitleEnabled(false);
//        requestPermission();
        if(sFirstLoad == 0) {
            Context c = getApplicationContext();
            Intent intent1 = new Intent(c, LoadingScreen.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            c.startActivity(intent1);
            sFirstLoad = 1;
        } else {
            mDB = new SongDBHandler(this);
            sImgNowPlaying = (ImageView) findViewById(R.id.iv_nowplaying);
            sTitleNowPlaying = (TextView) findViewById(R.id.tv_name_nowplaying);
            sArtistNowPlaying = (TextView) findViewById(R.id.tv_artist_nowplaying);
            sTimeLeft = (TextView) findViewById(R.id.tv_timeleft);
            sBtnPlay = (ImageButton) findViewById(R.id.btn_play);
            sBtnRepeat = (ImageButton) findViewById(R.id.btn_repeat);
            sBtnShuffle = (ImageButton) findViewById(R.id.btn_shuffle);
            sImgNowPlaying.setImageDrawable(getResources().getDrawable(R.drawable.ic_music2));
            sSeekBarPlayer = (SeekBar) findViewById(R.id.songProgressBar);
            sNowPlayingAction = findViewById(R.id.ll_now_playing_action_zone);
            sToolbarTitle = (TextView) findViewById(R.id.toolbar_label);
            sBtnSearch = (ImageButton) findViewById(R.id.btn_search);
            sBtnClose = (ImageButton) findViewById(R.id.btn_close);
            sBtnBack = (ImageButton) findViewById(R.id.btn_back);
            sContext = getApplicationContext();
            sTitleNowPlaying.setText("Unknown");
            sArtistNowPlaying.setText("Unknown");
            sSeekBarPlayer.setVisibility(View.INVISIBLE);
            sIsInPlayScreen = 0;
            sIsInAlbumDetail = 0;
            sIsInSingerDetail = 0;
            sIsInArrangeScreen = 0;
            if (sFirstOpen != 0) {
                new ProgressBar(sContext);
            } else {
                sFirstOpen = 1;
            }
            getPref();
            setUpButton();

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            setupViewPager(viewPager);
//        requestPermission();
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

            sNowPlayingAction.setOnTouchListener(new OnSwipeTouchListener(this) {
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
            sImgNowPlaying.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoPlayScreen();
                }
            });
//        autoCompleteInit();
//        startService();
//        setupNowPlaying();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_actionbar_main, menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NotificationManager mNotifyMgr =
                                (NotificationManager) sContext.
                                        getSystemService(sContext.NOTIFICATION_SERVICE);
                        if(MusicService.mode==1){
                            mNotifyMgr.cancelAll();
                            finish();
                            System.exit(0);
                        } else {
                            finish();
                        }

                    }
                }).setNegativeButton("No", null).show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("PermissionNotGranted","!!!!!!");
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    new MusicGetter(getApplicationContext());
                    setupViewPager(viewPager);
                    Log.e("PermissionGranted","!!!!!!");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e("PermissionGranted2","!!!!!!");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;

            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_listview:
                sMode =1;
                sTab = viewPager.getCurrentItem();
                setupViewPager(viewPager);
                viewPager.setCurrentItem(sTab);
                return true;
            case R.id.action_gridview:
                sMode =2;
                sTab = viewPager.getCurrentItem();
                setupViewPager(viewPager);
                viewPager.setCurrentItem(sTab);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static void showOverflowMenu(boolean showMenu){
        if(menu == null)
            return;
        menu.setGroupVisible(R.id.menu_group, showMenu);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SongListFragment(), "SONGS");
        adapter.addFragment(new AlbumListFragment(), "ALBUMS");
        adapter.addFragment(new SingerListFragment(), "ARTISTS");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            mFragmentList2 = mFragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    public void playSong(View view) throws IOException {
        if (!MusicService.sPlayer.isPlaying()) {
//            sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
            if (!(sNowPlayingPlaylist == null)) {
                if(sPause !=0) {
                    new ProgressBar(sNowPlayingPlaylist[sIndex],this);
                    Log.e("bug","here-----");
                } else {
                    new ProgressBar(this, sTitle, sImage, sArtist,
                            sNowPlayingPlaylist, sIndex);
//                    new CustomNotification(getApplicationContext());
                }
            }
        } else if (MusicService.sPlayer.isPlaying()) {
            MusicService.sPlayer.pause();
            sPause = MusicService.sPlayer.getCurrentPosition();
            sBtnPlay.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
            new CustomNotification(getApplicationContext(),1);
        }
    }

    public void playNext() {
        MusicService.sPlayer.reset();
        sPrevIndex = 1;
        if(sShuffleMode ==0) {
            if(sIndex < sTitle.length -1 ) {
                new ProgressBar(this, sTitle, sImage,
                        sArtist, sNowPlayingPlaylist, ++sIndex);
            } else {
                sIndex = 0;
                new ProgressBar(this, sTitle, sImage,
                        sArtist, sNowPlayingPlaylist, sIndex);
            }
        } else if(sShuffleMode ==1) {
            int max = sNowPlayingPlaylist.length;
            Random r = new Random();
            int i1 = r.nextInt(max - 0) + 0;
            new ProgressBar(this, sTitle, sImage,
                    sArtist, sNowPlayingPlaylist, i1);
        }
    }

    public void playPrevious() {
        MusicService.sPlayer.reset();
        if(sShuffleMode==0){
            if(sPrevious.size()<2) {
                if(sIndex == 0) {
                    sIndex = sNowPlayingPlaylist.length-1 ;
                    new ProgressBar(
                            this, sTitle,
                            sImage, sArtist,
                            sNowPlayingPlaylist, sIndex);
                } else {
                    sIndex -= 1;
                    new ProgressBar(
                            this, sTitle, sImage,
                            sArtist, sNowPlayingPlaylist,
                            sIndex);
                }
            } else {
                int index = sPrevious.get(sPrevious.size()-2);
                sPrevious.remove(sPrevious.size()-2);
                new ProgressBar(
                        this,sTitle,
                        sImage,sArtist,
                        sNowPlayingPlaylist,index);
            }
        } else if(sShuffleMode==1) {
            if(sPrevious.size()<2) {
                int max = sNowPlayingPlaylist.length;
                Random r = new Random();
                int i1 = r.nextInt(max - 0) + 0;
                new ProgressBar(
                        this,sTitle,
                        sImage,sArtist,
                        sNowPlayingPlaylist,i1);
            } else {
                int index = sPrevious.get(sPrevious.size()-2);
                sPrevious.remove(sPrevious.size()-2);
                new ProgressBar(
                        this,sTitle,
                        sImage,sArtist,
                        sNowPlayingPlaylist,index);
            }
        }
    }
    public void setUpButton() {
        if(sRepeatMode ==0) {
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
        } else if(sRepeatMode ==1) {
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
        }
        else if(sRepeatMode ==2){
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
        }
        if(sShuffleMode ==0) {
            sBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
        } else if (sShuffleMode ==1){
            sBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
        }
    }
    public void clickRepeat (View view) {
        if(sRepeatMode ==0) {
            sRepeatMode =1;
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_on));
        } else if(sRepeatMode ==1) {
            sRepeatMode =2;
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
        }
        else if(sRepeatMode ==2){
            sRepeatMode =0;
            sBtnRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_off));
        }
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("Repeat Mode", sRepeatMode);
        editor.putInt("Shuffle Mode", sShuffleMode);
        editor.commit();
    }
    public void clickShuffle (View view) {
        if(sShuffleMode == 0) {
            sShuffleMode =1;
            sBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_on));
        } else if (sShuffleMode ==1){
            sShuffleMode =0;
            sBtnShuffle.setImageDrawable(getResources().getDrawable(R.drawable.ic_shuffle_off));
        }
        editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
        editor.putInt("Shuffle Mode", sShuffleMode);
        editor.putInt("Repeat Mode", sRepeatMode);
        editor.commit();
    }
    public void gotoPlayScreen() {
        Context c = getApplicationContext();
        Intent intent1 = new Intent(c, PlayScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        c.startActivity(intent1);
    }
    public void getPref() {
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        sShuffleMode = prefs.getInt("Shuffle Mode", 0);
        sRepeatMode = prefs.getInt("Repeat Mode", 0);
//        Log.e("Shuffle",":"+sShuffleMode+"---");
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
            // instantiate database handler
            // put sample data to database
            // autocompletetextview is in activity_main.xml
            sAutoComplete = (CustomAutoCompleteView) findViewById(R.id.myautocomplete);
//            sAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
//                    //
//                    clickCloseSearch();
//                }
//            });
            // add the listener so it will tries to suggest while the user types
            sAutoComplete.addTextChangedListener(new CustomAutoCompleteListener(this));
            // ObjectItemData has no value at first
            SearchObject[] ObjectItemData = new SearchObject[0];

            // set the custom ArrayAdapter
            sMyAdapter = new AutocompleteCustomArrayAdapter(this, R.layout.item_search,
                        ObjectItemData, ObjectItemData, ObjectItemData, ObjectItemData);
            sAutoComplete.setAdapter(sMyAdapter);

        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void clickSearch(View view) {
        autoCompleteInit();
        sAutoComplete.setText("");
        sToolbarTitle.setVisibility(View.GONE);
        sBtnSearch.setVisibility(View.GONE);
        sBtnClose.setVisibility(View.VISIBLE);
        sBtnBack.setVisibility(View.VISIBLE);
        sAutoComplete.setVisibility(View.VISIBLE);
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
        showOverflowMenu(false);
    }
    public static void clickCloseSearch() {
        sToolbarTitle.setVisibility(View.VISIBLE);
        sBtnSearch.setVisibility(View.VISIBLE);
        sBtnClose.setVisibility(View.GONE);
        sBtnBack.setVisibility(View.GONE);
        sAutoComplete.setVisibility(View.GONE);
        showOverflowMenu(true);
//        sToolbar.setBackgroundColor(Color.parseColor("#125688"));
    }
    public void clickClearSearch() {
        sAutoComplete.setText("");
    }

    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            new MusicGetter(getApplicationContext());
            setupViewPager(viewPager);
        }
    }
    public static void setupNowPlaying() {
        if(sTitleNowPlaying.getText().equals("Unknown")) {
            sIndex=0;
            sTitleNowPlaying.setText(sTitle[sIndex]);
        }
    }
}