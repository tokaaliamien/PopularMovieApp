package com.example.android.movieapp.Utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Fragments.DetailFragment;
import com.example.android.movieapp.Models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Demo on 2016-11-30.
 */
public class FetchReviews extends AsyncTask<String, Review[], Review[]> {

    private final String LOG_TAG = FetchReviews.class.getSimpleName();

    @Override
    protected Review[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String APIKEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL+params[0]+"/reviews?").buildUpon()
                    .appendQueryParameter(APIKEY_PARAM, BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            Log.e(LOG_TAG, "el URI  " + builtUri.toString());
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                jsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");

            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonStr = null;
            }
            jsonStr = buffer.toString();

        } catch (IOException e) {
            Log.e("FetchReviews", "Error ", e);
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
                    Log.e("FetchReviews", "Error closing stream", e);
                }
            }
        }
        try {
            return getReviewsDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Review[] resultStrs) {
        if (resultStrs != null) {
            DetailFragment.reviewArrayAdapter.clear();
            DetailFragment.reviewArrayList.clear();
            for (Review i : resultStrs) {
                DetailFragment.reviewArrayAdapter.add(i);
                DetailFragment.reviewArrayList.add(i);
            }
        }
    }


    private Review[] getReviewsDataFromJson(String jsonStr)
            throws JSONException {

        final String AUTHOR="author";
        final String CONTENT ="content";
        final String OWM_LIST = "results";

        JSONObject reviewJson = new JSONObject(jsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(OWM_LIST);

        Review[] resultStrs = new Review[reviewArray.length()];

        for (int i = 0; i < reviewArray.length(); i++) {
            JSONObject temp=reviewArray.getJSONObject(i);
            Review review=new Review();
            review.setAuthor(temp.getString(AUTHOR));
            review.setContent(temp.getString(CONTENT));
            //Log.e(i+" getReviewsDataFromJson",temp.getString());
            resultStrs[i]=review;

        }
        return resultStrs;
    }

}
