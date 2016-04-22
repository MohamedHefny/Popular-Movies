package com.example.mohamedhefny.popularmovie_new_tray;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private boolean mTwoPane;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if(findViewById(R.id.detail_container) != null){
            mTwoPane = true;
            if(savedInstanceState == null){
               // getSupportFragmentManager().beginTransaction().replace(R.id.detail_container, new DetailsActivityFragment()).commit();
            }
        }else {
            mTwoPane = false;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onItemSelected(MovieModel movieModel) {
        if(mTwoPane){
            DetailsActivityFragment detailsActivityFragment = DetailsActivityFragment.newInstance(movieModel);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_container,detailsActivityFragment).commit();
        }else {
            Intent i = new Intent(MainActivity.this, DetailsActivity.class);
            i.putExtra("movieModel", movieModel);
            startActivity(i);
        }
    }
}
