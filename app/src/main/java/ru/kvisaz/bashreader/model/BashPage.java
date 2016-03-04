package ru.kvisaz.bashreader.model;

import java.util.ArrayList;
import java.util.HashMap;

public class BashPage {
    ArrayList<BashQuote> quotes;
    int id;
    int firstIndex;
    int lastIndex;
    BashPageType type;

    public String currentPage; // number or datecode of current page
    public String prevPage; // number or datecode of previous page
    public String nextPage; // number or datecode of next page

    public HashMap<Integer,BashPageType> topicMap;

    public BashPage(){
        quotes = new ArrayList<>();
    }

    public ArrayList<BashQuote> getQuotes(){ return quotes; }

    public void add(BashQuote quote){ quotes.add(quote); }
    public void clear(){ quotes.clear();}
}
