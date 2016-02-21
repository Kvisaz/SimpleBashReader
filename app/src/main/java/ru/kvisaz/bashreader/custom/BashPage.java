package ru.kvisaz.bashreader.custom;

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
}
