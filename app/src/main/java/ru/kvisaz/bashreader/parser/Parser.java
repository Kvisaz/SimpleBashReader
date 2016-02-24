package ru.kvisaz.bashreader.parser;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashQuote;
import ru.kvisaz.bashreader.model.Constants;

/*
*       Parser rawHTML->BashPage
*
*       Защита
*       1. в блоке div.quote - баннер => NPE, возвращаем null цитату и не добавляем
*       2.
*        //  did - 1. обработка свежих цитат со скрытым рейтингом (???).
 *                      - решено через дополнительный try-catch
 *

* */
public class Parser {

    private final static String RAW_QUOTE_QUERY = "div.quote";
    /*
    *      bash.im в этом блоке выдает цитаты и рекламные баннеры, от которых делаем защиту
    * */

    private final static String RATING_QUERY = "span.rating";
    private final static String DATE_QUERY = "span.date";
    private final static String ID_QUERY = "a.id";
    private final static String TEXT_QUERY = "div.text";

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

        for(Element rawQuote: rawQuotes){
            BashQuote quote = parseQuote(rawQuote);
            if(quote!=null)
                bashPage.add(quote);
        }

        cachedPage = bashPage;
        cachedHtml = rawHtml;

        return bashPage;
    }

    private static BashQuote parseQuote(Element rawQuote) {
        @SuppressWarnings({"id","rating","text","date"})
        int id, rating;
        String text, date;

        try {
            Elements idList = rawQuote.select(ID_QUERY);
            Elements textList = rawQuote.select(TEXT_QUERY);
            Elements dateList = rawQuote.select(DATE_QUERY);
            Elements ratingList = rawQuote.select(RATING_QUERY);

            // ошибка парсинга - какого-то элемента не хватает
            if (idList.size()<1 || textList.size()<1 || dateList.size()<1 || ratingList.size()<1 ) return null;

            String idStr = idList.first().attr("href");
            idStr = idStr.replace("/quote/", "");
            id = Integer.parseInt(idStr);

            text = textList.first().html();
            text = Html.fromHtml(text).toString(); // clean br tags


            date = dateList.first().text();

            // у цитат возможен рейтинг, обозначенный не цифрами (скрытый)
            String ratingStr = ratingList.first().text();
            try {
                rating = Integer.parseInt(ratingStr);
            }
            catch (Exception e){
                rating = 0;
            }


        }
        catch (Exception e)
        {
            Log.d(Constants.LOGTAG, e.toString());
            return null;
        }

        return new BashQuote(id,text,date,rating);
    }

}
