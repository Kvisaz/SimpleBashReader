package ru.kvisaz.bashreader.rest;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import ru.kvisaz.bashreader.model.Constants;

public class RetrofitFactory {

    private static RestAPI restApi;


    @NonNull
    public static RestAPI getApiService(){
        if(restApi==null){
            restApi = getRetrofit().create(RestAPI.class);
        }
        return restApi;
    }

    @NonNull
    public static Retrofit getRetrofit() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .build();

        return retrofit;
    }
}
