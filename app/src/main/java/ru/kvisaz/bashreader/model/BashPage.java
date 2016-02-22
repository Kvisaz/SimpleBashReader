package ru.kvisaz.bashreader.model;

import java.util.ArrayList;

public class BashPage {
    ArrayList<BashQuote> quotes;
    int id;
    int firstIndex;
    int lastIndex;
    BashPageType type;

    public BashPage(){
        quotes = new ArrayList<>();
    }

    public ArrayList<BashQuote> getQuotes(){ return quotes; }
}