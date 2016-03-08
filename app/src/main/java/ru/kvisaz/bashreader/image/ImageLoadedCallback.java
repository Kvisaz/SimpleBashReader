package ru.kvisaz.bashreader.image;
import android.view.View;
import android.widget.ProgressBar;

import com.squareup.picasso.Callback;
/**
 * Created by Builder on 06.03.2016.
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

    }
}
