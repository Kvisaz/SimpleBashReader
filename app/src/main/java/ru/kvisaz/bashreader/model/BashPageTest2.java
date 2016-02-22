package ru.kvisaz.bashreader.model;

import java.util.ArrayList;


public class BashPageTest2 extends BashPage {

    public BashPageTest2(){

        quotes = new ArrayList<>();

        int quote_id = 1;
        String date = "2016-01-10 09:12"; 
        int rating = 3915;
        String text = "Это просто второй набор данных - чтобы показать, что смена данных в ListView работает";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 2;
        date = "2016-01-10 08:46";
        rating = 416;
        text = "Ну так же она работает?";

        quotes.add(new BashQuote(quote_id,text,date,rating));

        quote_id = 3;
        date = "2016-01-10 08:46";
        rating = 416;
        text = "Третья цитата.";

        quotes.add(new BashQuote(quote_id,text,date,rating));



    }

}
