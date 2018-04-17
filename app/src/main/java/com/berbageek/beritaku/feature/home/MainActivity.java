package com.berbageek.beritaku.feature.home;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.berbageek.beritaku.R;
import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.feature.home.adapter.ListItemAdapter;
import com.berbageek.beritaku.feature.home.model.ListItem;
import com.berbageek.beritaku.feature.shared.ArticleItemBuilder;
import com.berbageek.beritaku.repository.NewsRepository;
import com.berbageek.beritaku.repository.callback.TopHeadlineCallback;
import com.berbageek.beritaku.repository.implementation.NewsApiRepository;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NEWSLIST_STATE = "NEWSLIST_STATE";

    private RecyclerView newsListRecyclerView;
    private RecyclerView.LayoutManager newsListLayoutManager;
    private Parcelable newsListState;

    private ListItemAdapter newsListItemAdapter;

    private ProgressBar newsLoadingProgressBar;
    private Toolbar toolbar;

    private NewsRepository newsRepository = new NewsApiRepository();

    private TopHeadlineCallback topHeadlineCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        restoreSavedInstaceState(savedInstanceState);

        bindViews();
        setupToolbars();
        setupNewsList();

        fetchTopHeadline();
    }

    private void restoreSavedInstaceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            newsListState = savedInstanceState.getParcelable(KEY_NEWSLIST_STATE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (newsListState != null) {
            newsListLayoutManager.onRestoreInstanceState(newsListState);
        }
    }

    @Override
    protected void onDestroy() {
        cancelTopHeadlineRequest();
        super.onDestroy();
    }

    private void cancelTopHeadlineRequest() {
        if (topHeadlineCallback != null) {
            topHeadlineCallback.cancelRequest();
            topHeadlineCallback = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.refresh) {
            fetchTopHeadline();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        newsListState = newsListLayoutManager.onSaveInstanceState();
        outState.putParcelable(KEY_NEWSLIST_STATE, newsListState);
    }

    private void fetchTopHeadline() {
        showProgressBar();
        hideList();
        cancelTopHeadlineRequest();
        topHeadlineCallback = new MainTopHeadlineCallback();
        newsRepository.getTopHeadlines(topHeadlineCallback);
    }

    void bindViews() {
        toolbar = findViewById(R.id.toolbar);

        newsListRecyclerView = findViewById(R.id.news_recyclerview);
        newsLoadingProgressBar = findViewById(R.id.news_load_progressbar);
    }

    void showProgressBar() {
        newsLoadingProgressBar.setVisibility(View.VISIBLE);
    }

    void hideProgressBar() {
        newsLoadingProgressBar.setVisibility(View.GONE);
    }

    void showList() {
        newsListRecyclerView.setVisibility(View.VISIBLE);
    }

    void hideList() {
        newsListRecyclerView.setVisibility(View.GONE);
    }

    void setupToolbars() {
        setSupportActionBar(toolbar);
    }

    void setupNewsList() {
        newsListRecyclerView.setHasFixedSize(false);

        newsListLayoutManager = new LinearLayoutManager(this);
        newsListRecyclerView.setLayoutManager(newsListLayoutManager);

        newsListItemAdapter = new ListItemAdapter(new ArrayList<ListItem>());
        newsListRecyclerView.setAdapter(newsListItemAdapter);
    }

    class MainTopHeadlineCallback implements TopHeadlineCallback {
        private boolean isCanceled = false;

        @Override
        public void onArticleLoaded(List<Article> articles) {
            List<ListItem> listItems = convertArticle(articles);
            if (!isCanceled) {
                hideProgressBar();
                newsListItemAdapter.replaceListItems(listItems);
                showList();
            }
        }

        @Override
        public void onEmptyArticle() {
            if (!isCanceled) {
                hideProgressBar();
                newsListItemAdapter.clearListItems();
                showList();
            }
        }

        @Override
        public void onError() {
            if (!isCanceled) {
                hideProgressBar();
            }
        }

        @Override
        public synchronized void cancelRequest() {
            isCanceled = true;
        }

        List<ListItem> convertArticle(List<Article> articles) {
            List<ListItem> results = new ArrayList<>();
            for (Article article : articles) {
                results.add(new ArticleItemBuilder(article).build());
            }
            return results;
        }
    }

}
