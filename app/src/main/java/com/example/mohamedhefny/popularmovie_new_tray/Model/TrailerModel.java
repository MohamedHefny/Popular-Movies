package com.example.mohamedhefny.popularmovie_new_tray.Model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohamed Hefny on 19/04/2016.
 */
public class TrailerModel {
    private String id;
    private String name;
    private String key;
    private String Site;
    private String type;

    public TrailerModel() {

    }

    public TrailerModel(JSONObject TjsonObject) throws JSONException {
        this.id = TjsonObject.getString("id");
        this.key = TjsonObject.getString("key");
        this.name = TjsonObject.getString("name");
        this.Site = TjsonObject.getString("site");
        this.type = TjsonObject.getString("type");
    }

    //Getter For Variables *************************
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return Site;
    }

    public String getType() {
        return type;
    }
    //******************************************************************
}
