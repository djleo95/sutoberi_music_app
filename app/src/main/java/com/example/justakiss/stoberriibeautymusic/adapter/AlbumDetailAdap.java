package com.example.justakiss.stoberriibeautymusic.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

/**
 * Created by justakiss on 20/10/2016.
 */
public class AlbumDetailAdap extends
        RecyclerView.Adapter<AlbumDetailAdap.ViewHolder>{

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
        public ImageView mAvatarIV;
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
//            if(MainScreen.sMode == 1){
                mNameTV = (TextView) itemView.findViewById(R.id.tv_songname_listview_songlist);
                mArtistTV = (TextView) itemView.findViewById(R.id.tv_artistname_listview_songlist);
                mAvatarIV = (ImageView) itemView.findViewById(R.id.iv_listview_songlist);
//            } else if(MainScreen.sMode == 2) {
//                mNameTV = (TextView) itemView.findViewById(R.id.tv_songname_gridview_songlist);
//                mArtistTV = (TextView) itemView.findViewById(R.id.tv_artistname_gridview_songlist);
//                mBackground = itemView.findViewById(R.id.ll_image_gridview_songlist);
//            }
        }
    }
    private String[] mArtistsName, mImages, mSongsName, mPath;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public AlbumDetailAdap(Context context, String[] mSongsName, String[] mArtistsName,
                            String[] images, String[] path) {
        this.mContext = context;
        this.mSongsName = mSongsName;
        this.mArtistsName = mArtistsName;
        this.mImages = images;
        this.mPath = path;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public AlbumDetailAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contactView = null;
        // Inflate the custom layout
//        if(MainScreen.sMode ==1){
            contactView = inflater.inflate(R.layout.item_list, parent, false);
//        } else if (MainScreen.sMode ==2){
//            contactView = inflater.inflate(R.layout.item_grid, parent, false);
//        }
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(AlbumDetailAdap.ViewHolder viewHolder, int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView nameTV = viewHolder.mNameTV;;
        TextView artistTV = viewHolder.mArtistTV;
        ImageView avatarIV = viewHolder.mAvatarIV;
        View background = viewHolder.mBackground;
//        if(position==MainScreen.sIndex) {
//            nameTV.setTextColor(Color.parseColor("#EFFA0000"));
//        } else {
//            nameTV.setTextColor(Color.parseColor("#ff2e2e2e"));
//        }
//        if(mSongsName[position].equals(MainScreen.sTitle[MainScreen.sIndex])) {
//            nameTV.setTextColor(Color.parseColor("#EFFA0000"));
////            Log.e("tag","inhere"+MainScreen.sTitle[MainScreen.sIndex]+"index"+MainScreen.sIndex+
////                    "name"+mSongsName[position]);
//        } else {
//            nameTV.setTextColor(Color.parseColor("#ff2e2e2e"));
////            Log.e("tag","inthere"+MainScreen.sTitle[MainScreen.sIndex]+"index"+MainScreen.sIndex+
////                    "name"+mSongsName[position]);
//        }
//        if(MainScreen.sMode == 1) {
            nameTV.setText(mSongsName[position]);
            artistTV.setText(mArtistsName[position]);
            if(mImages[position]!=null) {
                avatarIV.setImageDrawable(getImage(mImages[position]));
            } else {
                avatarIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_music2));
            }
//        } else if(MainScreen.sMode ==2) {
//            nameTV.setText(mSongsName[position]);
//            artistTV.setText(mArtistsName[position]);
//            if(mImages[position]!=null) {
//                background.setBackground(getImage(mImages[position]));
//            } else {
//                background.setBackground(getContext().getResources().getDrawable(R.drawable.ic_music2));
//            }
//        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mSongsName.length;
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 200,200);
        return new BitmapDrawable(bitmap1);
    }
}
