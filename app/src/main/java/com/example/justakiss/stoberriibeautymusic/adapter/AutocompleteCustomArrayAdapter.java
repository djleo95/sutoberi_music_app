package com.example.justakiss.stoberriibeautymusic.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.AlbumDetailScreen;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.activity.PlayScreen;
import com.example.justakiss.stoberriibeautymusic.activity.SingerDetailScreen;
import com.example.justakiss.stoberriibeautymusic.custom.ProgressBar;
import com.example.justakiss.stoberriibeautymusic.fragment.SongListFragment;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;

import java.util.MissingFormatArgumentException;

/**
 * Created by justakiss on 14/10/2016.
 */
public class AutocompleteCustomArrayAdapter extends ArrayAdapter<SearchObject> {
    private TextView textViewItem, textViewItem2;
    private ImageView imgview;
    final String TAG = "AutocompleteCustomArrayAdapter.java";

    Context mContext;
    int layoutResourceId;
    SearchObject data[] = null, data2[] = null, data3[] = null, allData[] = null;

//    /**
//     * state:
//     * 0:song not null
//     * 1:song null
//     * 2:song and artist null
//     * 3:song not null - artist null
//     * 4:song not null - artist not null
//     */
    int state = MainScreen.sSearchSong,
        state2 = MainScreen.sSearchArtist - MainScreen.sSearchSong,
        state3 = MainScreen.sSearchAlbum - MainScreen.sSearchArtist;
    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId,
                                          SearchObject[] allData,
                                          SearchObject[] data,
                                          SearchObject[] data2,
                                          SearchObject[] data3) {

        super(mContext, layoutResourceId, allData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
        this.data2 = data2;
        this.data3 = data3;
        this.allData = allData;
    }

    public AutocompleteCustomArrayAdapter(Context mContext, int layoutResourceId,
                                          SearchObject[] allData) {

        super(mContext, layoutResourceId, allData);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.allData = allData;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        LayoutInflater inflater = ((MainScreen) mContext).getLayoutInflater();
        LayoutInflater inflater = (LayoutInflater)
                mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listView = null;
        try{
            /*
             * The convertView argument is essentially a "ScrapView" as described is Lucas post
             * http://lucasr.org/2012/04/05/performance-tips-for-androids-listview/
             * It will have a non-null value when ListView is asking you recycle the row layout.
             * So, when convertView is not null, you should simply update its contents instead of inflating a new row layout.
             */
            if(convertView == null){
                // inflate the layout
//                LayoutInflater inflater = ((MainScreen) mContext).getLayoutInflater();
                listView = inflater.inflate(layoutResourceId, null);
            } else {
                listView = inflater.inflate(layoutResourceId, null);
            }

            // object item based on the position
//            SearchObject objectItem = data[position];
//            SearchObject objectItem2 = data2[position];
//            SearchObject objectItem3 = data3[position];
            textViewItem = (TextView) listView.findViewById(R.id.textViewItem1);
            textViewItem2 = (TextView) listView.findViewById(R.id.textViewItem2);
            imgview = (ImageView) listView.findViewById(R.id.iv_search);
//            int mode = 0;
            View searchLabel = listView.findViewById(R.id.ll_label);
            TextView textViewLabel = (TextView) listView.findViewById(R.id.tv_search_label);
            // get the TextView and then set the text (item name) and tag (item ID) values

            if(state!=0&&state2!=0) {
                if(position==0) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Songs");
                    displayItem1(allData[position]);
                } else if(position>0&&position<MainScreen.sSearchSong) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                } else if(position==MainScreen.sSearchSong) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Artists");
                    displayItem1(allData[position]);
                } else if(position>MainScreen.sSearchSong&&position<MainScreen.sSearchArtist) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                } else if(state3!=0) {
                    if(position==MainScreen.sSearchArtist) {
                        searchLabel.setVisibility(View.VISIBLE);
                        textViewLabel.setVisibility(View.VISIBLE);
                        textViewLabel.setText("Albums");
                        displayItem1(allData[position]);
                    } else if(position>MainScreen.sSearchArtist&&position<MainScreen.sSearchAlbum) {
                        searchLabel.setVisibility(View.GONE);
                        textViewLabel.setVisibility(View.GONE);
                        displayItem1(allData[position]);
                    }
                }
            } else if(state!=0&&state2==0) {
                if(position==0) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Songs");
                    displayItem1(allData[position]);
                } else if(position>0&&position<MainScreen.sSearchSong) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                } else if(position==MainScreen.sSearchSong) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Albums");
                    displayItem1(allData[position]);
                } else if(position>MainScreen.sSearchSong&&position<MainScreen.sSearchAlbum) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                }
            } else if(state==0&&state2!=0) {
                if(position==0) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Artists");
                    displayItem1(allData[position]);
                } else if(position>0&&position<MainScreen.sSearchArtist) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                } else if(state3!=0) {
                    if(position==MainScreen.sSearchArtist) {
                        searchLabel.setVisibility(View.VISIBLE);
                        textViewLabel.setVisibility(View.VISIBLE);
                        textViewLabel.setText("Albums");
                        displayItem1(allData[position]);
                    } else if(position>MainScreen.sSearchArtist&&position<MainScreen.sSearchAlbum) {
                        searchLabel.setVisibility(View.GONE);
                        textViewLabel.setVisibility(View.GONE);
                        displayItem1(allData[position]);
                    }
                }
            } else if(state==0&&state2==0&&state3!=0) {
                if(position==0) {
                    searchLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setVisibility(View.VISIBLE);
                    textViewLabel.setText("Albums");
                    displayItem1(allData[position]);
                } else if(position>0&&position<MainScreen.sSearchAlbum) {
                    searchLabel.setVisibility(View.GONE);
                    textViewLabel.setVisibility(View.GONE);
                    displayItem1(allData[position]);
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listView != null) {
            listView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(state!=0&&state2!=0) {
                        if(position>=0&&position<MainScreen.sSearchSong) {
                            //song
                            new ProgressBar(mContext,
                                    allData[position].Info1,
                                    allData[position].Info3,
                                    allData[position].Info2,
                                    allData[position].Info4);
                        } else if(position>=MainScreen.sSearchSong&&position<MainScreen.sSearchArtist) {
                            //artist
                            gotoArtist(allData[position].Info1);
                        } else if(state3!=0) {
                            if(position>=MainScreen.sSearchArtist&&position<MainScreen.sSearchAlbum) {
                                //album
                                gotoAlbum(allData[position].Info1);
                            }
                        }
                    } else if(state!=0&&state2==0) {
                        if(position>=0&&position<MainScreen.sSearchSong) {
                            //song
                            new ProgressBar(mContext,
                                    allData[position].Info1,
                                    allData[position].Info3,
                                    allData[position].Info2,
                                    allData[position].Info4);
                        } else if(position>=MainScreen.sSearchSong&&position<MainScreen.sSearchAlbum) {
                            //album
                            gotoAlbum(allData[position].Info1);
                        }
                    } else if(state==0&&state2!=0) {
                        if(position>=0&&position<MainScreen.sSearchArtist) {
                            //artist
                            gotoArtist(allData[position].Info1);
                        } else if(state3!=0) {
                            if(position>=MainScreen.sSearchArtist&&position<MainScreen.sSearchAlbum) {
                                //album
                                gotoAlbum(allData[position].Info1);
                            }
                        }
                    } else if(state==0&&state2==0&&state3!=0) {
                        if(position>=0&&position<MainScreen.sSearchAlbum) {
                            //album
                            gotoAlbum(allData[position].Info1);
                        }
                    }
                    if(MainScreen.sIsInAlbumDetail == 1) {
                        AlbumDetailScreen.clickCloseSearch();
                        AlbumDetailScreen.sAlbumDetailAdap.notifyDataSetChanged();
                    } else {
                        if (MainScreen.sIsInPlayScreen == 1){
                            PlayScreen.clickCloseSearch();
                        } else {
                            MainScreen.clickCloseSearch();
                        }
                    }
                    SongListFragment.sSongListAdap.notifyDataSetChanged();
                }
            });
        }
        return listView;
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 50,50);
        return new BitmapDrawable(bitmap1);
    }
    public void displayItem1 (SearchObject objectItem) {
        textViewItem.setText(objectItem.Info1);
        textViewItem2.setText(objectItem.Info2);
        if(objectItem.Info3!=null){
            imgview.setImageDrawable(getImage(objectItem.Info3));
        } else {
            imgview.setImageDrawable(MainScreen.sContext.getResources().getDrawable(R.drawable.ic_music2));
        }
    }
    @Override
    public int getCount() {
        return allData.length;
    }
    public void gotoAlbum(String x) {
        Context c = getContext();
        Intent intent1 = new Intent(c, AlbumDetailScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("album",x);
        c.startActivity(intent1);
    }
    public void gotoArtist(String x) {
        Context c = getContext();
        Intent intent1 = new Intent(c, SingerDetailScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("singer",x);
        c.startActivity(intent1);
    }
    public void gotoPlay() {
        Context c = getContext();
        Intent intent1 = new Intent(c, PlayScreen.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent1.putExtra("singer",x);
        c.startActivity(intent1);
    }
}