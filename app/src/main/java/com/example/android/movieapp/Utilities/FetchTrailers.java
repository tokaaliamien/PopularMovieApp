package com.example.android.movieapp.Utilities;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.android.movieapp.BuildConfig;
import com.example.android.movieapp.Fragments.DetailFragment;
import com.example.android.movieapp.Models.Trailer;

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
public class FetchTrailers extends AsyncTask<String, Trailer[], Trailer[]> {

    private final String LOG_TAG = FetchTrailers.class.getSimpleName();

    @Override
    protected Trailer[] doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonStr = null;

        try {

            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie/";
            final String APPID_PARAM = "api_key";

            Uri builtUri = Uri.parse(MOVIE_BASE_URL+params[0]+"/videos?").buildUpon()
                    .appendQueryParameter(APPID_PARAM, BuildConfig.API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

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
            Log.e(LOG_TAG, "b3d elloop" + buffer);


            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                jsonStr = null;
            }
            jsonStr = buffer.toString();

            Log.e(LOG_TAG, "ay 7aga" + jsonStr);

        } catch (IOException e) {
            Log.e("FetchTrailers", "Error ", e);
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
                    Log.e("FetchTrailers", "Error closing stream", e);
                }
            }
        }
        try {
            return getTrailerDataFromJson(jsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Trailer[] resultStrs) {
        if (resultStrs != null) {
            DetailFragment.trailerArrayAdapter.clear();
            DetailFragment.trailerArrayList.clear();
            for (Trailer i : resultStrs) {
                DetailFragment.trailerArrayAdapter.add(i);
                DetailFragment.trailerArrayList.add(i);
            }
        }
    }


    private Trailer[] getTrailerDataFromJson(String jsonStr)
            throws JSONException {

        final String ID ="id";
        final String KEY="key";
        final String NAME ="name";
        final String OWM_LIST = "results";


        JSONObject trailerJson = new JSONObject(jsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray(OWM_LIST);

        Trailer[] resultStrs = new Trailer[trailerArray.length()];

        for (int i = 0; i < trailerArray.length(); i++) {
            JSONObject temp=trailerArray.getJSONObject(i);
            Trailer trailer=new Trailer();
            trailer.setId(temp.getString(ID));
            trailer.setKey(temp.getString(KEY));
            trailer.setName(temp.getString(NAME));
            resultStrs[i]=trailer;
        }
        return resultStrs;
    }

}
