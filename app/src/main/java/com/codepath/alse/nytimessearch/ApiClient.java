package com.codepath.alse.nytimessearch;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aharyadi on 10/24/16.
 */

public class ApiClient {
    public static final String base_url = "http://api.nytimes.com/svc/search/v2/articlesearch.json/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient(){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}
