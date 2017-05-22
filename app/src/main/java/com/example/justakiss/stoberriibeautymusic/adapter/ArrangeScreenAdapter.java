package com.example.justakiss.stoberriibeautymusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.object.Song;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by justakiss on 01/11/2016.
 */
public class ArrangeScreenAdapter extends
        RecyclerView.Adapter<ArrangeScreenAdapter.ViewHolder>
        implements ItemTouchHelperAdapter {

//    public interface OnDragStartListener {
//        void onDragStarted(RecyclerView.ViewHolder viewHolder);
//    }
//    private final OnDragStartListener mDragStartListener;
//
//    public ArrangeScreenAdapter(OnDragStartListener dragStartListener) {
//        mDragStartListener = dragStartListener;
////        mItems.addAll(Arrays.asList(STRINGS));
//    }
    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        String temp;
        //path
        temp = MainScreen.sNowPlayingPlaylist[fromPosition];
        MainScreen.sNowPlayingPlaylist[fromPosition] = MainScreen.sNowPlayingPlaylist[toPosition];
        MainScreen.sNowPlayingPlaylist[toPosition] = temp;
        //name
        temp = MainScreen.sTitle[fromPosition];
        MainScreen.sTitle[fromPosition] = MainScreen.sTitle[toPosition];
        MainScreen.sTitle[toPosition] = temp;
        //artist
        temp = MainScreen.sArtist[fromPosition];
        MainScreen.sArtist[fromPosition] = MainScreen.sArtist[toPosition];
        MainScreen.sArtist[toPosition] = temp;
        //image
        temp = MainScreen.sImage[fromPosition];
        MainScreen.sImage[fromPosition] = MainScreen.sImage[toPosition];
        MainScreen.sImage[toPosition] = temp;
        notifyDataSetChanged();
    }

    @Override
    public void onItemDismiss(int position) {
        ArrayList<String> path = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        ArrayList<String> artist = new ArrayList<>();
        ArrayList<String> image = new ArrayList<>();
        int index = 0;
        for (String n : MainScreen.sNowPlayingPlaylist) {
            path.add(MainScreen.sNowPlayingPlaylist[index]);
            title.add(MainScreen.sTitle[index]);
            artist.add(MainScreen.sArtist[index]);
            image.add(MainScreen.sImage[index]);
            index++;
        }
        //path
        path.remove(position);
        //name
        title.remove(position);
        //artist
        artist.remove(position);
        //image
        image.remove(position);
        int index2 = 0;
        for (String n : path) {
            MainScreen.sNowPlayingPlaylist[index2] = path.get(index2);
            MainScreen.sTitle[index2] = title.get(index2);
            MainScreen.sArtist[index2] = artist.get(index2);
            MainScreen.sImage[index2] = image.get(index2);
            index2++;
        }
        notifyDataSetChanged();

    }

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView mNameTV;
        public TextView mArtistTV;
        public ImageView mAvatarIV, mHandler;
        public View mBackground;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
            mNameTV = (TextView) itemView.findViewById(R.id.tv_listview_songname);
            mArtistTV = (TextView) itemView.findViewById(R.id.tv_listview_songartist);
            mAvatarIV = (ImageView) itemView.findViewById(R.id.iv_listview_avatar);
            mHandler = (ImageView) itemView.findViewById(R.id.handle);
        }
    }
    private String[] mImages, mName, mArtist;
//    private int[] mNumber;
    // Store the context for easy access
    private Context mContext;
    // Pass in the contact array into the constructor
    public ArrangeScreenAdapter(Context context) {
        this.mContext = context;
        this.mName = MainScreen.sTitle;
        this.mArtist = MainScreen.sArtist;
        this.mImages = MainScreen.sImage;
//        mDragStartListener = null;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public ArrangeScreenAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView;
        // Inflate the custom layout
        contactView = inflater.inflate(R.layout.item_arrange, parent, false);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ArrangeScreenAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView nameTV = viewHolder.mNameTV;
        TextView artistTV = viewHolder.mArtistTV;
        ImageView avatarIV = viewHolder.mAvatarIV;
        ImageView holder = viewHolder.mHandler;

        nameTV.setText(mName[position]);
        artistTV.setText(mArtist[position]);
        if(mImages[position]!=null) {
            avatarIV.setImageDrawable(getImage(mImages[position]));
        } else {
            avatarIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_music2));
        }
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mName.length;
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 100,100);
        return new BitmapDrawable(bitmap1);
    }
}
