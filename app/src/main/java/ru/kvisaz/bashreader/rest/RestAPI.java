package ru.kvisaz.bashreader.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestAPI {
    @GET("index/{pageid}")
    Call<ResponseBody> loadPage(@Path("pageid") String pageid);

    @GET("random")
    Call<ResponseBody> loadRandom();

    @GET("best")
    Call<ResponseBody> loadBest();

    @GET("byrating/{pagenumber}")
    Call<ResponseBody> loadByRating(@Path("pagenumber") String pageNumber);

    @GET("abyss")
    Call<ResponseBody> loadAbyss();

    @GET("abysstop")
    Call<ResponseBody> loadAbyssTop();

    @GET("abyssbest/{date}")
    Call<ResponseBody> loadAbyssByDate(@Path("date") String date);

    @GET("comics/{datecode}")
    Call<ResponseBody> loadComics(@Path("datecode") String datecode);

    /*
    @GET("rss/")
    Call<ResponseBody> loadRSS();
    */


}
