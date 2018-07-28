package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Demo on 2016-10-25.
 */
public class ImageAdapter extends ArrayAdapter {


    Context mContext;
    ArrayList posterPaths;

    public ImageAdapter(Context context,ArrayList<String> posterPaths) {
        super(context, 0, posterPaths);
        this.mContext=context;
        this.posterPaths=posterPaths;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item, parent, false);
        }
        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w92/"+posterPaths.get(position))
                .into((ImageView)convertView.findViewById(R.id.imageView));

        return convertView;
    }

}


