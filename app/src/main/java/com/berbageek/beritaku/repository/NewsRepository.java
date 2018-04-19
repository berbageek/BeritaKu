package com.berbageek.beritaku.repository;

import android.support.annotation.NonNull;

import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.repository.callback.TopHeadlineCallback;

import java.util.List;

public interface NewsRepository {
    void getTopHeadlines(@NonNull TopHeadlineCallback callback);

    List<Article> getTopHeadlines() throws Exception;
}
