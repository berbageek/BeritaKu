package com.berbageek.beritaku.repository;

import android.support.annotation.NonNull;

import com.berbageek.beritaku.repository.callback.TopHeadlineCallback;

public interface NewsRepository {
    void getTopHeadlines(@NonNull TopHeadlineCallback callback);
}
