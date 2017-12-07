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

class FetchMovieAsyncTask extends AsyncTask<String, Void, Movie[]> {
    private final String LOG_TAG = FetchMovieAsyncTask.class.getSimpleName();
    private final String mApiKey;
    private final OnTaskCompleted mListener;
    public FetchMovieAsyncTask(OnTaskCompleted listener, String apiKey) {
        super();

        mListener = listener;
        mApiKey = BuildConfig.API_KEY;
    }

    @Override
    protected Movie[] doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String moviesJsonStr = null;

        try {
            URL url = getApiUrl(params);
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
            return getMoviesDataFromJson(moviesJsonStr);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return null;
    }
    private Movie[] getMoviesDataFromJson(String moviesJsonStr) throws JSONException {
        final String TAG_RESULTS = "results";
        final String TAG_ORIGINAL_TITLE = "original_title";
        final String TAG_POSTER_PATH = "poster_path";
        final String TAG_OVERVIEW = "overview";
        final String TAG_VOTE_AVERAGE = "vote_average";
        final String TAG_RELEASE_DATE = "release_date";
        final String TAG_BACK_POSTER = "backdrop_path";
        final String TAG_MOVIE_ID = "id";
        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray resultsArray = moviesJson.getJSONArray(TAG_RESULTS);
        Movie[] movies = new Movie[resultsArray.length()];
        for (int i = 0; i < resultsArray.length(); i++) {
                       movies[i] = new Movie();
            JSONObject movieInfo = resultsArray.getJSONObject(i);
            movies[i].setOriginalTitle(movieInfo.getString(TAG_ORIGINAL_TITLE));
            movies[i].setPosterPath(movieInfo.getString(TAG_POSTER_PATH));
            movies[i].setBackPoster(movieInfo.getString(TAG_BACK_POSTER));
            movies[i].setOverview(movieInfo.getString(TAG_OVERVIEW));
            movies[i].setVoteAverage(movieInfo.getDouble(TAG_VOTE_AVERAGE));
            movies[i].setReleaseDate(movieInfo.getString(TAG_RELEASE_DATE));
            movies[i].setMovieId(movieInfo.getLong(TAG_MOVIE_ID));
        }
        return movies;
    }
    private URL getApiUrl(String[] parameters) throws MalformedURLException {
        final String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie/";
        final String NEW_URL = TMDB_BASE_URL + parameters[0] + "?api_key=" + BuildConfig.API_KEY;
        Uri builtUri = Uri.parse(NEW_URL).buildUpon()
                .build();
         Log.v("FETCHMOVIE",String.valueOf(builtUri));
        return new URL(builtUri.toString());
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        mListener.onFetchMoviesTaskCompleted(movies);
    }
}
