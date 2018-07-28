package com.example.android.movieapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.android.movieapp.Activities.DetailActivity;
import com.example.android.movieapp.Adapters.ImageAdapter;
import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Models.Movie;
import com.example.android.movieapp.R;
import com.example.android.movieapp.Utilities.UserDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MovieFragment extends Fragment {
    private static final String LOG_TAG = MovieFragment.class.getSimpleName();
    static ArrayList<Movie> movieArrayList = new ArrayList<>();
    public static String order;
    ImageAdapter imageAdapter;
    FetchMovieTask fetchMovieTask;
    SQLiteDatabase sqLiteDatabase;
    UserDbHelper userDbHelper;
    Cursor cursor;

    public MovieFragment() {
        order = "popular";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        updateMovies();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.fav:
                MovieFragment.order = "fav";
                updateMovies();
                break;
            case R.id.popular:
                MovieFragment.order = "popular";
                updateMovies();
                break;
            case R.id.top_rated:
                MovieFragment.order = "top_rated";
                updateMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final GridView list = (GridView) rootView.findViewById(R.id.gridView);

        ArrayList<String> arrayList = new ArrayList<>();
        imageAdapter = new ImageAdapter(getActivity(), arrayList);
        list.setAdapter(imageAdapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                Movie item = movieArrayList.get(position);

                if (isOnline()) {

                    if (getActivity().findViewById(R.id.container2) != null) {
                        DetailFragment detailFragment = new DetailFragment();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("movie", item);
                        detailFragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container2, detailFragment)
                                .commit();
                    } else {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra("movie",item);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rootView;
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    private void updateMovies() {
        if (isOnline()) {
            if (order.equals("fav")) {
                favoriteMovies();
            } else {
                fetchMovieTask = new FetchMovieTask();
                fetchMovieTask.execute(order);
            }
        } else {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies();
    }

    public void favoriteMovies() {

        imageAdapter.clear();
        movieArrayList.clear();

        userDbHelper = new UserDbHelper(getActivity().getApplicationContext());
        sqLiteDatabase = userDbHelper.getReadableDatabase();
        cursor = userDbHelper.getCursor(sqLiteDatabase);

        if (cursor.moveToFirst()) {
            do {
                Movie temp = new Movie();

                temp.setOriginalTitle(cursor.getString(1));
                temp.setId(cursor.getString(0));
                temp.setOverview(cursor.getString(2));
                temp.setPosterPath(cursor.getString(3));
                temp.setRelease_date(cursor.getString(4));
                temp.setVoteAverage(cursor.getString(5));

                movieArrayList.add(temp);
                imageAdapter.add(temp.getPosterPath());

            } while (cursor.moveToNext());
        }

    }


    public class FetchMovieTask extends AsyncTask<String, Movie[], Movie[]> {

        private final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        @Override
        protected Movie[] doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = null;

            try {

                final String MOVIE_BASE_URL =
                        "http://api.themoviedb.org/3/movie/";
                final String APPID_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL + params[0] + "?").buildUpon()
                        .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.e(LOG_TAG, "el URI  " + builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                jsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully getCursor the weather data, there's no point in attempting
                // to parse it.
                jsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            try {
                return getMovieDataFromJson(jsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Movie[] resultStrs) {
            if (resultStrs != null) {
                imageAdapter.clear();
                movieArrayList.clear();

                for (Movie i : resultStrs) {
                    imageAdapter.add(i.getPosterPath());
                    movieArrayList.add(i);
                }
            }
        }


        private Movie[] getMovieDataFromJson(String jsonStr)
                throws JSONException {

            final String POSTER_PATH = "poster_path";
            final String TITLE = "original_title";
            final String REALSE_DATE = "release_date";
            final String VOTE = "vote_average";
            final String OVERVIEW = "overview";
            final String ID = "id";
            final String OWM_LIST = "results";

            JSONObject movieJson = new JSONObject(jsonStr);
            Log.e(LOG_TAG, "Json str: " + jsonStr.toString());
            JSONArray movieArray = movieJson.getJSONArray(OWM_LIST);

            Movie[] resultStrs = new Movie[movieArray.length()];

            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject temp = movieArray.getJSONObject(i);
                Movie movie = new Movie();
                movie.setPosterPath(temp.getString(POSTER_PATH));
                movie.setId(temp.getString(ID));
                movie.setOriginalTitle(temp.getString(TITLE));
                movie.setOverview(temp.getString(OVERVIEW));
                movie.setRelease_date(temp.getString(REALSE_DATE));
                movie.setVoteAverage(temp.getString(VOTE));
                resultStrs[i] = movie;
            }
            return resultStrs;
        }

    }

}







