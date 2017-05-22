package com.example.justakiss.stoberriibeautymusic.handler;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.object.Song;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by justakiss on 11/10/2016.
 */
public class MyServiceManager extends Service {
    private SongDBHandler mDB;
    private int count = 0;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved (Intent rootIntent) {
        getMusic(getApplicationContext());
        Toast.makeText(getApplicationContext(),"DONE RELOAD DATABASE!", Toast.LENGTH_SHORT).show();
        stopSelf();
    }
    public void getMusic(Context context){
        mDB = new SongDBHandler(getApplicationContext());
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
//                    Toast.makeText(getContext(),path,Toast.LENGTH_LONG).show();
                    String title = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.TITLE));
                    String album = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    String artist = audioCursor.getString(audioCursor
                            .getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    int album_id = audioCursor.getInt(audioCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    int track_id = audioCursor.getInt(audioCursor.getColumnIndex(MediaStore.Audio.Media.TRACK));
                    Long duration = audioCursor.getLong(audioCursor.getColumnIndex(MediaStore.Audio.Media.DURATION));

                    Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                            new String[] {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART},
                            MediaStore.Audio.Albums._ID+ "=?",
                            new String[] {String.valueOf(album_id)},
                            null);
                    if (cursor.moveToFirst()) {
                        image1 = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                        // do whatever you need to do
                        if(image1!=null){
                            images = saveImage(image1);
                            Log.e("images:","-----------------"+image1);
                        }
                    }
                    try {
//                        if(!mDB.checkExist(path)){
                        mDB.addSong(new Song(path,title,album,artist,album_id,track_id,duration,images));
//                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while(audioCursor.moveToNext());
            }
        }
        mDB.close();
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
}
