package ru.kvisaz.bashreader.adapter;


import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashQuote;

public class AdapterDataFactory {

    public static ArrayList<Map<String,Object>> getData(BashPage bashPage){
        ArrayList<Map<String,Object>> listOfMap;
        String head;
        String text;

        String date;
        String rating;
        String id;


        listOfMap = new ArrayList<>();
        for(BashQuote quote: bashPage.getQuotes()){

            head = quote.date + formatRating(quote.rating) + formatId(quote.id);
            text = quote.text;
            listOfMap.add(AdapterMapping.getMap(head, text));
        }

        return listOfMap;
    }

    private static String formatId(int id) {
    if(id==0) return "";
        return "          #"+id;
    }

    private static String formatRating(int rating) {
        if(rating==0) return "";
        return "          +"+rating;
    }
}
