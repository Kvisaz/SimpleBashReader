package ru.kvisaz.bashreader.image;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import ru.kvisaz.bashreader.Constants;

/**
 * callback for Picasso image loader in MainActivity
 */
public class ImageLoadedCallback implements Callback {
    ProgressBar progressBar;

    public  ImageLoadedCallback(ProgressBar progBar){
        progressBar = progBar;
    }



    @Override
    public void onSuccess() {
        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError() {
        Log.d(Constants.LOGTAG,"Picasso image load error");

        if (this.progressBar != null) {
            this.progressBar.setVisibility(View.GONE);
        }

    }
}
