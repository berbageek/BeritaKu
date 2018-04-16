package com.berbageek.beritaku.api;

import com.berbageek.beritaku.api.model.response.TopHeadlinesResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApiSource {
    @GET("/v2/top-headlines?country=us&category=technology")
    Call<TopHeadlinesResponse> getTopHeadlines();
}
