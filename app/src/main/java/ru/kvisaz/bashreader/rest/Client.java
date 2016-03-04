package ru.kvisaz.bashreader.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


/**
 *   Client functions
 *   get server response in same thread - use loader or any other async task
 *
 */
public class Client {

    public static final String charset = "windows-1251";

    public static String getPage(String pageCode) throws IOException
    {
      return grab(RetrofitFactory.getApiService().loadPage(pageCode));
    }

    public static String getRandom() throws IOException{
        return grab(RetrofitFactory.getApiService().loadRandom());
    }

    public static String getBest() throws IOException{
        return grab(RetrofitFactory.getApiService().loadBest());
    }

    public static String getByRating(String pageCode) throws IOException{
        return grab(RetrofitFactory.getApiService().loadByRating(pageCode));
    }

    public static String getAbyss() throws IOException{
        return grab(RetrofitFactory.getApiService().loadAbyss());
    }

    public static String getAbyssTop() throws IOException{
        return grab(RetrofitFactory.getApiService().loadAbyssTop());
    }

    public static String getAbyssBest(String bashDate) throws IOException {
        return grab(RetrofitFactory.getApiService().loadAbyssByDate(bashDate));
    }

    public static String getComics(String comicsDate) throws IOException{
        return grab(RetrofitFactory.getApiService().loadComics(comicsDate));
    }

    //  Client common functions
    private static String grab(Call<ResponseBody> call) throws IOException {
        Response response = call.execute();

        if(!response.isSuccess()){
            return null;
        }

        return getAsString((ResponseBody) response.body());
    }

    private static String getAsString(ResponseBody responseBody) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(responseBody.byteStream(), charset));
        StringBuilder sb = new StringBuilder("");
        while(br.ready())
        {
            sb.append(br.readLine());
        }
        br.close();

        return sb.toString();
    }


}
