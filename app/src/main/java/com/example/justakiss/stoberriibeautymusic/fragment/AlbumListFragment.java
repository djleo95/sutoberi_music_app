package com.example.justakiss.stoberriibeautymusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.justakiss.stoberriibeautymusic.activity.AlbumDetailScreen;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;
import com.example.justakiss.stoberriibeautymusic.adapter.AlbumsListAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.SongsListAdapter;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.object.Song;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by justakiss on 15/09/2016.
 */

public class AlbumListFragment extends Fragment{
    private SongDBHandler mDB;
    public static AlbumsListAdapter sAlbumListAdap;
    public AlbumListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_albumlist, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initView(view);
    }
    public void initView(View view) {
        mDB = new SongDBHandler(getContext());
//        GridView albumList = (GridView) view.findViewById(R.id.gv_main_albumtab);
//        if(MainScreen.sMode==1) {
//            albumList.setNumColumns(1);
//        } else if (MainScreen.sMode==2) {
//            albumList.setNumColumns(2);
//        }
        // Lookup the recyclerview in activity layout
        RecyclerView rvAlbums = (RecyclerView) view.findViewById(R.id.rv_AlbumsList);

        List<Song> albums = mDB.getAllAlbums();
        String[] artist = new String[albums.size()];
        String[] image = new String[albums.size()];
        final String[] album = new String[albums.size()];
        String a, b, c, d;
        int index = albums.size()-1;
        for (Song n : albums) {
            a = n.getAlbum();
            b = n.getArtist();
            c = n.getImage();
//            d = n.getPath();
//            Toast.makeText(this.getContext(),c,Toast.LENGTH_LONG).show();
            album[index] = a;
            artist[index] = b;
            image[index] = c;
//            path[index] = d;
            index--;
        }
        // Create adapter passing in the sample user data
//        AlbumsListAdapter adapter = new AlbumsListAdapter(this, contacts);
        // Attach the adapter to the recyclerview to populate items
        sAlbumListAdap = new AlbumsListAdapter(getContext(), album, artist,image);
        rvAlbums.setAdapter(sAlbumListAdap);
        // Set layout manager to position the items
        rvAlbums.setLayoutManager(new GridLayoutManager(MainScreen.sContext,MainScreen.sMode));
        rvAlbums.setHasFixedSize(true);
        rvAlbums.setItemAnimator(new SlideInUpAnimator());
        sAlbumListAdap.setOnItemClickListener(new AlbumsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Context c = getContext();
                Intent intent1 = new Intent(c, AlbumDetailScreen.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("album",album[position]);
                c.startActivity(intent1);
            }
        });
        // That's all!
//        albumList.setAdapter(new AlbumListAdapter(getContext(), album, artist,image));
        mDB.close();
    }
}