package com.berbageek.beritaku.feature.shared;

import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.feature.home.MainActivity;
import com.berbageek.beritaku.feature.home.model.ArticleItem;

public class ArticleItemBuilder {
    private String sourceName;
    private String author;
    private String title;
    private String description;
    private String url;
    private String imagePath;
    private String publishedAt;

    public ArticleItemBuilder() {

    }

    public ArticleItemBuilder(Article article) {
        sourceName = article.getSource().getName();
        author = article.getAuthor();
        title = article.getTitle();
        description = article.getDescription();
        url = article.getUrl();
        imagePath = article.getUrlToImage();
        publishedAt = article.getPublishedAt();
    }

    public ArticleItemBuilder setSourceName(String sourceName) {
        this.sourceName = sourceName;
        return this;
    }

    public ArticleItemBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public ArticleItemBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public ArticleItemBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public ArticleItemBuilder setImagePath(String imagePath) {
        this.imagePath = imagePath;
        return this;
    }

    public ArticleItemBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public ArticleItemBuilder setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
        return this;
    }

    public ArticleItem build() {
        return new ArticleItem(sourceName, author, title, description, url, imagePath, publishedAt);
    }
}
