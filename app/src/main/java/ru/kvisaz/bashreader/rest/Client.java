package ru.kvisaz.bashreader.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.kvisaz.bashreader.custom.Constants;

/**
 *   Client functions
 *   get server response in same thread
 */
public class Client {

    public static String getPage(int pageId) throws IOException {
        restAPI service = ApiFactory.getApiService();
        Call<ResponseBody> call;
        if(pageId<=0)
            call = service.loadLastPage();
        else
            call = service.loadPage(pageId);

        // прямое исполнение, не асинхронное -
        ResponseBody responseBody = call.execute().body();

        String code = getAsString(responseBody);

        return code;
    }


    private static String getAsString(ResponseBody responseBody) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(responseBody.byteStream(), Constants.charset));
        StringBuilder sb = new StringBuilder("");
        while(br.ready())
        {
            sb.append(br.readLine());
        }
        br.close();

        return sb.toString();
    }


}
