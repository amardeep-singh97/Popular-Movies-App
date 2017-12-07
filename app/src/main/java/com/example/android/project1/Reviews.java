package com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amardeep on 9/10/2017.
 */

public class Reviews implements Parcelable {
    private String mAuthor;
    private String mContent;
    private String mUrl;
    private String mId;
    public Reviews(){
    }

    public void setAuthorName(String Author){mAuthor=Author;}
    public void setContent(String content){mContent=content;}
    public void setReviewId(String Id){mId=Id;}
    public void setReviewUrl(String url){mUrl=url;}
    public String getAuthorName(){return mAuthor;}
    public String getContent(){return mContent;}
    protected Reviews(Parcel in) {
        mAuthor=in.readString();
        mContent=in.readString();
        mUrl=in.readString();
        mId=in.readString();
    }

    public static final Creator<Reviews> CREATOR = new Creator<Reviews>() {
        @Override
        public Reviews createFromParcel(Parcel in) {
            return new Reviews(in);
        }

        @Override
        public Reviews[] newArray(int size) {
            return new Reviews[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mContent);
        parcel.writeString(mId);
        parcel.writeString(mUrl);
    }
}
