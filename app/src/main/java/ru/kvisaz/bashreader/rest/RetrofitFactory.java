package ru.kvisaz.bashreader.rest;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import ru.kvisaz.bashreader.model.Constants;

public class RetrofitFactory {

    private static RestApi restApi;


    @NonNull
    public static RestApi getApiService(){
        if(restApi==null){
            restApi = getRetrofit().create(RestApi.class);
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
