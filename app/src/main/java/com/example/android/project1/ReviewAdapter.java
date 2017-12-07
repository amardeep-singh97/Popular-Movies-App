package com.example.android.project1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Amardeep on 9/10/2017.
 */

public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
   Context mContext;
   Reviews[] mReview;
    public ReviewAdapter(Context context,Reviews[] review){
        this.mContext=context;
        this.mReview=review;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_review, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String Name = mReview[position].getAuthorName();
        String TrailerType = mReview[position].getContent();
        ((MyItemHolder) holder).NametextView.setText(Name);
        ((MyItemHolder) holder).reviewtextView.setText(TrailerType);
    }

    @Override
    public int getItemCount() {
        if(mReview==null||mReview.length==0){return -1;}
        return mReview.length;
    }
    public static class MyItemHolder extends RecyclerView.ViewHolder {
        TextView NametextView;
        TextView reviewtextView;

        public MyItemHolder(View itemView) {
            super(itemView);
            NametextView = (TextView) itemView.findViewById(R.id.AuthorName);
            reviewtextView = (TextView) itemView.findViewById(R.id.ReviewContent);
        }
    }
//    @Override
//    public int getCount() {
//        if(mReview==null||mReview.length==0){return -1;}
//        return mReview.length;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        if(mReview==null||mReview.length==0){return null;}
//        return mReview[i];
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View convertView, ViewGroup viewGroup) {
//        View view= convertView;
//        if(view==null){
//           view = LayoutInflater.from(mContext).inflate(R.layout.list_review,viewGroup,false);
//        }
//        TextView AuthorName = (TextView)view.findViewById(R.id.AuthorName);
//        TextView Content = (TextView)view.findViewById(R.id.ReviewContent);
//        AuthorName.setText(mReview[i].getAuthorName());
//        Content.setText(mReview[i].getContent());
//        return view;
//    }
}
