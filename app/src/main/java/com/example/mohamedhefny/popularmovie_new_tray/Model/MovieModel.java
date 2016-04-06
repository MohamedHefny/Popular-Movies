package com.example.mohamedhefny.popularmovie_new_tray.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mohamed Hefny on 02/04/2016.
 */
public class MovieModel implements Parcelable{

    private int id;
    private String movieId;
    private String posterPath;
    private String title;
    private String releaseDate;
    private String overview;
    private String voteAverage;
    private int isFav;

    //Seter and Getter For Variables *************************
    public int getIsFav() {
        return isFav;
    }

    public void setIsFav(int isFav) {
        this.isFav = isFav;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    //******************************************************************

    public MovieModel(){};

    public MovieModel(String movieId, String title, String releaseDate, String voteAverage, String overview, String posterPath, int isFav){
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
        this.isFav = isFav;
    }

    public MovieModel(String movieId, String title, String posterPath, int isFav) {
        this.movieId = movieId;
        this.title = title;
        this.posterPath = posterPath;
        this.isFav = isFav;
    }


    protected MovieModel(Parcel in) {
        id = in.readInt();
        movieId = in.readString();
        posterPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        isFav = in.readInt();
    }

    public static final Creator<MovieModel> CREATOR = new Creator<MovieModel>() {
        @Override
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        @Override
        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(movieId);
        dest.writeString(posterPath);
        dest.writeString(title);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeInt(isFav);
    }
}
