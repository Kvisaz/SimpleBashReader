package ru.kvisaz.bashreader.parser;

import android.text.Html;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ru.kvisaz.bashreader.model.BashPage;
import ru.kvisaz.bashreader.model.BashPageComics;
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


    public static BashPage convert(String rawHtml, BashPageType aPageType, String aPageCode)
    {
        if(rawHtml==null) return null;

        if(cachedHtml!=null && cachedHtml.equals(rawHtml) && bashPage!=null)
        {
            return bashPage;
        }

        if(aPageType==BashPageType.Comics)
            bashPage = new BashPageComics();
        else
            bashPage = new BashPage();


        bashPage.type = aPageType;
        bashPage.currentPage = aPageCode; // can be changed
        document = Jsoup.parse(rawHtml);

        setupPages();

        if(bashPage.type==BashPageType.Comics)
            parseComics();
        else
            parseQuotes();

        cachedHtml = rawHtml;
        return bashPage;
    }

    // Comics .......................
    private static void parseComics() {
        BashPageComics page = (BashPageComics) bashPage;

        page.pictureUrl = "";
        page.about = "";
        page.authorUrl = "";
        page.quoteId = "";

        Element elTmp = document.select("#cm_strip").first();
        if(elTmp==null) {
            page.setError("Comics No Found");
            return;
        }
        page.pictureUrl = elTmp.attr("src");

        elTmp = document.select("#boiler>.backlink").first();
        if(elTmp==null) return;

        page.about = elTmp.html();

        elTmp = elTmp.select("a").first();
        if(elTmp!=null){
            page.authorUrl = elTmp.attr("href");
        }


        int startQuote = page.about.indexOf("#")+1;
        if(startQuote==-1 || startQuote >= page.about.length()) return;
        page.quoteId = page.about.substring(startQuote);
        int endQuote = page.quoteId.indexOf("<");
        if(endQuote==-1) {
            page.quoteId = ""; // обнуляем неудачный парсинг
            return;
        }
        page.quoteId = page.quoteId.substring(0,endQuote);
        }


    // Quotes.........................
    private static void parseQuotes() {
        Elements rawQuotes = document.select(RAW_QUOTE_QUERY);
        for(Element rawQuote: rawQuotes){
            BashQuote quote = parseQuote(rawQuote);
            if(quote!=null)
                bashPage.add(quote);
        }
    }

    private static void setupPages() {
        if( bashPage.type == BashPageType.Page
                ||  bashPage.type == BashPageType.LastPage
                ||  bashPage.type == BashPageType.ByRating
                )
        {
            bashPage.currentPage = getCurrentPageBasic();
            bashPage.nextPage = getNextPageBasic();
            bashPage.prevPage = getPrevPageBasic();
        }
        else if( bashPage.type == BashPageType.AbyssBest)
        {
            setAbyssBestPages();
        }
        else if( bashPage.type==BashPageType.Comics)
        {
            setComicsPages();
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
        Element pager = document.select("div.thumbs").first();
        if(pager==null) return pageCode;
        Element pageCodeEl = pager.select(".current>a").first();
        if(pageCodeEl == null) return pageCode;

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



    // NextPage .........................  .........................
    private static String getNextPageBasic() {
        Element el = document.select("link[rel=next]").first();
        if(el==null) return "";
        String code = el.attr("href");
        code = getLastElementInUri(code);
        return code;
    }

    // todo  исправить парсинг номера - чтобы подходило для любой длины
    // AbyssBest structure:  div.pager > a... a input a ... a a
    private static void setAbyssBestPages()
    {
        bashPage.currentPage = getCurrentPageAbyssBest();
        Element elPager = document.select("div.pager").first();
        if(elPager == null) return;
        String pagerHtml = elPager.html(); // direct children

        String[] pagerSplitHtml = pagerHtml.split("class=\"current\"");
        final int PAGECODE_LENGTH = 8; // 20160302

        try {
            if (pagerSplitHtml.length == 2) { // prev and next OR current is last
                String startTag = "abyssbest/";
                int start = pagerSplitHtml[0].lastIndexOf(startTag);
                if (start != -1) {
                    start = start + startTag.length();
                    bashPage.prevPage = pagerSplitHtml[0].substring(start, start + PAGECODE_LENGTH); // abyssbest/20150308

                }

                start = pagerSplitHtml[1].indexOf(startTag);
                if (start != -1) {
                    start = start + startTag.length();

                    bashPage.nextPage = pagerSplitHtml[1].substring(start, start + PAGECODE_LENGTH); // abyssbest/20150308

                }

            }
        }
        catch (Exception e)
        {
            Log.d(Constants.LOGTAG, e.toString());
        }

    }

    // todo  исправить парсинг номера - чтобы подходило для любой длины
// AbyssBest structure:  div.thumbs > a... a a.current a ... a a
    private static void setComicsPages()
    {
        bashPage.currentPage = getCurrentPageComics();
        Element elPager = document.select("div.thumbs").first();
        if(elPager == null) return;

        String pagerHtml = elPager.html(); // direct children

        String[] pagerSplitHtml = pagerHtml.split("class=\"current\"");

        final int PAGECODE_LENGTH = 8; // 20160302
//        !  prevPage и nextPage на сайте развернуты в другую сторону, здесь я их привожу к единому
//           формату (prev - current - next)
        try {
            if (pagerSplitHtml.length == 2) { // must be always
                String startTag = "comics/";
                int start = pagerSplitHtml[0].lastIndexOf(startTag);
                if (start != -1) {
                    start = start + startTag.length();
                    bashPage.nextPage = pagerSplitHtml[0].substring(start, start + PAGECODE_LENGTH); // comics/20160302
                }

                start = pagerSplitHtml[1].indexOf(startTag);
                start = pagerSplitHtml[1].indexOf(startTag,start+startTag.length()); // защита от собственного а
                if (start != -1) {
                    start = start + startTag.length();
                    bashPage.prevPage = pagerSplitHtml[1].substring(start, start + PAGECODE_LENGTH); // comics/20150308
                }

            }
        }
        catch (Exception e)
        {
            Log.d(Constants.LOGTAG, e.toString());
        }

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
            String idQuery = ID_QUERY;
            if(bashPage.type == BashPageType.AbyssTop) idQuery = "span.abysstop";
            else if(bashPage.type == BashPageType.AbyssBest) idQuery = "span.id";
            Elements idList = rawQuote.select(idQuery);

            Elements textList = rawQuote.select(TEXT_QUERY);

            String dateQuery = "span.date";
            if(bashPage.type == BashPageType.AbyssTop) dateQuery = "span.abysstop-date";
            Elements dateList = rawQuote.select(dateQuery);
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
                String idStr;
                if(bashPage.type == BashPageType.AbyssTop) { //#1
                    idStr = idList.first().text();
                    idStr = idStr.replace("#", "");
                }
                else if(bashPage.type == BashPageType.AbyssBest) // #AA-290980
                {
                    idStr = idList.first().text();
                    idStr = idStr.replace("#AA-", "");
                }
                else{ //#290980
                    idStr = idList.first().attr("href");
                    idStr = idStr.replace("/quote/", "");
                }
                try {
                    id = Integer.parseInt(idStr);
                }
                catch (Exception e){
                    id = 0;
                }
            }

            if(dateList.size()<1) {date = "00";}
            else { date = dateList.first().text(); }

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

