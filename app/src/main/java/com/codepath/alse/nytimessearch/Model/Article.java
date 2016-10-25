package com.codepath.alse.nytimessearch.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;

//Model for Article
@Parcel
public class Article {

    String web_url;
    String thumbnail;
    String headline;

    public String getOverview() {
        return overview;
    }

    String overview;

    public Article(){

    }

    public String getWeb_url() {
        return web_url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public String getHeadline() {
        return headline;
    }



    public Article(JSONObject jsonObject){
        try {
            this.web_url = jsonObject.getString("web_url");
            JSONArray multimediaArray = jsonObject.getJSONArray("multimedia");
            if(multimediaArray.length()>0){
                this.thumbnail = "http://www.nytimes.com/"+multimediaArray.getJSONObject(0).getString("url");
            }
            this.headline = jsonObject.getJSONObject("headline").getString("main");
            if(jsonObject.getString("lead_paragraph")==null){
                overview = jsonObject.getString("snippet");
            }
            else{
                overview = jsonObject.getString("lead_paragraph");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Factory method to process JSONArray
    public static ArrayList<Article> processJSONArray(JSONArray resultArray){
        ArrayList<Article> result = new ArrayList<Article>();
        for(int i = 0;i<resultArray.length();i++){
            try {
                result.add(new Article(resultArray.getJSONObject(i)));
            } catch (JSONException e) {
                Log.d("Error processJSONArray",e.toString());

            }
        }
        return result;
    }
}
