package ru.kvisaz.bashreader.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("/")
    Call<ResponseBody> loadLastPage();

    @GET("index/{pageid}")
    Call<ResponseBody> loadPage(@Path("pageid") int pageId);

    @GET("random")
    Call<ResponseBody> loadRandom();

    @GET("best")
    Call<ResponseBody> loadBest();

    @GET("byrating")
    Call<ResponseBody> loadByRatingLast();

    @GET("byrating/{pagenumber}")
    Call<ResponseBody> loadByRatingLast(@Path("pagenumber") int pageNumber);

    @GET("abyss")
    Call<ResponseBody> loadAbyss();

    @GET("abysstop")
    Call<ResponseBody> loadAbyssTop();

    @GET("abyssbest/{date}")
    Call<ResponseBody> loadAbyssByDate(@Path("date") String date);

    @GET("abyssbest")
    Call<ResponseBody> loadAbyssBestLast();

   /* @GET("comics")
    Call<ResponseBody> loadLastComics();

    @GET("comics/{id}")
    Call<ResponseBody> loadComics(@Path("id") int comicsId);

    @GET("rss/")
    Call<ResponseBody> loadRSS();*/


}
