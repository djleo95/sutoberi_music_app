package com.example.justakiss.stoberriibeautymusic.handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;
import com.example.justakiss.stoberriibeautymusic.object.Song;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by justakiss on 20/09/2016.
 */
public class SongDBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "songsManager";
    private static final String TABLE_SONGS = "songs";
    private static final String KEY_ID = "id";
    private static final String KEY_PATH = "path";
    private static final String KEY_TITLE = "song_title";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_ALBUM = "album";
    private static final String KEY_ALBUMID = "album_id";
    private static final String KEY_TRACKID = "track_id";
    private static final String KEY_DURATION = "duration";
    private static final String KEY_IMAGE = "image";
    JSONObject json = new JSONObject();

    public SongDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SONGS + "("
                + KEY_ID + " INTEGER," + KEY_PATH + " TEXT PRIMARY KEY,"
                + KEY_TITLE + " TEXT," + KEY_ARTIST + " TEXT,"
                + KEY_ALBUM + " TEXT," + KEY_ALBUMID + " INTEGER,"
                + KEY_TRACKID + " INTEGER," + KEY_DURATION + " LONG,"
                + KEY_IMAGE + " TEXT"+ ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SONGS);
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public void addSong(Song song) throws JSONException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, song.getId());
        values.put(KEY_PATH, song.getPath());
        values.put(KEY_TITLE, song.getTitle());
        values.put(KEY_ARTIST, song.getArtist());
        values.put(KEY_ALBUM, song.getAlbum());
        values.put(KEY_ALBUMID, song.getAlbumId());
        values.put(KEY_TRACKID, song.getTrackId());
        values.put(KEY_DURATION, song.getDuration());
        values.put(KEY_IMAGE, song.getImage());
        db.insert(TABLE_SONGS, null, values);
        db.close();
    }

    Song getSong(int id) throws JSONException {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_SONGS, new String[] { KEY_ID,
                        KEY_PATH, KEY_TITLE, KEY_ARTIST, KEY_ALBUM, KEY_ALBUMID,
                        KEY_TRACKID, KEY_DURATION, KEY_IMAGE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if(!cursor.moveToFirst()) {
            return null;
        }
        Song song = new Song(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),
                cursor.getString(4), Integer.parseInt(cursor.getString(5)),
                Integer.parseInt(cursor.getString(6)), Integer.parseInt(cursor.getString(7)), cursor.getString(8));
        // return contact
        cursor.close();
        db.close();
        return song;
    }

    public List<Song> getAllSongs() {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT  * FROM " + TABLE_SONGS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setId(Integer.parseInt(cursor.getString(0)));
                song.setPath(cursor.getString(1));
                song.setTitle(cursor.getString(2));
                song.setArtist(cursor.getString(3));
                song.setDuration(Integer.parseInt(cursor.getString(7)));
                song.setImage(cursor.getString(8));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }

    public List<Song> getAllAlbums() {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_SONGS + " GROUP BY " + KEY_ALBUM;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setArtist(cursor.getString(3));
                song.setAlbum(cursor.getString(4));
                song.setImage(cursor.getString(8));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }

    public List<Song> getAllSingers() {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_SONGS + " GROUP BY " + KEY_ARTIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setArtist(cursor.getString(3));
                song.setAlbum(cursor.getString(4));
                song.setImage(cursor.getString(8));
                song.setAlbumId(getNumberOfAlbum(cursor.getString(3)));
                song.setTrackId(getNumberOfSong(cursor.getString(3)));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }
    public int getNumberOfAlbum(String artist) {
        String selectQuery = "SELECT COUNT( DISTINCT "+KEY_ALBUM+") FROM "
                        +TABLE_SONGS+" WHERE "+KEY_ARTIST+" == "+ "\""+artist+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        db.close();
        return count;
    }
    public int getNumberOfSong(String artist) {
        String selectQuery = "SELECT COUNT("+KEY_TITLE+") FROM "
                +TABLE_SONGS+" WHERE "+KEY_ARTIST+" == "+ "\""+artist+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
    public boolean checkExist(String path) {
        String selectQuery = "SELECT  "+ KEY_PATH
                +" FROM " + TABLE_SONGS
                +" WHERE "+ KEY_PATH +" == "+ "\""+path+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor!=null&&cursor.getCount()>0){
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }
    // Updating single Song
//    public int updateSong(Song song) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_PATH, song.getName());
//        values.put(KEY_TITLE, song.getDetail());
//        // updating row
//        return db.update(TABLE_SONGS, values, KEY_ID + " = ?",
//                new String[] { String.valueOf(song.getID()) });
//    }

    public void deleteSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SONGS, KEY_ID + " = ?",
                new String[] { String.valueOf(song.getId()) });
        db.close();
    }


    public int getSongsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SONGS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        db.close();
        // return count
        return cursor.getCount();
    }
    public List<SearchObject> readSong(String searchTerm) {
        List<SearchObject> recordsList = new ArrayList<SearchObject>();
        // select query
        String sql = "";
        sql += "SELECT * FROM " + TABLE_SONGS;
        sql += " WHERE " + KEY_TITLE + " LIKE '%" + searchTerm + "%'";
        sql += " ORDER BY " + KEY_TITLE + " ASC";
//        sql += " LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String info1 = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
                String info2 = cursor.getString(cursor.getColumnIndex(KEY_ARTIST));
                String info3 = cursor.getString(cursor.getColumnIndex(KEY_IMAGE));
                String info4 = cursor.getString(cursor.getColumnIndex(KEY_PATH));
                SearchObject myObject = new SearchObject(info1, info2, info3, info4);
                recordsList.add(myObject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        MainScreen.sSearchSong = recordsList.size();
        String sql2 = "";
        sql2 += "SELECT * FROM " + TABLE_SONGS;
        sql2 += " WHERE " + KEY_ARTIST + " LIKE '%" + searchTerm + "%'";
        sql2 += " GROUP BY " + KEY_ARTIST;
        sql2 += " ORDER BY " + KEY_IMAGE + " ASC";

        Cursor cursor2 = db.rawQuery(sql2, null);

        if (cursor2.moveToFirst()) {
            do {
                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String info1 = cursor2.getString(3);
                String info2 = cursor2.getString(3);
                String info3 = cursor2.getString(cursor2.getColumnIndex(KEY_IMAGE));
                SearchObject myObject = new SearchObject(info1, info2, info3);
                recordsList.add(myObject);
            } while (cursor2.moveToNext());
        }
        cursor2.close();
        MainScreen.sSearchArtist = recordsList.size();
        String sql3 = "";
        sql3 += "SELECT * FROM " + TABLE_SONGS;
        sql3 += " WHERE " + KEY_ALBUM + " LIKE '%" + searchTerm + "%'";
        sql3 += " GROUP BY " + KEY_ALBUM;
        sql3 += " ORDER BY " + KEY_IMAGE + " ASC";

        Cursor cursor3 = db.rawQuery(sql3, null);

        if (cursor3.moveToFirst()) {
            do {
                // int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                String info1 = cursor3.getString(4);
                String info2 = cursor3.getString(3);
                String info3 = cursor3.getString(cursor3.getColumnIndex(KEY_IMAGE));
                SearchObject myObject = new SearchObject(info1, info2, info3);
                recordsList.add(myObject);
            } while (cursor3.moveToNext());
        }
        cursor3.close();
        MainScreen.sSearchAlbum = recordsList.size();
        db.close();
        return recordsList;
    }

    public List<Song> getAllSongOfAlbum(String album) {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery =    "SELECT * FROM " + TABLE_SONGS
                                + " WHERE " + KEY_ALBUM
                                + " = \"" + album + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setPath(cursor.getString(1));
                song.setTitle(cursor.getString(2));
                song.setArtist(cursor.getString(3));
                song.setDuration(Integer.parseInt(cursor.getString(7)));
                song.setImage(cursor.getString(8));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }
    public List<Song> getAllSongOfSinger(String singer) {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_SONGS
                + " WHERE " + KEY_ARTIST
                + " = \"" + singer + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setPath(cursor.getString(1));
                song.setTitle(cursor.getString(2));
                song.setArtist(cursor.getString(3));
                song.setDuration(Integer.parseInt(cursor.getString(7)));
                song.setImage(cursor.getString(8));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }
    public List<Song> getEverythingOfSinger(String singer) {
        List<Song> songList = new ArrayList<Song>();
        String selectQuery = "SELECT * FROM " + TABLE_SONGS
                        + " WHERE " + KEY_ARTIST + " == \'"
                        + singer + "\'" + " GROUP BY " + KEY_ALBUM;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Song song = new Song();
                song.setAlbum(cursor.getString(4));
                song.setImage(cursor.getString(8));
                song.setTrackId(getNumberOfSongInAlbum(cursor.getString(4)));
                songList.add(song);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return songList;
    }
    public int getNumberOfSongInAlbum(String album) {
        String selectQuery = "SELECT COUNT("+KEY_TITLE+") FROM "
                +TABLE_SONGS+" WHERE "+KEY_ALBUM+" == "+ "\""+album+"\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        db.close();
        return count;
    }
}