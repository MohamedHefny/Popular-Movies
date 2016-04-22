package com.example.mohamedhefny.popularmovie_new_tray.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;
import com.example.mohamedhefny.popularmovie_new_tray.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed Hefny on 04/04/2016.
 */
public class MoviesAdapter extends BaseAdapter {

    public static final String posterURL = "http://image.tmdb.org/t/p/w185";

    private List<MovieModel> movieList;
    private Context context;

    public MoviesAdapter(){}

    public MoviesAdapter(Context context , List<MovieModel> movieList){

        this.context = context;
        this.movieList = movieList;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Override
    public MovieModel getItem(int position) {
        return movieList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item , parent , false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        MovieModel movieModel = getItem(position);
//        if(movieModel.getIsFav() == 0){
            String posterPath = posterURL + movieModel.getPosterPath();
            Log.i("poster",posterPath);
            Picasso.with(context).load(posterPath).into(holder.posterImageView);
//        }else {
//
//        }

        return convertView;
    }
}

class ViewHolder{

    ImageView posterImageView;

    public ViewHolder(View convertView){
        posterImageView = (ImageView) convertView.findViewById(R.id.image_poster_item);
    }

        }
