package ru.kvisaz.bashreader.adapter;


import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashQuote;

public class AdapterDataFactory {

    public static ArrayList<Map<String,Object>> getData(BashPage bashPage){
        ArrayList<Map<String,Object>> data;
        String head;
        String text;

        data = new ArrayList<>();
        for(BashQuote quote: bashPage.getQuotes()){
            head = quote.date;
            text = quote.text;
            data.add(AdapterMapping.getMap(head, text));
        }

        return data;
    }
}
