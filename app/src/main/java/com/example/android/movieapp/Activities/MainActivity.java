package com.example.android.movieapp.Activities;

import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.android.movieapp.Fragments.MovieFragment;
import com.example.android.movieapp.R;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment())
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

       int id = item.getItemId();

        switch (id){
            case R.id.fav:
                MovieFragment.order="fav";
                break;
            case R.id.popular:
                MovieFragment.order="popular";
                break;
            case R.id.top_rated:
                MovieFragment.order="top_rated";
                break;
        }

        return super.onOptionsItemSelected(item);
    }

}
