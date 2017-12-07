package com.example.android.project1;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.GridView;

import com.example.android.project1.Favmovie.MovieContract;
import com.example.android.project1.Favmovie.MovieCursorAdapter;
import com.example.android.project1.Favmovie.MovieDbHelper;

public class FavouriteActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    MovieDbHelper mDbHelper;
    GridView favList;
    private static final int MOVIE_LOADER_ID=0;
    MovieCursorAdapter mCourserAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        favList = (GridView) findViewById(R.id.gridview_fav);
        mCourserAdapter = new MovieCursorAdapter(this,null);
        favList.setAdapter(mCourserAdapter);
        getSupportLoaderManager().initLoader( MOVIE_LOADER_ID,null,this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] Projection = {MovieContract.MovieEntry._ID,
                MovieContract.MovieEntry.COLUMN_MOVIE_NMAE,
                MovieContract.MovieEntry.COLUMN_MOVIE_ID,};
        return new CursorLoader(this, MovieContract.MovieEntry.CONTENT_URI,
                Projection,
                null,
                null,
                null);
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v("FAVOURITE ACTIVITY","POINTER IS ON ONLOADFINISH");
        mCourserAdapter.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCourserAdapter.swapCursor(null);
    }
}
