package com.berbageek.beritaku.repository.callback;

import com.berbageek.beritaku.api.model.data.Article;

import java.util.List;

public interface TopHeadlineCallback {
    void onArticleLoaded(List<Article> articles);

    void onEmptyArticle();

    void onError();

    void cancelRequest();
}
