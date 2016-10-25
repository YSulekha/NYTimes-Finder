package com.codepath.alse.nytimessearch.utils;

import com.codepath.alse.nytimessearch.Model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @GET("{page}")

    Call<ArticleResponse> getArticleList(@Path("page")int page, @Query("api_key")String apiKey, @Query("q")String query);
}
