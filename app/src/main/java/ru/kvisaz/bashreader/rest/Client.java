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

    public static String getPage(int pageId) throws IOException {
        String code="";
        if(pageId<=0)
            code = grab(RetrofitFactory.getApiService().loadLastPage());
        else
            code = grab(RetrofitFactory.getApiService().loadPage(pageId));
        return code;
    }

    public static String getLastPage() throws IOException {
        return getPage(0);
    }

    public static String getRandom() throws IOException{
        return grab(RetrofitFactory.getApiService().loadRandom());
    }

    public static String getBest() throws IOException{
        return grab(RetrofitFactory.getApiService().loadBest());
    }

    public static String getByRating() throws IOException{
        return grab(RetrofitFactory.getApiService().loadByRatingLast());
    }

    public static String getByRating(int pageNumber) throws IOException{
        return grab(RetrofitFactory.getApiService().loadByRatingLast(pageNumber));
    }

    public static String getAbyss() throws IOException{
        return grab(RetrofitFactory.getApiService().loadAbyss());
    }

    public static String getAbyssTop() throws IOException{
        return grab(RetrofitFactory.getApiService().loadAbyssTop());
    }

    public static String getAbyssBestLast() throws IOException{
        return grab(RetrofitFactory.getApiService().loadAbyssBestLast());
    }

    public static String getAbyssBestByDate(String bashDate) throws IOException {
        return grab(RetrofitFactory.getApiService().loadAbyssByDate(bashDate));
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
