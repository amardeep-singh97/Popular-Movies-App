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

public class FetchReviewAsyncTask extends AsyncTask<String, Void, Reviews[]> {
    private final String LOG_TAG = FetchReviewAsyncTask.class.getSimpleName();
    private final String mMovieId;
    private final OnReviewTaskCompletion mListener;
    public FetchReviewAsyncTask(OnReviewTaskCompletion listener, String movieId) {
        super();

        mListener = listener;
        mMovieId=movieId;
        Log.v(LOG_TAG,"Movie ID is:" + movieId);
    }
    @Override
    protected Reviews[] doInBackground(String... strings) {
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
        final String MID_URL ="/reviews?api_key=";
        final String apikey = BuildConfig.API_KEY;
        final String LAST_URL ="&language=en-US&page=1";
//        final String SORT_BY_PARAM = "sort_by";
//        final String API_KEY_PARAM = "api_key";
        String Full_url = TMDB_BASE_URL + mMovieId + MID_URL + apikey +LAST_URL;
//        Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
//                .appendQueryParameter(SORT_BY_PARAM, parameters[0])
//                .appendQueryParameter(API_KEY_PARAM, mApiKey)
//                .build();
        Uri built = Uri.parse(Full_url).buildUpon().build();
        return new URL(built.toString());
    }
    private Reviews[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException{
        final String TAG_RESULT = "results";
        final String TAG_ID = "id";
        final String TAG_AUTHOR="author";
        final String TAG_CONTENT = "content";
        final String TAG_URL = "url";
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULT);
        Reviews[] review = new Reviews[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
            review[i]=new Reviews();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            review[i].setAuthorName(movieInfo.getString(TAG_AUTHOR));
            review[i].setContent(movieInfo.getString(TAG_CONTENT));
            review[i].setReviewId(movieInfo.getString(TAG_ID));
            review[i].setReviewUrl(movieInfo.getString(TAG_URL));
        }
        return review;
    }

    @Override
    protected void onPostExecute(Reviews[] reviewses) {
        super.onPostExecute(reviewses);
        mListener.onFetchReviewTaskCompleted(reviewses);
    }
}
