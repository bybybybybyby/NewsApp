package com.example.brian.newsapp;

public class Article {

    private String mTitle;
    private String mSection;
    private String mAuthor;
    private String mUrl;
    private String mDate;

    public Article(String title, String section, String author, String url, String date) {
        mTitle = title;
        mSection = section;
        mAuthor = author;
        mUrl = url;
        mDate = date;
    }

//    public Article(String title, String section, String author, String url) {
//        mTitle = title;
//        mSection = section;
//        mAuthor = author;
//        mUrl = url;
//    }
//
//    public Article(String title, String section, String url) {
//        mTitle = title;
//        mSection = section;
//        mUrl = url;
//    }

    public String getTitle() { return mTitle; }
    public String getSection() { return mSection; }
    public String getAuthor() { return mAuthor; }
    public String getUrl() { return  mUrl; }
    public String getDate() { return mDate; }
}
