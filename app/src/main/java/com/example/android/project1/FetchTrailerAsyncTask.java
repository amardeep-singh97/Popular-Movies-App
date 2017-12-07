package com.example.android.project1;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Amardeep on 9/10/2017.
 */

public class FetchTrailerAsyncTask extends AsyncTask<String,Void,Videos[]> {
    private final String LOG_TAG = FetchTrailerAsyncTask.class.getSimpleName();
    private String mMovieId;
    private OnTrailorTaskCompletion mListener;
    public FetchTrailerAsyncTask(OnTrailorTaskCompletion listener, String movieId){
        super();
        mListener = listener;
        mMovieId=movieId;
        Log.v(LOG_TAG,"Movie ID is:" + movieId);
    }
    @Override
    protected Videos[] doInBackground(String... strings) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {
            URL url = getApiUrl();
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder builder = new StringBuilder();

            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line).append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            moviesJsonStr = builder.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
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
            Log.v(LOG_TAG,"JSONRESPONSE: " + moviesJsonStr);
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
    private URL getApiUrl() throws MalformedURLException {
        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String MID_URL ="/videos?api_key=";
        final String apikey = BuildConfig.API_KEY;
        final String LAST_URL ="&language=en-US&page=1";
        String Full_url = TMDB_BASE_URL + mMovieId + MID_URL + apikey +LAST_URL;
        Uri built = Uri.parse(Full_url).buildUpon().build();
        return new URL(built.toString());
    }
    private Videos[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException{
        final String TAG_RESULT = "results";
        final String TAG_KEY = "key";
        final String TAG_NAME="name";
        final String TAG_TYPE = "type";
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULT);
        Videos[] Trailer = new Videos[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            Trailer[i]=new Videos();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            Trailer[i].setTrailerKey(movieInfo.getString(TAG_KEY));
            Trailer[i].setTrailerName(movieInfo.getString(TAG_NAME));
            Trailer[i].setType(movieInfo.getString(TAG_TYPE));
        }
        return Trailer;
    }

    @Override
    protected void onPostExecute(Videos[] videoses) {
        super.onPostExecute(videoses);
        Log.v(LOG_TAG,"POST EXECUTION TRAILER FETCHING");
        mListener.OnFetchTrailerTaskCompleted(videoses);
    }
}
