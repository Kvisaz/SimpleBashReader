package ru.kvisaz.bashreader.rest;

import android.support.annotation.NonNull;

import retrofit2.Retrofit;
import ru.kvisaz.bashreader.custom.Constants;

public class RetrofitFactory {

    @NonNull
    public static restAPI getApiService(){
        return getRetrofit().create(restAPI.class);
    }

    @NonNull
    public static Retrofit getRetrofit() {

        @SuppressWarnings("retrofit")
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BaseUrl)
                .build();

        return retrofit;
    }
}
