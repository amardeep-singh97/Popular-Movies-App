package com.example.android.project1.Favmovie;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.project1.R;

/**
 * Created by Amardeep on 9/14/2017.
 */

public class MovieCursorAdapter extends CursorAdapter {
    public MovieCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.fav_list,viewGroup,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView MovieName = (TextView)view.findViewById(R.id.movie_name_listview);
        TextView MovieId = (TextView)view.findViewById(R.id.movie_id_listview);
        String name = cursor.getString(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_NMAE));
        Log.v("LOG:",name);
        long id = cursor.getLong(cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_MOVIE_ID));
        MovieName.setText(name);
        MovieId.setText(String.valueOf(id));
    }
}
