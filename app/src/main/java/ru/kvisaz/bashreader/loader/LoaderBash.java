package ru.kvisaz.bashreader.loader;

import android.content.Context;

import java.io.IOException;

import ru.kvisaz.bashreader.custom.BashPageType;
import ru.kvisaz.bashreader.rest.Client;

public class LoaderBash extends LoaderBase {
    private int pageId;
    BashPageType pageType;

    public LoaderBash(Context context, int pageId, BashPageType pageType) {
        super(context);
        this.pageId = pageId;
        this.pageType = pageType;

    }

    @Override
    protected String clientCall() throws IOException {

        switch(pageType){
            case Index:
                return Client.getPage(pageId);
            default:
                return Client.getLastPage();
        }

    }

}
