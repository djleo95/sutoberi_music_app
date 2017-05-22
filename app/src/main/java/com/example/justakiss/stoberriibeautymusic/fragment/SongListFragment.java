package com.example.justakiss.stoberriibeautymusic.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.adapter.SongsListAdapter;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.object.Song;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by justakiss on 15/09/2016.
 */
public class SongListFragment extends Fragment{
    private SongDBHandler mDB;
    private int count=0;
    private static int sFirstOpen = 0;
    public static SongsListAdapter sSongListAdap;
    public SongListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_songlist, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initView(view);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.e("PermissionNotGrantedYet","!!!!!!");
        switch (requestCode) {
            case 111: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        getMusic(getContext());
                    Log.e("PermissionGrantedx","!!!!!!");
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {
                    Log.e("PermissionGrantedxx","!!!!!!");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public void initView(View view) {
        mDB = new SongDBHandler(getContext());
        final RecyclerView rvSongs = (RecyclerView) view.findViewById(R.id.rv_SongsList);
//        if(sFirstOpen == 0) {
//            requestPermission();
//            getMusic(getContext());
//            sFirstOpen =1;
//        }
        List<Song> song = mDB.getAllSongs();
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
        if(sFirstOpen == 0) {
            MainScreen.sTitle = name;
            MainScreen.sArtist = artist;
            MainScreen.sImage = image;
            MainScreen.sNowPlayingPlaylist = path;
            sFirstOpen = 1;
            MainScreen.sIndex = 0;
            MainScreen.setupNowPlaying();
            Log.e("set", "playlist--------");
        }
        sSongListAdap = new SongsListAdapter(getContext(),
                name, artist,image,path);
        rvSongs.setAdapter(sSongListAdap);
        // Set layout manager to position the items
        rvSongs.setLayoutManager(new GridLayoutManager(MainScreen.sContext,MainScreen.sMode));
        rvSongs.setHasFixedSize(true);
        rvSongs.setItemAnimator(new SlideInUpAnimator());
        sSongListAdap.setOnItemClickListener(new SongsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                new ProgressBar(getContext(),name,
                        image,artist,
                        path,position);
                sSongListAdap.notifyDataSetChanged();
            }
        });
    }

    public void getMusic(Context context){

        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        String[] projection = {
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TRACK,
                MediaStore.Audio.Media.DURATION
        };

        Cursor audioCursor = context.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null);

        if (audioCursor != null) {
            if (audioCursor.moveToFirst()) {
                do {
                    String image1 = null, images = null;
                    String path = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    if(mDB.checkExist(path)){
                        Log.e("CHECKED!!!!!!!!!!!",""+path);
                        continue;
                    }
                    String title = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String album = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artist = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int album_id = audioCursor.getInt(
                            audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    int track_id = audioCursor.getInt(
                            audioCursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
                    Long duration = audioCursor.getLong(
                            audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Cursor cursor = context.getContentResolver().query(
                            MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID,
                                    MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(album_id)},
                            null);
                    if (cursor.moveToFirst()) {
                        image1 = cursor.getString(
                                cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        // do whatever you need to do
                        if(image1!=null){
                            images = saveImage(image1);
                            Log.e("images:","-----------------"+image1);
                        }
                    }
                    try {
//                        if(!mDB.checkExist(path)){
                        mDB.addSong(new Song(path,title,album,artist,
                                album_id,track_id,duration,images));
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while(audioCursor.moveToNext());
            }
        }
        //--------------------------------------------------------
//        Cursor audioCursor2 = context.getContentResolver().query(
//                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,
//                projection,
//                selection,
//                null,
//                null);
//
//        if (audioCursor2 != null) {
//            if (audioCursor2.moveToFirst()) {
//                String image1 = null, images = null;
//                do {
//                    String path = audioCursor2.getString(audioCursor2
//                            .getColumnIndex(MediaStore.Audio.Media.DATA));
////                    Toast.makeText(getContext(),path,Toast.LENGTH_LONG).show();
//                    String title = audioCursor2.getString(audioCursor2
//                            .getColumnIndex(MediaStore.Audio.Media.TITLE));
//                    String album = audioCursor2.getString(audioCursor2
//                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
//                    String artist = audioCursor2.getString(audioCursor2
//                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
//                    int album_id = audioCursor2.getInt(audioCursor2.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
//                    int track_id = audioCursor2.getInt(audioCursor2.getColumnIndex(MediaStore.Audio.Media.TRACK));
//                    Long duration = audioCursor2.getLong(audioCursor2.getColumnIndex(MediaStore.Audio.Media.DURATION));
//
//                    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
//                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
//                            MediaStore.Audio.Albums._ID+ "=?",
//                            new String[] {String.valueOf(album_id)},
//                            null);
//                    if (cursor.moveToFirst()) {
//                        image1 = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
//                        // do whatever you need to do
//                        images = "file:/"+image1;
//
//                    }
//                    try {
//                        mDB.addSong(new Song(path,title,album,artist,album_id,track_id,duration,image1));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } while(audioCursor2.moveToNext());
//            }
//        }
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 100,100);
        return new BitmapDrawable(bitmap1);
    }
    public String saveImage(String path) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp;
        String fname = imageFileName +"_"+ count + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(path), 400,400);
            FileOutputStream out = new FileOutputStream(file);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        count++;
        return myDir.getAbsolutePath() +"/" + fname;
    }
    public void requestPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        111);
                Log.e("REQUESTED!!","!!");

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }
}