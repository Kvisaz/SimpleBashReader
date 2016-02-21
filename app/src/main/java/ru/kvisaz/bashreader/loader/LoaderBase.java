package ru.kvisaz.bashreader.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.io.IOException;

import ru.kvisaz.bashreader.custom.Constants;

public abstract class LoaderBase extends AsyncTaskLoader<String> {

    protected abstract String clientCall() throws IOException;

    public LoaderBase(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public String loadInBackground() {
        try{
            return clientCall();
        }
        catch (IOException e)
        {
            Log.d(Constants.LOGTAG,"LoaderBase ApiCall Exception");
            return  null;
        }
    }
}
