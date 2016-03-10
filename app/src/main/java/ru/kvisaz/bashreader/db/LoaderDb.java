package ru.kvisaz.bashreader.db;

import android.content.Context;

import java.util.Objects;

import ru.kvisaz.bashreader.model.BashPage;

public class LoaderDb extends LoaderDataBaseBasic {
    /*
    *      add database query
    *      1. add W_constant for query type
    *      2. add switch case for query type in getDatabaseWork
     *
     *
    * */
    public static final int W_SAVE_PAGE = 0;
    public static final int W_SAVE_QUOTE = 20;

    private int workType;
    private Object data;

    public LoaderDb(Context context, int workType, Object data) {
        super(context);
        this.workType = workType;
        this.data = data;
    }

    @Override
    protected BashPage getDatabaseWork() {
        switch(workType){
            case W_SAVE_PAGE:
                return savePage();
            case W_SAVE_QUOTE:
                return saveQuote();

        }

        return null;
    }

    // Save all quotes from page to base ............................................
    private BashPage savePage() {
        return null;
    }

    // Save 1 quote to base ............................................
    private BashPage saveQuote() {
        return null;
    }
}
