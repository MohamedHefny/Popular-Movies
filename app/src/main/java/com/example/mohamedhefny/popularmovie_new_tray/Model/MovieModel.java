package com.example.mohamedhefny.popularmovie_new_tray.Model;

import android.os.Parcel;
import android.os.Parcelable;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Mohamed Hefny on 02/04/2016.
 */
@SimpleSQLTable(table = "MoviesTable", provider = "MovieProvider")

public class MovieModel implements Parcelable{

    @SimpleSQLColumn(value = "col_id")
    private int id;
    @SimpleSQLColumn(value = "col_movieId")
    private String movieId;
    @SimpleSQLColumn(value = "col_posterPath")
    private String posterPath;
    @SimpleSQLColumn(value = "col_title")
    private String title;
    @SimpleSQLColumn(value = "col_releaseDate")
    private String releaseDate;
    @SimpleSQLColumn(value = "col_overview")
    private String overview;
    @SimpleSQLColumn(value = "col_voteAverage")
    private String voteAverage;

    //Setter and Getter For Variables *************************

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    //******************************************************************

    public MovieModel(){};

    public MovieModel(String movieId, String title, String releaseDate, String voteAverage, String overview, String posterPath){
        this.movieId = movieId;
        this.title = title;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.overview = overview;
        this.posterPath = posterPath;
    }

    protected MovieModel(Parcel in) {
        id = in.readInt();
        movieId = in.readString();
        posterPath = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
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
    }
}
