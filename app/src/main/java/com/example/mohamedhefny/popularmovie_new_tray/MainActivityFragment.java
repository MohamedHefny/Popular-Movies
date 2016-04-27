package com.example.mohamedhefny.popularmovie_new_tray;

import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.mohamedhefny.popularmovie_new_tray.Adapters.MoviesAdapter;
import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;
import com.example.mohamedhefny.popularmovie_new_tray.Model.MoviesTableTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivityFragment extends Fragment implements AdapterView.OnItemClickListener {

    //Moveis URL Vriables
    static final String movies_URL = "https://api.themoviedb.org/3/movie/";
    static final String sort_Popular = "popular";
    static final String sort_Top_Rated = "top_rated";
    static final String API_Kay = "api_key";

    //Final MovieModel URL
    String PopularMoveisURL ;
    String TopRatedMoviesURL ;
    String FavoriteSort = "" ;

    GridView gridView;
    MoviesAdapter adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_main_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.main_posters_fragment, container , false);
        gridView = (GridView) rootView.findViewById(R.id.grid_posters_view);
        gridView.setOnItemClickListener(this);
        return rootView ;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);

        //Popular MovieModel URL Build
        Uri popularMoviesURL = Uri.parse(movies_URL).buildUpon().appendPath(sort_Popular)
                .appendQueryParameter(API_Kay,getString(R.string.myApiKeyValue)).build();
        PopularMoveisURL = popularMoviesURL.toString();

        //Top_Rated MovieModel URL Build
        Uri topRatedMoviesURL = Uri.parse(movies_URL).buildUpon().appendPath(sort_Top_Rated)
                .appendQueryParameter(API_Kay,getString(R.string.myApiKeyValue)).build();
        TopRatedMoviesURL = topRatedMoviesURL.toString();

    }

    @Override
    public void onStart() {
        super.onStart();
        if(isNetworkConnected()) {
            if(FavoriteSort.equals("Favorite")){
                FetchFavoriteMovies fetchFavoriteMoviesTask = new FetchFavoriteMovies();
                fetchFavoriteMoviesTask.execute();
            }
            else {
                updateData(PopularMoveisURL);
            }
        }else { //If the Internet not connected
            Toast.makeText(getContext(), "No Internet Connection and this is your Favorite Movies", Toast.LENGTH_SHORT).show();
            FetchFavoriteMovies fetchFavoriteMovies = new FetchFavoriteMovies();
            fetchFavoriteMovies.execute();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_popular_sort) {
            updateData(PopularMoveisURL);
            FavoriteSort = "Popular";
        }
        else if(id == R.id.action_top_sort) {
            updateData(TopRatedMoviesURL);
            FavoriteSort = "Top Rated";
        }
        else if(id == R.id.action_favorites){
            FavoriteSort = "Favorite";
            updateData(FavoriteSort);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieModel movieModel = (MovieModel) parent.getItemAtPosition(position);
        ((Callback) getActivity()).onItemSelected(movieModel);
    }

    void updateData(String DataURL){
        if(DataURL.equals("Favorite")){
            FetchFavoriteMovies fetchFavoriteMovies = new FetchFavoriteMovies();
            fetchFavoriteMovies.execute();
        }else {
            FetchMoviesData fetchMoviesData = new FetchMoviesData();
            fetchMoviesData.execute(DataURL);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    //************************************************************************************************************************//
    public class FetchMoviesData extends AsyncTask<String , Void , List<MovieModel>> {

        private String moviesJSONData;

        @Override
        protected List<MovieModel> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            Uri uri = Uri.parse(params[0]).buildUpon().build();
            URL url = null;

            try {
                url = new URL(uri.toString());
            }catch (MalformedURLException e){
                e.printStackTrace();
            }

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null){return null;}

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null){
                    buffer.append(line +  "\n");
                }

                if(buffer.length() == 0){return null;}

                moviesJSONData = buffer.toString();

            }catch (IOException ex){
                //ex.printStackTrace();
                Log.e("Error FetchMovieData",""+ex);
                return null;
            }finally {
                //Check and Disconnect the Internet Connection
                if(urlConnection != null){urlConnection.disconnect();}
                if(reader != null){
                    try {
                        reader.close();
                    }catch (final IOException exp){}
                }
            }

            return getMoviesDataFromJSON(moviesJSONData);
        }

        //Create List Of Movies Form JSON Data
        private List<MovieModel> getMoviesDataFromJSON(String moviesJSONData){
            List<MovieModel> movies = new ArrayList<>();
            if(moviesJSONData != null){
                try {
                    JSONObject jsonObject = new JSONObject(moviesJSONData);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject movieJSON = (JSONObject) jsonArray.get(i);
                        if(movieJSON.getString("poster_path") != null){
                            movies.add(new MovieModel( movieJSON.getString("id") , movieJSON.getString("original_title") , movieJSON.getString("release_date") , movieJSON.getString("vote_average") , movieJSON.getString("overview") , movieJSON.getString("poster_path")));
                        }
                    }
                }catch (JSONException JsonEx){}
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            adapter = new MoviesAdapter(getActivity() , movieModels);

            if(movieModels != null){
                if(adapter.isEmpty()){
                    gridView.setAdapter(adapter);
                }else {
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    //************************************************************************************************************************//

    public class FetchFavoriteMovies extends AsyncTask<Void, Void, List<MovieModel>> {
        @Override
        protected List<MovieModel> doInBackground(Void... params) {
            Cursor cursor = getActivity().getContentResolver().query(MoviesTableTable.CONTENT_URI,null,null,null,null);
            List<MovieModel> MoviesRows = MoviesTableTable.getRows(cursor,false);
            cursor.close();
            return MoviesRows;
        }

        @Override
        protected void onPostExecute(List<MovieModel> MoviesRows) {
            super.onPostExecute(MoviesRows);
            adapter = new MoviesAdapter(getActivity() , MoviesRows);

            if(MoviesRows != null){
                if(adapter.isEmpty()){
                    gridView.setAdapter(adapter);
                }else {
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
    //************************************************************************************************************************//

    public interface Callback{
        public void onItemSelected(MovieModel movieModel);
    }
}


