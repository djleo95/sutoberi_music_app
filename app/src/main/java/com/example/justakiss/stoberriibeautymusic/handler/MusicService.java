package com.example.justakiss.stoberriibeautymusic.handler;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.custom.CustomNotification;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.object.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by justakiss on 02/11/2016.
 */
public class MusicService extends Service {
    private static final String TAG = null;
    public static MediaPlayer sPlayer = new MediaPlayer();
    public static int mode = 0;
    private String path,name,artist,image;

    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

        sPlayer.setLooping(true); // Set looping
        Log.e("get name","again");

//        sPlayer.setVolume(100,100);

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        name = MainScreen.sTitle[MainScreen.sIndex];
        artist = MainScreen.sArtist[MainScreen.sIndex];
        image = MainScreen.sImage[MainScreen.sIndex];
//        stopService()
        path = intent.getExtras().getString("path");
        if(!path.matches("stopmusic")&&!path.matches("stopmusic2")) {
            mode = 0;
            if(MainScreen.sPause!=0){
                sPlayer.seekTo(MainScreen.sPause);
                MainScreen.sPause = 0;
                sPlayer.start();
                startForeground(1,simpleNoti(getApplicationContext()));
            } else {
                try {
                    sPlayer.reset();
                    sPlayer.setDataSource(path);
                    sPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sPlayer.start();
                startForeground(1,simpleNoti(getApplicationContext()));
            }

        } else if(path.matches("stopmusic")) {
            mode = 1;
            stopService(intent);
            Intent svc = new Intent(getApplicationContext(), MusicService.class);
            svc.putExtra("path", "stopmusic2");
            startService(svc);
        } else if(path.matches("stopmusic2")) {
            mode = 1;
            simpleNoti(getApplicationContext());
        }
//        new CustomNotification(getApplicationContext());
        return START_NOT_STICKY;
    }

    public void onStart(Intent intent, int startId) {
        // TO DO
    }
    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    public void onStop() {
        Log.e(null,"stoped------------");
    }
    public void onPause() {
        Log.e(null,"paused------------");
    }
    @Override
    public void onDestroy() {
        Log.e(null,"destroyed------------");
        if(mode != 1){
            simpleNoti(getApplicationContext());
        }
    }

    @Override
    public void onLowMemory() {

    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.e("_______________","removed------------");
        if(mode == 1) {
            clearNoti(getApplicationContext());
            Log.e("_______________","removed------------2");
            stopSelf();
            stopForeground(true);
        }
    }
    public Notification simpleNoti(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.test_noti);
        RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(),
                R.layout.noti_lockscreen);

        if(sPlayer.isPlaying()){
            remoteViewsSmall.setImageViewResource(R.id.noti_play2,R.drawable.ic_pause);
        } else {
            remoteViewsSmall.setImageViewResource(R.id.noti_play2,R.drawable.ic_play);
        }
        remoteViewsSmall.setTextViewText(R.id.noti_songname2,name);
        remoteViewsSmall.setTextColor(R.id.noti_songname2, Color.parseColor("#1d1d1d"));
        remoteViewsSmall.setTextColor(R.id.noti_artist2,Color.parseColor("#1d1d1d"));
        remoteViewsSmall.setTextViewText(R.id.noti_artist2,artist);
        if(image!=null) {
            remoteViewsSmall.setImageViewBitmap(R.id.noti_image2, ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(image), 150, 150));
        } else {
            remoteViewsSmall.setImageViewResource(R.id.noti_image2,R.drawable.ic_music2);
        }

        Intent pauseIntent1 = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent nextIntent1 = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent backIntent1 = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent gotoPlay1 = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        pauseIntent1.putExtra("noti", 0);
        nextIntent1.putExtra("noti", 1);
        backIntent1.putExtra("noti", 2);
        gotoPlay1.putExtra("noti",3);


        PendingIntent pause1 = PendingIntent.getService(context.getApplicationContext(),
                0, pauseIntent1, 0);
        PendingIntent next1 = PendingIntent.getService(context.getApplicationContext(),
                1, nextIntent1, 0);
        PendingIntent back1 = PendingIntent.getService(context.getApplicationContext(),
                2, backIntent1, 0);
        PendingIntent goPlay1 = PendingIntent.getService(context.getApplicationContext(),
                3,gotoPlay1,0);


        remoteViewsSmall.setOnClickPendingIntent(R.id.noti_play2, pause1);
        remoteViewsSmall.setOnClickPendingIntent(R.id.noti_next2, next1);
        remoteViewsSmall.setOnClickPendingIntent(R.id.noti_back2, back1);
        remoteViewsSmall.setOnClickPendingIntent(R.id.noti_image2,goPlay1);


        //--
        if(sPlayer.isPlaying()){
            remoteViews.setImageViewResource(R.id.noti_play,R.drawable.ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.noti_play,R.drawable.ic_play);
            Log.e("test","playicon");
        }
        remoteViews.setTextViewText(R.id.noti_songname1,name);
        remoteViews.setTextColor(R.id.noti_songname1,Color.parseColor("#1d1d1d"));
        remoteViews.setTextColor(R.id.noti_artist1,Color.parseColor("#1d1d1d"));
        remoteViews.setTextViewText(R.id.noti_artist1,artist);
        if(image!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_image1, ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(image), 200, 200));
        } else {
            remoteViews.setImageViewResource(R.id.noti_image1,R.drawable.ic_music2);
        }

        Intent pauseIntent = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent nextIntent = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent backIntent = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        Intent gotoPlay = new Intent(context.getApplicationContext(), NotiClickHandler.class);
        pauseIntent.putExtra("noti", 0);
        nextIntent.putExtra("noti", 1);
        backIntent.putExtra("noti", 2);
        gotoPlay.putExtra("noti",3);


        PendingIntent pause = PendingIntent.getService(context.getApplicationContext(),
                0, pauseIntent, 0);
        PendingIntent next = PendingIntent.getService(context.getApplicationContext(),
                1, nextIntent, 0);
        PendingIntent back = PendingIntent.getService(context.getApplicationContext(),
                2, backIntent, 0);
        PendingIntent goPlay = PendingIntent.getService(context.getApplicationContext(),
                3,gotoPlay,0);


        remoteViews.setOnClickPendingIntent(R.id.noti_play, pause);
        remoteViews.setOnClickPendingIntent(R.id.noti_next, next);
        remoteViews.setOnClickPendingIntent(R.id.noti_back, back);
        remoteViews.setOnClickPendingIntent(R.id.noti_image1,goPlay);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_play)
                        .setContentTitle("My notification")
//                        .setColor(Color.parseColor("#6bc7e9"))
                        .setContent(remoteViewsSmall)
                        .setVisibility(1)
                        .setOngoing(true)
                        .setAutoCancel(true)
                        .setCustomBigContentView(remoteViews);
        if(sPlayer.isPlaying()){
            mBuilder.setOngoing(true);
            Log.e("test","ongoingtrue");
        } else {
            mBuilder.setOngoing(false);
            Log.e("test","ongoingfalse");
        }
//        mBuilder.setContentIntent(remoteViews);
//        mBuilder.setColor(Color.parseColor("#ff6bc7e9"));
        // NotificationCompat.Builder mBuilder;
        //        ...
        // Sets an ID for the notification

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());
        Notification noti = mBuilder.build();
        return noti;
    }
    public void clearNoti(Context context) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancelAll();
    }
}