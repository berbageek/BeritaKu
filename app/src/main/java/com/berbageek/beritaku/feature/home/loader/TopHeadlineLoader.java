package com.berbageek.beritaku.feature.home.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.repository.NewsRepository;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class TopHeadlineLoader extends AsyncTaskLoader<List<Article>> {
    public static final int TOP_HEADLINE_LOADER_ID = 101;
    private static final String TAG = "TopHeadlineLoader";
    private NewsRepository newsRepository;
    private List<Article> articles;

    public TopHeadlineLoader(Context context, NewsRepository newsRepository) {
        super(context);
        this.newsRepository = newsRepository;
    }

    @Override
    protected void onStartLoading() {
        if (articles == null) {
            forceLoad();
        } else {
            deliverResult(articles);
        }
    }

    @Override
    public List<Article> loadInBackground() {
        try {
            return newsRepository.getTopHeadlines();
        } catch (Exception e) {
            Log.e(TAG, "loadInBackground: ", e);
            if (e instanceof IOException) {
                return null;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void deliverResult(List<Article> data) {
        articles = data;
        super.deliverResult(data);
    }
}
