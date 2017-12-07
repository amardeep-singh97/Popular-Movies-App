package com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Amardeep on 9/10/2017.
 */

public class Videos implements Parcelable {
    private String mTrailerName;
    private String mTrailerKey;
    private String mType;
    public Videos(){
    }

    public void setTrailerName(String TrailerName){
        mTrailerName =TrailerName;
    }
    public void setTrailerKey(String TrailerKey){
        mTrailerKey=TrailerKey;
    }
    public void setType(String Type){
        mType = Type;
    }
    public String getTrailerName(){return mTrailerName;}
    public String getTrailerKey(){return mTrailerKey;}
    public String getType(){return mType;}

    protected Videos(Parcel in) {
        mTrailerName=in.readString();
        mTrailerKey=in.readString();
        mType=in.readString();
    }

    public static final Creator<Videos> CREATOR = new Creator<Videos>() {
        @Override
        public Videos createFromParcel(Parcel in) {
            return new Videos(in);
        }

        @Override
        public Videos[] newArray(int size) {
            return new Videos[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mTrailerName);
        parcel.writeString(mTrailerKey);
        parcel.writeString(mType);
    }
}
