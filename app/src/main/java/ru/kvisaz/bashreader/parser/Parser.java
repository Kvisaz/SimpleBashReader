package ru.kvisaz.bashreader.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import ru.kvisaz.bashreader.model.BashPage;

public class Parser {

    private final static String RAW_QUOTE_QUERY = "div.quote";
    private final static String RATING_QUERY = "span.rating";
    private final static String DATE_QUERY = "span.date";
    private final static String ID_QUERY = "a.id";

    private static String cachedHtml;
    private static BashPage cachedPage;

    public static BashPage convert(String rawHtml)
    {
        if(cachedHtml!=null && cachedHtml.equals(rawHtml) && cachedPage!=null)
        {
            return cachedPage;
        }

        BashPage bashPage = new BashPage();

        Document doc = Jsoup.parse(rawHtml);
        Elements rawQuotes = doc.select(RAW_QUOTE_QUERY);



        return bashPage;
    }

}
