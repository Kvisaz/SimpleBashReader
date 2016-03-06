package ru.kvisaz.bashreader.model;

import java.util.ArrayList;
import java.util.HashMap;

public class BashPage {
    ArrayList<BashQuote> quotes;
    int id;
    public BashPageType type;

    public String firstPageCode;
    public String lastPageCode;

    public String currentPage; // number or datecode of current page
    public String prevPage; // number or datecode of previous page (NEWER)
    public String nextPage; // number or datecode of next page   (OLDER)

    public String errorMessage;
    public boolean isError;

    public HashMap<Integer,BashPageType> topicMap;

    public BashPage(){
        quotes = new ArrayList<>();
        currentPage = "";
        prevPage = "";
        nextPage = "";
        isError = false;
    }

    public ArrayList<BashQuote> getQuotes(){ return quotes; }

    public void add(BashQuote quote){ quotes.add(quote); }
    public void clear(){ quotes.clear();}

    public void setError(String message){
        isError = true;
        errorMessage = message;
    }
}
