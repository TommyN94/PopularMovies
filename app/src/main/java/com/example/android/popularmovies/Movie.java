package com.example.android.popularmovies;


import android.os.Parcel;
import android.os.Parcelable;

class Movie implements Parcelable {
    String title;
    String overview;
    double rating;
    String releaseDate;
    String moviePosterUrl;

    Movie() {}

    Movie(String title) {
        this.title = title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    String getTitle() {
        return title;
    }

    void setMoviePosterUrl(String moviePosterUrl) {
        this.moviePosterUrl = moviePosterUrl;
    }

    String getMoviePosterUrl() {
        return moviePosterUrl;
    }

    void setOverview(String overview) {
        this.overview = overview;
    }

    String getOverview() {
        return overview;
    }

    void setRating(double rating) {
        this.rating = rating;
    }

    double getRating() {
        return rating;
    }

    void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    String getReleaseDate() {
        return releaseDate;
    }

    public String toString() {
        return title;
    }

    // methods for the Parcelable interface
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(title);
        out.writeString(overview);
        out.writeDouble(rating);
        out.writeString(releaseDate);
        out.writeString(moviePosterUrl);
    }

    public static final Parcelable.Creator<Movie> CREATOR
            = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    private Movie(Parcel in) {
        title = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        releaseDate = in.readString();
        moviePosterUrl = in.readString();
    }
}
