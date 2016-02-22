package ru.kvisaz.bashreader.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Call;
import ru.kvisaz.bashreader.model.Constants;

/**
 *   Client functions
 *   get server response in same thread - use loader or any other async task
 */
public class Client {

    public static String getPage(int pageId) throws IOException {
        restAPI service = RetrofitFactory.getApiService();
        Call<ResponseBody> call;
        if(pageId<=0)
            call = service.loadLastPage();
        else
            call = service.loadPage(pageId);

        ResponseBody responseBody = call.execute().body();
        @SuppressWarnings("code")
        String code = getAsString(responseBody);

        return code;
    }

    public static String getLastPage() throws IOException{
        return getPage(0);
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
