package com.example.mohamedhefny.popularmovie_new_tray;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;
import com.example.mohamedhefny.popularmovie_new_tray.Adapters.MoviesAdapter;

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
    static final String API_Value = "3b7f55cfc517563495641cc3d5ed4285";

    //Final MovieModel URL
    String PopularMoveisURL ;
    String TopRatedMoviesURL ;

    GridView gridView;
    String sortMode;

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
        Uri popularMoviesURL = Uri.parse(movies_URL).buildUpon().appendPath(sort_Popular).appendQueryParameter(API_Kay,API_Value).build();
        PopularMoveisURL = popularMoviesURL.toString();

        //Top_Rated MovieModel URL Build
        Uri topRatedMoviesURL = Uri.parse(movies_URL).buildUpon().appendPath(sort_Top_Rated).appendQueryParameter(API_Kay,API_Value).build();
        TopRatedMoviesURL = topRatedMoviesURL.toString();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.app_main_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_popular_sort) {
            updateData(PopularMoveisURL);
        }
        else if(id == R.id.action_top_sort) {
            updateData(TopRatedMoviesURL);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData(PopularMoveisURL);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MovieModel movie = (MovieModel) parent.getItemAtPosition(position);
        ((Callback) getActivity()).onItemSelected(movie);
    }

    void updateData(String DataURL){
        FetchMoviesData updateMovies = new FetchMoviesData();
        updateMovies.execute(DataURL);

    }

    //************************************************************************************************************************//
    public class FetchMoviesData extends AsyncTask<String , Void , List<MovieModel>> {

        private final String LOG_TAG = FetchMoviesData.class.getSimpleName();

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
                ex.printStackTrace();
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
                            movies.add(new MovieModel( movieJSON.getString("id") , movieJSON.getString("original_title") , movieJSON.getString("release_date") , movieJSON.getString("vote_average") , movieJSON.getString("overview") , movieJSON.getString("poster_path") , 0 ));
                        }
                    }
                }catch (JSONException JsonEx){}
            }
            return movies;
        }

        @Override
        protected void onPostExecute(List<MovieModel> movieModels) {
            super.onPostExecute(movieModels);
            MoviesAdapter adapter = new MoviesAdapter(getActivity() , movieModels);

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

    public interface Callback{
        public void onItemSelected(MovieModel movieModel);
    }
}


