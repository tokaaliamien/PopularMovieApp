package com.example.android.movieapp.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.example.android.movieapp.Fragments.DetailFragment;
import com.example.android.movieapp.Models.Movie;
import com.example.android.movieapp.R;


public class DetailActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Movie movieStr = (Movie) getIntent().getSerializableExtra("movie");

        if (savedInstanceState == null) {

            DetailFragment detailFragment = new DetailFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("movie",movieStr);
            detailFragment.setArguments(bundle);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, detailFragment)
                    .commit();
        }
    }

}
