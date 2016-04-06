package com.example.mohamedhefny.popularmovie_new_tray;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.mohamedhefny.popularmovie_new_tray.Model.MovieModel;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.container , new MainActivityFragment()).commit();
        }
    }


    @Override
    public void onItemSelected(MovieModel movieModel) {

    }
}
