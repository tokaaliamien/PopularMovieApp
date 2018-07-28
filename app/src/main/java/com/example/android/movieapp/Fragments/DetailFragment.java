package com.example.android.movieapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movieapp.Adapters.ReviewsAdapter;
import com.example.android.movieapp.Adapters.TrailerAdapter;
import com.example.android.movieapp.Models.Movie;
import com.example.android.movieapp.Models.Review;
import com.example.android.movieapp.Models.Trailer;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Utilities.FetchReviews;
import com.example.android.movieapp.Utilities.FetchTrailers;
import com.example.android.movieapp.Utilities.UserDbHelper;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;


public class DetailFragment extends Fragment {

    private static final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static TrailerAdapter trailerArrayAdapter;
    public static ArrayList<Trailer> trailerArrayList;
    public static ReviewsAdapter reviewArrayAdapter;
    public static ArrayList<Review> reviewArrayList;
    public FetchTrailers fetchTrailers;
    public FetchReviews fetchReviews;
    Context context;
    UserDbHelper userDbHelper;
    SQLiteDatabase sqLiteDatabase;
    Movie movieStr;

    public DetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        final ListView trailersList = (ListView) rootView.findViewById(R.id.trailersListView);
        final ListView reviewsList = (ListView) rootView.findViewById(R.id.reviewsListView);
        ImageButton button = (ImageButton) rootView.findViewById(R.id.favorite_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDbHelper = new UserDbHelper(context);
                sqLiteDatabase = userDbHelper.getWritableDatabase();
                userDbHelper.add(movieStr, sqLiteDatabase);
                Toast.makeText(context, movieStr.getOriginalTitle() + " is added to favorites", Toast.LENGTH_LONG).show();
                userDbHelper.close();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            movieStr = (Movie) bundle.getSerializable("movie");
            ((TextView) rootView.findViewById(R.id.title)).setText(movieStr.getOriginalTitle());
            ((TextView) rootView.findViewById(R.id.overview)).setText(movieStr.getOverview());
            ((TextView) rootView.findViewById(R.id.realse_date)).setText(movieStr.getRelease_date());
            ((TextView) rootView.findViewById(R.id.vote)).setText(movieStr.getVoteAverage());
            ImageView image = (ImageView) rootView.findViewById(R.id.poster_path);


            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + movieStr.getPosterPath()).into(image);

            fetchTrailers = new FetchTrailers();
            fetchReviews = new FetchReviews();
            try {
                trailerArrayList = new ArrayList<>(Arrays.asList(fetchTrailers.execute(movieStr.getId()).get()));
                reviewArrayList = new ArrayList<>(Arrays.asList(fetchReviews.execute(movieStr.getId()).get()));

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        trailerArrayAdapter = new TrailerAdapter(getActivity(), trailerArrayList);
        reviewArrayAdapter = new ReviewsAdapter(getActivity(), reviewArrayList);

        trailersList.setAdapter(trailerArrayAdapter);
        reviewsList.setAdapter(reviewArrayAdapter);

        trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Trailer trailer = trailerArrayList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String baseUrl = "https://www.youtube.com/watch?v=";
                intent.setData(Uri.parse(baseUrl + trailer.getKey()));
                startActivity(intent);
            }
        });
        return rootView;
    }

}



