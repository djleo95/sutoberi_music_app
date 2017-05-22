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
import com.example.justakiss.stoberriibeautymusic.activity.SingerDetailScreen;
import com.example.justakiss.stoberriibeautymusic.adapter.AlbumsListAdapter;
import com.example.justakiss.stoberriibeautymusic.adapter.SingersListAdapter;
import com.example.justakiss.stoberriibeautymusic.object.Song;
import com.example.justakiss.stoberriibeautymusic.handler.SongDBHandler;

import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by justakiss on 15/09/2016.
 */

public class SingerListFragment extends Fragment{
    private SongDBHandler mDB;
    public static SingersListAdapter sSingerListAdap;
    public SingerListFragment() {
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
        return inflater.inflate(R.layout.fragment_singerlist, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        initView(view);
    }
    public void initView(View view) {
        mDB = new SongDBHandler(getContext());
//        GridView singerList = (GridView) view.findViewById(R.id.gv_main_singertab);
        RecyclerView rvSingers = (RecyclerView) view.findViewById(R.id.rv_SingersList);
//        if(MainScreen.sMode==1) {
//            singerList.setNumColumns(1);
//        } else if (MainScreen.sMode==2) {
//            singerList.setNumColumns(2);
//        }
        final List<Song> singers = mDB.getAllSingers();
        final String[] artist = new String[singers.size()];
        String[] image = new String[singers.size()];
        int[] album_number = new int[singers.size()];
        int[] song_number = new int[singers.size()];
        String a, b;
        int c=0,d=0;
        int index = singers.size()-1;
        for (Song n : singers) {
            a = n.getArtist();
            b = n.getImage();
            c = n.getAlbumId();
            d = n.getTrackId();
//            d = n.getPath();
//            Toast.makeText(this.getContext(),c,Toast.LENGTH_LONG).show();
            artist[index] = a;
            image[index] = b;
            album_number[index] = c;
            song_number[index] = d;
            index--;
        }
//        singerList.setAdapter(
//                  new SingerListAdapter(getContext(), artist,image, album_number, song_number));
        sSingerListAdap = new SingersListAdapter(getContext(), artist,image, album_number, song_number);
        rvSingers.setAdapter(sSingerListAdap);
        // Set layout manager to position the items
        rvSingers.setLayoutManager(new GridLayoutManager(MainScreen.sContext, MainScreen.sMode));
        rvSingers.setHasFixedSize(true);
        rvSingers.setItemAnimator(new SlideInUpAnimator());
        sSingerListAdap.setOnItemClickListener(new SingersListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Context c = getContext();
                Intent intent1 = new Intent(c, SingerDetailScreen.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent1.putExtra("singer",artist[position]);
                c.startActivity(intent1);
            }
        });
        mDB.close();
    }
}