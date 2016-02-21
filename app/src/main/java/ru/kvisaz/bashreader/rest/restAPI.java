package ru.kvisaz.bashreader.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface restAPI {
    @GET("/")
    Call<ResponseBody> loadLastPage();

    @GET("index/{pageid}")
    Call<ResponseBody> loadPage(@Path("pageid") int pageId);

    @GET("abyssbest")
    Call<ResponseBody> loadAbyss();

    @GET("abyssbest/{date}")
    Call<ResponseBody> loadAbyss(@Path("date") String date);

    @GET("comics")
    Call<ResponseBody> loadLastComics();

    @GET("comics/{id}")
    Call<ResponseBody> loadComics(@Path("id") int comicsId);

    @GET("rss/")
    Call<ResponseBody> loadRSS();

    @GET("best")
    Call<ResponseBody> loadBest();


}
