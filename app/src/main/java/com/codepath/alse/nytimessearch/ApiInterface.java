package com.codepath.alse.nytimessearch;

import com.codepath.alse.nytimessearch.Model.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by aharyadi on 10/24/16.
 */

public interface ApiInterface {

    @GET("{page}")

    Call<ArticleResponse> getArticleList(@Path("page")int page, @Query("api_key")String apiKey, @Query("q")String query);
}
