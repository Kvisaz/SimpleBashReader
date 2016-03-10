package ru.kvisaz.bashreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import ru.kvisaz.bashreader.Constants;

public abstract class LoaderBasic extends AsyncTaskLoader<String> {

    protected abstract String clientResponse() throws IOException;

    public LoaderBasic(Context context) {
        super(context);
    }

    // http://stackoverflow.com/questions/8606048/asynctaskloader-doesnt-run
    // http://stackoverflow.com/questions/10524667/android-asynctaskloader-doesnt-start-loadinbackground
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try{
            return clientResponse();
        }
        catch (IOException e)
        {
            Log.d(Constants.LOGTAG,"LoaderBasic ApiCall Exception");
            return  null;
        }
    }
}
