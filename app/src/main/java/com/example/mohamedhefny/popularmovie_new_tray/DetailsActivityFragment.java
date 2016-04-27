package com.example.mohamedhefny.popularmovie_new_tray;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.telecom.Connection;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mohamedhefny.popularmovie_new_tray.Adapters.MoviesAdapter;
import com.example.mohamedhefny.popularmovie_new_tray.Adapters.ReviewAdapter;
import com.example.mohamedhefny.popularmovie_new_tray.Adapters.TrailerAdapter;
import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;
import com.example.mohamedhefny.popularmovie_new_tray.Model.ReviewModel;
import com.example.mohamedhefny.popularmovie_new_tray.Model.TrailerModel;
import com.linearlistview.LinearListView;
import com.squareup.picasso.Picasso;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import com.example.mohamedhefny.popularmovie_new_tray.Model.*;

public class DetailsActivityFragment extends Fragment implements View.OnClickListener {

    private MovieModel movieModel;
    private TrailerModel trailerModel;
    //private ReviewModel reviewModel;

    private Toolbar toolbar;

    private String posterPath;
    private ImageView posterImage;

    private TextView posterTitle;
    private TextView release_date;
    private TextView average_vote;
    private TextView overview;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private LinearListView trailerListView;
    private LinearListView reviewListView;

    private CardView reviewsCardview;
    private CardView trailersCardview;

    private ShareActionProvider mShareActionProvider;

    private Button favoriteBtn;

    String mmMovieId = "";

    public DetailsActivityFragment() {}

    public static DetailsActivityFragment newInstance(MovieModel movieModel){
        DetailsActivityFragment detailsActivityFragment = new DetailsActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable("movieModel",movieModel);
        detailsActivityFragment.setArguments(args);
        return detailsActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movieModel = getArguments().getParcelable("movieModel");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_details, container, false);

        toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);

        posterTitle = (TextView) rootView.findViewById(R.id.posterTitle);
        release_date = (TextView) rootView.findViewById(R.id.release_date);
        average_vote = (TextView) rootView.findViewById(R.id.average_vote);
        overview = (TextView) rootView.findViewById(R.id.overview);

        posterImage = (ImageView) rootView.findViewById(R.id.posterImage);

        reviewListView = (LinearListView) rootView.findViewById(R.id.detail_reviews);
        trailerListView = (LinearListView) rootView.findViewById(R.id.detail_trailers);

        trailersCardview = (CardView) rootView.findViewById(R.id.detail_trailers_cardview);
        reviewsCardview = (CardView) rootView.findViewById(R.id.detail_reviews_cardview);

        trailerAdapter = new TrailerAdapter(getActivity(), new ArrayList<TrailerModel>());
        trailerListView.setAdapter(trailerAdapter);
        trailerListView.setOnItemClickListener(new LinearListView.OnItemClickListener() {
            @Override
            public void onItemClick(LinearListView linearListView, View view,
                                    int position, long id) {
                TrailerModel trailer = trailerAdapter.getItem(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
                startActivity(intent);
            }
        });

        reviewAdapter = new ReviewAdapter(getActivity(), new ArrayList<ReviewModel>());
        reviewListView.setAdapter(reviewAdapter);

        favoriteBtn = (Button) rootView.findViewById(R.id.favoriteBtn);
        favoriteBtn.setOnClickListener(this);
        movieInFavoriteCheck();
        if(mmMovieId != ""  /* the variable not change and This movie is not in the DataBase */) {
            favoriteBtn.setTextColor(Color.RED);
        }

        if(toolbar != null){
            //Do this In One Pane Mode
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        movieModel = getArguments().getParcelable("movieModel");
        posterPath = MoviesAdapter.posterURL + movieModel.getPosterPath();
        Picasso.with(getActivity()).load(posterPath).fit().into(posterImage);

        posterTitle.setText(movieModel.getTitle());
        release_date.setText("Released On : " + movieModel.getReleaseDate());
        average_vote.setText("Vote Avrage : " + movieModel.getVoteAverage() + " / 10");
        overview.setText(movieModel.getOverview());

        if (movieModel != null && isNetworkConnected()) {
            new FetchTrailersData().execute(movieModel.getMovieId());
            new FetchReviewsData().execute(movieModel.getMovieId());
        }
    }

    @Override
    public void onClick(View v) {
        movieInFavoriteCheck();

        if(mmMovieId == ""  /* the variable not change and This movie is not in the DataBase */) {
            getActivity().getContentResolver().insert(MoviesTableTable.CONTENT_URI,MoviesTableTable.getContentValues(movieModel,false));
            favoriteBtn.setTextColor(Color.RED);
            Toast.makeText(getContext(),"Movie Add to Favorite",Toast.LENGTH_SHORT).show();
        }else{
            //The variable is change and the movie is already in the DataBase
            Toast.makeText(getContext(), "This Movie is already in your Favorites", Toast.LENGTH_SHORT).show();
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movieModel.getTitle() + " " + "http://www.youtube.com/watch?v=" + trailerModel.getKey());
        return shareIntent;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    private void movieInFavoriteCheck(){
        Cursor cursor = getActivity().getContentResolver().query(MoviesTableTable.CONTENT_URI,null,null,null,null);
        List<MovieModel> MoviesRows = MoviesTableTable.getRows(cursor,false);

        //Check if the movieId in DataBase or not
        short i;
        for(i=0; i < MoviesRows.size(); i++){
            if (movieModel.getMovieId().equals(MoviesRows.get(i).getMovieId())){
                mmMovieId = movieModel.getMovieId();
                cursor.close();
                break;
            }
        }
    }

    //************************************************************************************************************************//
    public class FetchTrailersData extends AsyncTask<String, Void, List<TrailerModel>> {

        private String trailersJSONData;
        @Override
        protected List<TrailerModel> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/videos";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_KEY_PARAM, getString(R.string.myApiKeyValue))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null) {return null;}

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {return null;}

                trailersJSONData = buffer.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } finally {
                //Check and Disconnect the Internet Connection
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {}
                }
            }

            try {
                return getTrailersDataFromJSON(trailersJSONData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        //Create List Of Trailers Form JSON Data
        private List<TrailerModel> getTrailersDataFromJSON(String trailerJSONData) throws JSONException {

            List<TrailerModel> trailerModels = new ArrayList<>();

            if(trailerJSONData != null) {
                try {
                    JSONObject T_jsonObject = new JSONObject(trailerJSONData);
                    JSONArray T_jsonArray = T_jsonObject.getJSONArray("results");

                    for (int i = 0; i < T_jsonArray.length(); i++) {
                        JSONObject trailerJSON = T_jsonArray.getJSONObject(i);
                        if (trailerJSON.getString("site").contentEquals("YouTube")) {
                            TrailerModel trailerModel = new TrailerModel(trailerJSON);
                            trailerModels.add(trailerModel);
                        }
                    }
                } catch (JSONException JsonEx) {}
            }

            return trailerModels;
        }

        @Override
        protected void onPostExecute(List<TrailerModel> trailers) {
            if (trailers != null) {
                if (trailers.size() > 0) {
                    trailersCardview.setVisibility(View.VISIBLE);
                    if (trailerAdapter != null) {
                        trailerAdapter.clear();
                        for (TrailerModel trailer : trailers) {
                            trailerAdapter.add(trailer);
                        }
                    }

                    trailerModel = trailers.get(0);
                    if (mShareActionProvider != null) {
                        mShareActionProvider.setShareIntent(createShareMovieIntent());
                    }
                }
            }
        }
    }
    //************************************************************************************************************************//

    public class FetchReviewsData extends AsyncTask<String, Void, List<ReviewModel>> {

        private String reviewsJSONData;
        @Override
        protected List<ReviewModel> doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/movie/" + params[0] + "/reviews";
                final String API_KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(API_KEY_PARAM, getString(R.string.myApiKeyValue))
                        .build();

                URL url = new URL(builtUri.toString());

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {return null;}

                reviewsJSONData = buffer.toString();
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            } finally {
                //Check and Disconnect the Internet Connection
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {}
                }
            }

            try {
                return getReviewsDataFromJson(reviewsJSONData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private List<ReviewModel> getReviewsDataFromJson(String reviewJSONData) throws JSONException {
            JSONObject R_jsonObject = new JSONObject(reviewJSONData);
            JSONArray R_jsonArray = R_jsonObject.getJSONArray("results");

            List<ReviewModel> reviewModels = new ArrayList<>();

            for(int i = 0; i < R_jsonArray.length(); i++) {
                JSONObject reviewJSON = R_jsonArray.getJSONObject(i);
                reviewModels.add(new ReviewModel(reviewJSON));
            }

            return reviewModels;
        }

        @Override
        protected void onPostExecute(List<ReviewModel> reviewModels) {
            if (reviewModels != null) {
                if (reviewModels.size() > 0) {
                    reviewsCardview.setVisibility(View.VISIBLE);
                    if (reviewAdapter != null) {
                        reviewAdapter.clear();
                        for (ReviewModel review : reviewModels) {
                            reviewAdapter.add(review);
                        }
                    }
                }
            }
        }
    }
    //************************************************************************************************************************//
}
