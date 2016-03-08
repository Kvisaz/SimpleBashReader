package ru.kvisaz.bashreader.adapter;


import java.util.ArrayList;
import java.util.Map;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashPageType;
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
            String idMark = "#";
            if(bashPage.type== BashPageType.AbyssBest) idMark = "#AA-";
            head = quote.date + formatRating(quote.rating) + formatId(quote.id, idMark);
            text = quote.text;
            listOfMap.add(AdapterMapping.getMap(head, text));
        }

        return listOfMap;
    }

    private static String formatId(int id, String idMark) {
    if(id==0) return "";
        return "          "+idMark+id;
    }

    private static String formatRating(int rating) {
        if(rating==0) return "";
        return "          +"+rating;
    }
}
