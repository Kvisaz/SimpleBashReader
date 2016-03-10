package ru.kvisaz.bashreader.db;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import ru.kvisaz.bashreader.Constants;
import ru.kvisaz.bashreader.model.BashPage;

public abstract class LoaderDataBaseBasic extends AsyncTaskLoader<BashPage> {
    protected abstract BashPage getDatabaseWork();

    public LoaderDataBaseBasic(Context context) {
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
    public BashPage loadInBackground() {
        try{
            return getDatabaseWork();
        }
        catch (Exception e){
            Log.d(Constants.LOGTAG, "Loader DataBase Exception");
            e.printStackTrace();
            return null;
        }
    }

}
