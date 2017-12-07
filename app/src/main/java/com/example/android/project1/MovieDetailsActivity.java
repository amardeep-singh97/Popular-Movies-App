package com.example.android.project1;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.project1.Favmovie.MovieContract;
import com.squareup.picasso.Picasso;

import java.text.ParseException;

import static com.example.android.project1.R.id.trailersRecyclerView;

public class MovieDetailsActivity extends AppCompatActivity {

    private final String LOG_TAG = MovieDetailsActivity.class.getSimpleName();
    private ListView mReviewListView;
    private ProgressBar mReviewProgressBar;
    private TextView mNoReviewTextview;
    private RecyclerView mTrailerRecyclerView;
    private RecyclerView mReviewsRecyclerView;
    LinearLayoutManager LayoutManager;
    LinearLayoutManager mLayoutManager;
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView tvOriginalTitle = (TextView) findViewById(R.id.textview_original_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.imageview_poster);
        TextView tvOverView = (TextView) findViewById(R.id.textview_overview);
        TextView tvVoteAverage = (TextView) findViewById(R.id.textview_vote_average);
        TextView tvReleaseDate = (TextView) findViewById(R.id.textview_release_date);
        ImageView backPoster = (ImageView) findViewById(R.id.backpath_Image);
        mTrailerRecyclerView = (RecyclerView) findViewById(trailersRecyclerView);
        mReviewsRecyclerView = (RecyclerView)findViewById(R.id.reviewsRecyclerView);
        LayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        Intent intent = getIntent();
        movie = intent.getParcelableExtra(getString(R.string.parcel_movie));
        tvOriginalTitle.setText(movie.getOriginalTitle());

        Picasso.with(this)
                .load(movie.getPosterPath())
                .resize(getResources().getInteger(R.integer.tmdb_poster_w185_width),
                        getResources().getInteger(R.integer.tmdb_poster_w185_height))
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(ivPoster);
        Picasso.with(this)
                .load(movie.getBackPoster())
                .error(R.drawable.not_found)
                .placeholder(R.drawable.searching)
                .into(backPoster);
        String overView = movie.getOverview();
        long MovieId = movie.getMovieId();
        if (overView == null) {
            tvOverView.setTypeface(null, Typeface.ITALIC);
            overView = getResources().getString(R.string.no_summary_found);
        }
        tvOverView.setText(overView);
        tvVoteAverage.setText(movie.getDetailedVoteAverage());
        String releaseDate = movie.getReleaseDate();
        if (releaseDate != null) {
            try {
                releaseDate = DateTimeHelper.getLocalizedDate(this,
                        releaseDate, movie.getDateFormat());
            } catch (ParseException e) {
                Log.e(LOG_TAG, "Error with parsing movie release date", e);
            }
        } else {
            tvReleaseDate.setTypeface(null, Typeface.ITALIC);
            releaseDate = getResources().getString(R.string.no_release_date_found);
        }
        tvReleaseDate.setText(releaseDate);
        getReviewFromTMDb(String.valueOf(MovieId));
        getTrailerFromTMDb(String.valueOf(MovieId));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void getReviewFromTMDb(String movieId) {
        OnReviewTaskCompletion reviewCompleted = new OnReviewTaskCompletion() {
            @Override
            public void onFetchReviewTaskCompleted(Reviews[] reviewses) {
                mReviewsRecyclerView.setLayoutManager(mLayoutManager);
                mReviewsRecyclerView.setHasFixedSize(true);
                mReviewsRecyclerView.setAdapter(new ReviewAdapter(getApplicationContext(),reviewses));
            }
        };
        FetchReviewAsyncTask ReviewTask = new FetchReviewAsyncTask(reviewCompleted, movieId);
        ReviewTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_fav:
                 insertMovie();
                break;
            case R.id.remove_fav:
                 deleteMovie();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getTrailerFromTMDb(String movieId) {
        OnTrailorTaskCompletion trailerCompleted = new OnTrailorTaskCompletion() {
            @Override
            public void OnFetchTrailerTaskCompleted(final Videos[] videoses) {
                mTrailerRecyclerView.setLayoutManager(LayoutManager);
                mTrailerRecyclerView.setHasFixedSize(true);
                Log.v(LOG_TAG, "LAST STEP");
                mTrailerRecyclerView.setAdapter(new TrailerAdapter(getApplicationContext(), videoses, new TrailerAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Videos item) {
                        String url = "https://www.youtube.com/watch?v=".concat(item.getTrailerKey());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                }));
            }
        };
        FetchTrailerAsyncTask Trailertask = new FetchTrailerAsyncTask(trailerCompleted, movieId);
        Trailertask.execute();
    }
    private void insertMovie(){
        Uri mNewUr;
        ContentValues values = new ContentValues();
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_NMAE,movie.getOriginalTitle());
        values.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,movie.getMovieId());
        mNewUr = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI,values);
        if (mNewUr!=null){
            Toast.makeText(getApplicationContext(),"Movie added to favourite",Toast.LENGTH_SHORT).show();
        }
    }
    private void deleteMovie(){
        Uri currentPetUri = ContentUris.withAppendedId(MovieContract.MovieEntry.CONTENT_URI,movie.getMovieId());
        int rowsdeleted = getContentResolver().delete(currentPetUri,null,null);
    }
}
