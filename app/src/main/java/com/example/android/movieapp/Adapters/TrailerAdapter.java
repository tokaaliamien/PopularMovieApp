package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movieapp.Fragments.DetailFragment;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Models.Trailer;

import java.util.ArrayList;

/**
 * Created by Demo on 2016-11-28.
 */
public class TrailerAdapter extends ArrayAdapter<Trailer> {


    public TrailerAdapter(Context context, ArrayList<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @Override
    public int getCount() {
        return DetailFragment.trailerArrayList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Trailer trailer = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer_list_item, parent, false);
        }
        TextView trailerName = (TextView) convertView.findViewById(R.id.trailer_name);
        trailerName.setText(trailer.getName());

        return convertView;
    }
}
