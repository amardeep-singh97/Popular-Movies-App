package com.example.android.project1;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {
    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private String mOriginalTitle;
    private String mPosterPath;
    private String mOverview;
    private Double mVoteAverage;
    private String mReleaseDate;
    private String mBackPoster;
    private long mMovieId;
    public Movie() {
    }
    public void setOriginalTitle(String originalTitle) {
        mOriginalTitle = originalTitle;
    }
    public void setPosterPath(String posterPath) {
        mPosterPath = posterPath;
    }
    public void setOverview(String overview) {
        if(!overview.equals("null")) {
            mOverview = overview;
        }
    }
    public void setMovieId(long MovieId){mMovieId=MovieId;}
    public void setBackPoster(String Poster){mBackPoster=Poster;}
    public void setVoteAverage(Double voteAverage) {
        mVoteAverage = voteAverage;
    }
    public void setReleaseDate(String releaseDate) {
        if(!releaseDate.equals("null")) {
            mReleaseDate = releaseDate;
        }
    }
    public String getOriginalTitle() {
        return mOriginalTitle;
    }
    public String getPosterPath() {
        final String TMDB_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w185";

        return TMDB_POSTER_BASE_URL + mPosterPath;
    }
    public String getOverview() {
        return mOverview;
    }
    private Double getVoteAverage() {
        return mVoteAverage;
    }
    public String getReleaseDate() {
        return mReleaseDate;
    }
    public long getMovieId(){return mMovieId;}
    public String getDetailedVoteAverage() {
        return String.valueOf(getVoteAverage()) + "/10";
    }
    public String getDateFormat() {
        return DATE_FORMAT;
    }
    public String getBackPoster(){
        final String TMDB_BACK_POSTER_BASE_URL = "https://image.tmdb.org/t/p/w500";
        return TMDB_BACK_POSTER_BASE_URL + mBackPoster;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mBackPoster);
        dest.writeString(mOverview);
        dest.writeValue(mVoteAverage);
        dest.writeString(mReleaseDate);
        dest.writeLong(mMovieId);
    }

    private Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mBackPoster = in.readString();
        mOverview = in.readString();
        mVoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        mReleaseDate = in.readString();
        mMovieId=in.readLong();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
