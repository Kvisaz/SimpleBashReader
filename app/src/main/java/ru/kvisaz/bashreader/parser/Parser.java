package ru.kvisaz.bashreader.parser;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashPageType;
import ru.kvisaz.bashreader.model.BashQuote;
import ru.kvisaz.bashreader.Constants;

/*
*       Parser rawHTML->BashPage
*
*       Защита
*       1. в блоке div.quote - баннер => NPE, возвращаем null цитату и не добавляем
*       2.
*        //  did - 1. обработка свежих цитат со скрытым рейтингом (???).
*                      - решено через дополнительный try-catch *
*
* */
public class Parser {

    private final static String RAW_QUOTE_QUERY = "div.quote";
    private final static String RATING_QUERY = "span.rating";
    private final static String DATE_QUERY = "span.date";
    private final static String ID_QUERY = "a.id";
    private final static String TEXT_QUERY = "div.text";
    private final static String PAGER_QUERY = "div.pager";
    private static final String PAGER_COMICS_QUERY = "div.thumbs";


    private static String cachedHtml;
    private static BashPage bashPage;
    private static Document document;

    private static BashPageType pageType;
    private static String pageCode;

    public static BashPage convert(String rawHtml, BashPageType aPageType, String aPageCode)
    {
        if(rawHtml==null) return null;

        if(cachedHtml!=null && cachedHtml.equals(rawHtml) && bashPage!=null)
        {
            return bashPage;
        }

        pageType = aPageType;
        pageCode = aPageCode;
        bashPage = new BashPage();
        document = Jsoup.parse(rawHtml);

        setupPages();

        Elements rawQuotes = document.select(RAW_QUOTE_QUERY);
        for(Element rawQuote: rawQuotes){
            BashQuote quote = parseQuote(rawQuote);
            if(quote!=null)
                bashPage.add(quote);
        }

        cachedHtml = rawHtml;

        return bashPage;
    }

    private static void setupPages() {
        if(pageType == BashPageType.Page
                || pageType == BashPageType.LastPage
                || pageType == BashPageType.ByRating
                )
        {
            bashPage.currentPage = getCurrentPageBasic();
            bashPage.nextPage = getNextPageBasic();
            bashPage.prevPage = getPrevPageBasic();
        }
        else if(pageType == BashPageType.AbyssBest)
        {
            bashPage.currentPage = getCurrentPageAbyssBest();
            bashPage.nextPage = getNextPageAbyssBest();
            bashPage.prevPage = getPrevPageAbyssBest();

            // в Бездне мы просто ловим первые две ссылки в пейджере,
            // а кто из них кто - вычисляем сейчас
            int linkAge = bashPage.currentPage.compareTo(bashPage.prevPage);
            if(linkAge>0){
                // сurrentPage больше prevPage (которая должна быть НОВЕЕ, т.е. БОЛЬШЕ)
                // значит nextPage отсутствует в Бездне
                bashPage.nextPage = bashPage.prevPage;
                bashPage.prevPage = ""; // удаляем кандидата
            }

        }
        else if(pageType==BashPageType.Comics)
        {
            bashPage.currentPage = getCurrentPageComics();
            bashPage.nextPage = getNextPageComics();
            bashPage.prevPage = getPrevPageComics();
        }
    }
    private static String getCurrentPageBasic()
    {
        String pageCode = "";
        Element pager = document.select(PAGER_QUERY).first();
        if(pager==null) return pageCode;
        Element pageCodeEl = pager.select("input").first();
        if(pageCodeEl == null) return pageCode;

        pageCode = pageCodeEl.val();
        return pageCode;
    }

    private static String getCurrentPageAbyssBest()
    {
        String pageCode = "";
        Element pager = document.select(PAGER_QUERY).first();
        if(pager==null) return pageCode;
        Element pageCodeEl = pager.select("input").first();
        if(pageCodeEl == null) return pageCode;

        pageCode = pageCodeEl.attr("data-date");
        return pageCode;
    }

