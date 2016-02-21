package ru.kvisaz.bashreader.custom;

import java.util.Date;


public class BashQuote {
    public int id;
    public String text;
    public String  date;
    public int rating;

    public boolean isReaded;
    public Date lastReaded;

    public BashQuote(int id, String text, String date, int rating) {
        this.id = id;
        this.text = text;
        this.date = date;
        this.rating = rating;
    }
}
