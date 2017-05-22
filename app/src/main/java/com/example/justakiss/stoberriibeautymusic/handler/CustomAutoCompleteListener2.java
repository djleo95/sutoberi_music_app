package com.example.justakiss.stoberriibeautymusic.handler;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.justakiss.stoberriibeautymusic.R;
import com.example.justakiss.stoberriibeautymusic.activity.AlbumDetailScreen;
import com.example.justakiss.stoberriibeautymusic.activity.MainScreen;
import com.example.justakiss.stoberriibeautymusic.adapter.AutocompleteCustomArrayAdapter;
import com.example.justakiss.stoberriibeautymusic.object.SearchObject;

/**
 * Created by justakiss on 14/10/2016.
 */
public class CustomAutoCompleteListener2 implements TextWatcher {

    public static final String TAG = "CustomAutoCompleteListener.java";
    Context context;

    public CustomAutoCompleteListener2(Context context){
        this.context = context;
    }

    @Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
        // TODO Auto-generated method stub

    }


    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
        AlbumDetailScreen mainActivity = ((AlbumDetailScreen) context);

        // query the database based on the user input
        SearchObject[] item = mainActivity.getItemsFromDb(userInput.toString());
        int x=0;
//        for (SearchObject record : item) {
//            allItem[x] = record;
////            item[x].Info1 = record.Info1;
////            item[x].Info2 = record.Info2;
////            item[x].Info3 = record.Info3;
//            x++;
//            Log.e("-------x","="+x);
//        }
//        // update the adapater
//        mainActivity.sMyAdapter = new AutocompleteCustomArrayAdapter(context, R.layout.item_search,
//                allItem, item, item2, item3);
        mainActivity.sAutoAdapAlbum = new AutocompleteCustomArrayAdapter(context, R.layout.item_search,
                item);
//        Log.e("in","hereallitem"+item.length);
        mainActivity.sAutoAdapAlbum.notifyDataSetChanged();
        mainActivity.sAutoCompleteAlbum.setAdapter(mainActivity.sAutoAdapAlbum);
    }

}