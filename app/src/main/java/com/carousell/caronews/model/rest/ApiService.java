package com.carousell.caronews.model.rest;

import com.carousell.caronews.model.pojo.News;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface ApiService {

    @GET("carousell-interview-assets/android/carousell_news.json")
    Single<List<News>> getNewsDetails();
}
