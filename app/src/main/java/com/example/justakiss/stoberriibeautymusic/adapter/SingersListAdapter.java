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

public class SingersListAdapter extends
        RecyclerView.Adapter<SingersListAdapter.ViewHolder> {
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder {
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
            if(MainScreen.sMode ==1){
                mNameTV = (TextView) itemView.findViewById(R.id.tv_songname_listview_songlist);
                mArtistTV = (TextView) itemView.findViewById(R.id.tv_artistname_listview_songlist);
                mAvatarIV = (ImageView) itemView.findViewById(R.id.iv_listview_songlist);
            } else if(MainScreen.sMode ==2) {
                mNameTV = (TextView) itemView.findViewById(R.id.tv_songname_gridview_songlist);
                mArtistTV = (TextView) itemView.findViewById(R.id.tv_artistname_gridview_songlist);
                mBackground = itemView.findViewById(R.id.ll_image_gridview_songlist);
            }
        }
    }
    private String[] mSingersName, mArtistsName, mImages;
    private int[] mSongNumber, mAlbumNumber;
    // Store the context for easy access
    private Context mContext;

    // Pass in the contact array into the constructor
    public SingersListAdapter(Context context, String[] mSingersName, String[] images,
                             int[] album_number, int[] song_number) {
        this.mContext = context;
        this.mImages = images;
        this.mSingersName = mSingersName;
        this.mSongNumber = song_number;
        this.mAlbumNumber = album_number;
    }
    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }
    @Override
    public SingersListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = null;
        // Inflate the custom layout
        if(MainScreen.sMode ==1){
            contactView = inflater.inflate(R.layout.item_list, parent, false);
        } else if (MainScreen.sMode ==2){
            contactView = inflater.inflate(R.layout.item_grid, parent, false);
        }

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SingersListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model
        TextView nameTV = viewHolder.mNameTV;;
        TextView artistTV = viewHolder.mArtistTV;
        ImageView avatarIV = viewHolder.mAvatarIV;
        View background = viewHolder.mBackground;
        //-----------------
        String albums = "albums", songs = "songs";
        nameTV.setText(mSingersName[position]);
        if(mAlbumNumber[position]<2) {
            albums = "album";
        } else if( mSongNumber[position]<2) {
            songs = "song";
        }
        artistTV.setText(mAlbumNumber[position]+" "+albums+" | "+ mSongNumber[position]+" "+songs);
        if(MainScreen.sMode ==1) {
            if(mImages[position]!=null) {
                avatarIV.setImageDrawable(getImage(mImages[position]));
            } else {
                avatarIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_music2));
            }
        } else if(MainScreen.sMode ==2) {
            if(mImages[position]!=null) {
               background.setBackground(getImage(mImages[position]));
            } else {
                background.setBackground(getContext().getResources().getDrawable(R.drawable.ic_music2));
            }
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mSingersName.length;
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 100,100);
        return new BitmapDrawable(bitmap1);
    }
}