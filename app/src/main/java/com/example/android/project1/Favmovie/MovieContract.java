package com.example.android.project1.Favmovie;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Amardeep on 9/13/2017.
 */

public final class MovieContract {
    private MovieContract(){}
    public static final String CONTENT_AUTHORITY = "com.example.android.movies";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);
    public static final String PATH_MOVIES="movies";

    public static abstract class MovieEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final Uri CONTENT_URI=Uri.withAppendedPath(BASE_CONTENT_URI,PATH_MOVIES);
        public static final String _ID = BaseColumns._ID;
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_NMAE = "name";
        public static final String COLUMN_MOVIE_ID = "movieId";
    }
}
