package com.codepath.alse.nytimessearch.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.codepath.alse.nytimessearch.BuildConfig;
import com.codepath.alse.nytimessearch.Model.ArticleResponse;
import com.codepath.alse.nytimessearch.Model.Doc;

import java.util.List;

import retrofit2.Retrofit;

//Utility class for Networking Calls
public class NetworkingCalls {
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    //Call using retrofit
    public void networkCalls(String query, final int page) {
        String apiKey = BuildConfig.NYTIMES_API_KEY;

        Retrofit retrofit = ApiClient.getClient();
        ApiInterface apiService = retrofit.create(ApiInterface.class);
        retrofit2.Call<ArticleResponse> call = apiService.getArticleList(page, apiKey, query);
        call.enqueue(new retrofit2.Callback<ArticleResponse>() {
            @Override
            public void onResponse(retrofit2.Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                ArticleResponse articleResponse = response.body();
                List<Doc> articles = response.body().getDocs();
                for (int i = 0; i < articles.size(); i++) {
                    Log.v("articles", articles.get(0).getWebUrl());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<ArticleResponse> call, Throwable t) {

            }
        });
    }

}
