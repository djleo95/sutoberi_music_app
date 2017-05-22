package com.example.justakiss.stoberriibeautymusic.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;

/**
 * Created by justakiss on 02/11/2016.
 */
public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "com.marothiatechs.customnotification.action.main";
        public static String INIT_ACTION = "com.marothiatechs.customnotification.action.init";
        public static String PREV_ACTION = "com.marothiatechs.customnotification.action.prev";
        public static String PLAY_ACTION = "com.marothiatechs.customnotification.action.play";
        public static String NEXT_ACTION = "com.marothiatechs.customnotification.action.next";
        public static String STARTFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "com.marothiatechs.customnotification.action.stopforeground";

    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }

    public static Bitmap getDefaultAlbumArt(Context context) {
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        try {
//            bm = ThumbnailUtils.extractThumbnail(
//                BitmapFactory.decodeFile(MainScreen.sImage[MainScreen.sIndex]), 150, 150);
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.ic_music2, options);
        } catch (Error ee) {
        } catch (Exception e) {
        }
        return bm;
    }

}
