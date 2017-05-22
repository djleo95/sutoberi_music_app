package com.example.justakiss.stoberriibeautymusic.custom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.ThumbnailUtils;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.RemoteViews;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;
import com.example.justakiss.stoberriibeautymusic.handler.MusicService;
import com.example.justakiss.stoberriibeautymusic.handler.NotiClickHandler;

/**
 * Created by justakiss on 02/11/2016.
 */
public class CustomNotification {
    public CustomNotification(Context context, int id) {
        simpleNoti(context, id);
    }
    public CustomNotification(Context context) {
        closeNoti(context);
    }
    public Notification simpleNoti(Context context, int id) {
//        RemoteViews remoteViews = new RemoteViews(getPackageName(),
//                R.layout.custom_notification);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.test_noti);
        RemoteViews remoteViewsSmall = new RemoteViews(context.getPackageName(),
                R.layout.noti_lockscreen);
        String name = MainScreen.sTitle[MainScreen.sIndex];
        String artist = MainScreen.sArtist[MainScreen.sIndex];
        String image = MainScreen.sImage[MainScreen.sIndex];
        //--
        if(MainScreen.sMediaPlayer.isPlaying()){
            remoteViewsSmall.setImageViewResource(R.id.noti_play2,R.drawable.ic_pause);
        } else {
            remoteViewsSmall.setImageViewResource(R.id.noti_play2,R.drawable.ic_play);
        }
        remoteViewsSmall.setTextViewText(R.id.noti_songname2,name);
        remoteViewsSmall.setTextColor(R.id.noti_songname2,Color.parseColor("#1d1d1d"));
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
        if(MusicService.sPlayer.isPlaying()){
            remoteViews.setImageViewResource(R.id.noti_play,R.drawable.ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.noti_play,R.drawable.ic_play);
        }
        remoteViews.setTextViewText(R.id.noti_songname1,name);
        remoteViews.setTextColor(R.id.noti_songname1,Color.parseColor("#1d1d1d"));
        remoteViews.setTextColor(R.id.noti_artist1,Color.parseColor("#1d1d1d"));
        remoteViews.setTextViewText(R.id.noti_artist1,artist);
        if(image!=null) {
            remoteViews.setImageViewBitmap(R.id.noti_image1, ThumbnailUtils.extractThumbnail(
                    BitmapFactory.decodeFile(image), 150, 150));
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
        if(MusicService.sPlayer.isPlaying()){
            mBuilder.setOngoing(true);
        } else {
            mBuilder.setOngoing(false);
            closeNoti(context);
        }
//        mBuilder.setContentIntent(remoteViews);
//        mBuilder.setColor(Color.parseColor("#ff6bc7e9"));
        // NotificationCompat.Builder mBuilder;
        //        ...
        // Sets an ID for the notification
        int mNotificationId = 001;
        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, mBuilder.build());
        Notification noti = mBuilder.build();
        return noti;
    }
    public void closeNoti(Context context){
        Intent svc = new Intent(context, MusicService.class);
        svc.putExtra("path","stopmusic");
        context.stopService(svc);
        context.startService(svc);
    }
}
