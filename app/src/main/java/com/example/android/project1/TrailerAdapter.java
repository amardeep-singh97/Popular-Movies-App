package com.example.android.project1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * Created by Amardeep on 9/10/2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    Videos[] Trailer;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Videos Trailer);
    }

    public TrailerAdapter(Context context, Videos[] data, OnItemClickListener listener) {
        Log.v("Trailer Adapter", "  is called");
        this.mContext = context;
        this.Trailer = data;
        this.listener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_trail, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String keyId = Trailer[position].getTrailerKey();
        String TrailerName = Trailer[position].getTrailerName();
        String TrailerType = Trailer[position].getType();
        String thumbnailURL = "http://img.youtube.com/vi/".concat(keyId).concat("/hqdefault.jpg");
        Picasso.with(mContext).load(thumbnailURL).placeholder(R.drawable.thumbnail).into(((MyItemHolder) holder).imageView);
        ((MyItemHolder) holder).NametextView.setText(TrailerName);
        ((MyItemHolder) holder).TypetextView.setText(TrailerType);
        ((MyItemHolder) holder).bind(Trailer[position], listener);
    }

    @Override
    public int getItemCount() {
        if (Trailer == null || Trailer.length == 0) {
            return -1;
        }
        return Trailer.length;
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView NametextView;
        TextView TypetextView;

        public MyItemHolder(View itemView) {
            super(itemView);

            imageView = (ImageView) itemView.findViewById(R.id.trailerImage);
            NametextView = (TextView) itemView.findViewById(R.id.trailer_name);
            TypetextView = (TextView) itemView.findViewById(R.id.trailer_type);
        }

        public void bind(final Videos Trailer, final OnItemClickListener listener) {
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(Trailer);
                }
            });
        }
    }
}
