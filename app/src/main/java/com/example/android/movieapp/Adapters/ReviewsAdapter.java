package com.example.android.movieapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.movieapp.R;
import com.example.android.movieapp.Models.Review;

import java.util.ArrayList;

/**
 * Created by Demo on 2016-11-28.
 */
public class ReviewsAdapter extends ArrayAdapter<Review> {


    public ReviewsAdapter(Context context,ArrayList<Review> reviews) {
        super(context,0, reviews);
      }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Review review=(Review) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_list_item, parent, false);
        }
        TextView author=(TextView) convertView.findViewById(R.id.author);
        author.setText(review.getAuthor());

        TextView content=(TextView)convertView.findViewById(R.id.content);
        content.setText(review.getContent());

        return convertView;
    }
}
