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

public class PlayScreenListAdapter extends
        RecyclerView.Adapter<PlayScreenListAdapter.ViewHolder>{

    private OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView mNameTV;
        public TextView mArtistTV;
        public ImageView mAvatarIV;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (listener != null && position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
            mNameTV = (TextView) itemView.findViewById(R.id.tv_songname_listview_playlist);
            mArtistTV = (TextView) itemView.findViewById(R.id.tv_artistname_listview_playlist);
            mAvatarIV = (ImageView) itemView.findViewById(R.id.iv_listview_playlist);
        }


    }
    private String[] mAlbumsName, mArtistsName, mImages, mSongsName, mPath;
    private Context mContext;

    public PlayScreenListAdapter(Context context, String[] mSongsName, String[] mArtistsName,
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
    public PlayScreenListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_playlist, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(PlayScreenListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
//        Contact contact = mContacts.get(position);

        // Set item views based on your views and data model

        TextView nameTV = viewHolder.mNameTV;
        TextView artistTV = viewHolder.mArtistTV;
        ImageView avatarIV = viewHolder.mAvatarIV;
        if(position==MainScreen.sIndex){
            nameTV.setTextColor(Color.parseColor("#EFFA0000"));
        } else {
            nameTV.setTextColor(Color.parseColor("#ff2e2e2e"));
        }
        nameTV.setText(mSongsName[position]);
        artistTV.setText(mArtistsName[position]);
        if(mImages[position]!=null) {
            avatarIV.setImageDrawable(getImage(mImages[position]));
        } else {
            avatarIV.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_music2));
        }

    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mSongsName.length;
    }
    public Drawable getImage(String path) {
        Bitmap bitmap1 = ThumbnailUtils.extractThumbnail(
                BitmapFactory.decodeFile(path), 100,100);
        return new BitmapDrawable(bitmap1);
    }
}