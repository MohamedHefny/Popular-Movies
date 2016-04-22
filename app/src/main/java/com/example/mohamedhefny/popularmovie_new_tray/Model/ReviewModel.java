package com.example.mohamedhefny.popularmovie_new_tray.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohamed Hefny on 19/04/2016.
 */
public class ReviewModel {

    private String id;
    private String author;
    private String content;

    public ReviewModel() {

    }

    public ReviewModel(JSONObject RjsonObject) throws JSONException {
        this.id = RjsonObject.getString("id");
        this.author = RjsonObject.getString("author");
        this.content = RjsonObject.getString("content");
    }

    //Getter For Variables *************************
    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }
    //******************************************************************
}
