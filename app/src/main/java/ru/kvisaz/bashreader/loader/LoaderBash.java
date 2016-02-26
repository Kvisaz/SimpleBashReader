package ru.kvisaz.bashreader.loader;

import android.content.Context;

import java.io.IOException;

import ru.kvisaz.bashreader.model.BashPageType;
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
    protected String clientResponse() throws IOException {
        switch(pageType){
            case LastPage:
                return Client.getPage(pageId);
            case Page:
                return Client.getPage(pageId);
            case Random:
                return Client.getRandom();
            case ByRating:
                return Client.getByRating();
            case Best:
                return Client.getBest();
            case Abyss:
                return Client.getAbyss();
            case AbyssTop:
                return Client.getAbyssTop();
            case AbyssBest:
                return Client.getAbyssBestLast();
            default:
                return Client.getLastPage();
        }

    }

}