    private static String getCurrentPageComics()
    {
        String pageCode = "";
        Element pager = document.select(PAGER_COMICS_QUERY).first();
        if(pager==null) return pageCode;
        Element pageCodeEl = pager.select(".current>a").first();
        if(pageCodeEl==null) return pageCode;
        pageCode = pageCodeEl.attr("href");
        pageCode = getLastElementInUri(pageCode);
        return pageCode;
    }

    // PrevPage .........................  .........................
    private static String getPrevPageBasic() {
        String pageCode = "";
        Element el = document.select("link[rel=prev]").first();
        if(el==null) return pageCode;
        pageCode = el.attr("href");
        pageCode = getLastElementInUri(pageCode);
        return pageCode;
    }

    private static String getPrevPageAbyssBest()
    {
        String pageCode = "";
        Element pager = document.select(PAGER_QUERY).first();
        if(pager==null) return pageCode;

        Element el = pager.select("a").first();
        if(el==null) return pageCode;
        pageCode = el.attr("href");
        pageCode = getLastElementInUri(pageCode);
        return pageCode;
    }

    private static String getPrevPageComics() {
        String pageCode = "";

        return pageCode;
    }

    // NextPage .........................  .........................
    private static String getNextPageBasic() {
        Element el = document.select("link[rel=next]").first();
        if(el==null) return "";
        String code = el.attr("href");
        code = getLastElementInUri(code);
        return code;
    }

    private static String getNextPageAbyssBest()
    {
        String pageCode = "";
        Element pager = document.select(PAGER_QUERY).first();
        if(pager==null) return pageCode;

        Element el = pager.select("a").get(1);
        if(el==null) return pageCode;
        pageCode = el.attr("href");
        pageCode = getLastElementInUri(pageCode);
        return pageCode;
    }

    private static String getNextPageComics() {
        String pageCode = "";

        return pageCode;
    }

    // Quotes .........................  .........................
    private static BashQuote parseQuote(Element rawQuote) {
        @SuppressWarnings({"id","rating","text","date"})
        int id, rating;
        String text, date;

        try {
            Elements idList = rawQuote.select(ID_QUERY);
            Elements textList = rawQuote.select(TEXT_QUERY);
            Elements dateList = rawQuote.select(DATE_QUERY);
            Elements ratingList = rawQuote.select(RATING_QUERY);

            // текст - обязателен. Ид и рейтинг нет (у цитат из бездны он не показывается).
            if (textList.size()<1 ) return null;

            // поскольку у цитат из бездны своя уникальная нумерация идами (речь идет про AbyssBest
            // - для них потребуется отдельная таблица в БД


            text = textList.first().html();
            text = Html.fromHtml(text).toString(); // clean br tags

            // id ....
            if(idList.size()<1) { id = 0; }
            else{
                String idStr = idList.first().attr("href");
                idStr = idStr.replace("/quote/", "");
                // у цитат в Бездне - id отсутствует, есть нумерация в AbyssBest
                try {
                    id = Integer.parseInt(idStr);
                }
                catch (Exception e){
                    id = 0;
                }
            }

            if(dateList.size()<1) {date = "00";}
            else{
                date = dateList.first().text();
            }

            if(ratingList.size()<1) {rating = 0;}
            else{
                // у цитат в AbyssBest возможен рейтинг, обозначенный не цифрами (скрытый)
                try {
                    rating = Integer.parseInt(ratingList.first().text());
                }
                catch (Exception e){
                    rating = 0;
                }
            }
        }
        catch (Exception e)
        {
            Log.d(Constants.LOGTAG, e.toString());
            return null;
        }

        return new BashQuote(id,text,date,rating);
    }

    private static String getLastElementInUri(String uri)
    {
        int start = uri.lastIndexOf("/")+1;
        return uri.substring(start);
    }
}

