package com.berbageek.beritaku.feature.home.model;

import com.berbageek.beritaku.R;

public class ArticleItem implements ListItem {
    private final String sourceName;
    private final String author;
    private final String title;
    private final String description;
    private final String url;
    private final String imagePath;
    private final String publishedAt;

    public ArticleItem(String sourceName, String author, String title, String description, String url, String imagePath, String publishedAt) {
        this.sourceName = sourceName;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.imagePath = imagePath;
        this.publishedAt = publishedAt;
    }

    public String getSourceName() {
        return sourceName;
    }

    public String getAuthor() {
        return author;
    }


    public String getTitle() {
        return title;
    }


    public String getDescription() {
        return description;
    }


    public String getUrl() {
        return url;
    }


    public String getImagePath() {
        return imagePath;
    }


    public String getPublishedAt() {
        return publishedAt;
    }


    @Override
    public int getType() {
        return R.layout.article_list_item;
    }
}
