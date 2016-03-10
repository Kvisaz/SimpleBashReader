package ru.kvisaz.bashreader.loader;

import android.content.Context;

import java.io.IOException;

import ru.kvisaz.bashreader.model.BashPageType;
import ru.kvisaz.bashreader.rest.Client;

public class LoaderBash extends LoaderBasic {
    private String pageCode; // id or date
    BashPageType pageType;


    public LoaderBash(Context context, BashPageType pageType, String pageCode){
        super(context);
        this.pageCode = pageCode;
        this.pageType = pageType;
    }

    @Override
    protected String clientResponse() throws IOException {
        switch(pageType){
            case LastPage:
                return Client.getPage("");
            case Page:
                return Client.getPage(pageCode);
            case Random:
                return Client.getRandom();
            case ByRating:
                return Client.getByRating(pageCode);
            case Best:
                return Client.getBest();
            case Abyss:
                return Client.getAbyss();
            case AbyssTop:
                return Client.getAbyssTop();
            case AbyssBest:
                return Client.getAbyssBest(pageCode);
            case Comics:
                return Client.getComics(pageCode);
            default:
                return Client.getPage("");
        }

    }

}
