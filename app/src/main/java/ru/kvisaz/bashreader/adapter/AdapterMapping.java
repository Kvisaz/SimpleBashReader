package ru.kvisaz.bashreader.adapter;

import java.util.HashMap;

import ru.kvisaz.bashreader.R;

public class AdapterMapping {
    public static final String header = "date";
    public static final int headerId = R.id.quoteDate;

    public static final String base = "baseText";
    public static final int baseId = R.id.quoteText;

    public final static String[] from = {header, base};
    public final static int[] to = {headerId, baseId};

    public static HashMap<String,Object> getMap(String headerText, Object baseText){
        HashMap<String,Object> tempMap = new HashMap<>();
        tempMap.put(header, headerText);
        tempMap.put(base, baseText);
        return tempMap;
    }


}
