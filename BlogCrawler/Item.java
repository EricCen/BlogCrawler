package com.ericcen.BlogCrawler;

/**
 * Created by eric on 2015/3/6.
 */
public class Item {
    private String link;
    private String title;

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Item(String link, String title) {

        this.link = link;
        this.title = title;
    }
}
