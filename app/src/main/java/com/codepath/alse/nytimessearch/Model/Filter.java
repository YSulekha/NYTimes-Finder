package com.codepath.alse.nytimessearch.Model;

import org.parceler.Parcel;

/**
 * Created by aharyadi on 10/22/16.
 */

@Parcel
public class Filter {

    String sortOrder;
    String news;
    String date;
    boolean isArts;
    boolean isFashion;

    public void setArts(boolean arts) {
        isArts = arts;
    }

    public void setFashion(boolean fashion) {
        isFashion = fashion;
    }

    public void setSports(boolean sports) {
        isSports = sports;
    }

    boolean isSports;

    public Filter(){

    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getNews() {
        return news;
    }
    public boolean isArts() {
        return isArts;
    }

    public boolean isFashion() {
        return isFashion;
    }

    public boolean isSports() {
        return isSports;
    }

    public void setNews(String news) {
        this.news = news;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
