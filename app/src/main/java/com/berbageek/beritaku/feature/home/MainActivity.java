package com.berbageek.beritaku.feature.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.berbageek.beritaku.R;
import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.db.NewsDatabase;
import com.berbageek.beritaku.feature.home.adapter.ListItemAdapter;
import com.berbageek.beritaku.feature.home.loader.TopHeadlineLoader;
import com.berbageek.beritaku.feature.home.model.ListItem;
import com.berbageek.beritaku.feature.shared.ArticleItemBuilder;
import com.berbageek.beritaku.repository.NewsRepository;
import com.berbageek.beritaku.repository.callback.TopHeadlineCallback;
import com.berbageek.beritaku.repository.implementation.NewsApiRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_NEWSLIST_STATE = "NEWSLIST_STATE";

    private RecyclerView newsListRecyclerView;
    private RecyclerView.LayoutManager newsListLayoutManager;
    private Parcelable newsListState;

    private ListItemAdapter newsListItemAdapter;

    private ProgressBar newsLoadingProgressBar;
    private Toolbar toolbar;

    private NewsRepository newsRepository;

    private TopHeadlineCallback topHeadlineCallback;

    private LoaderManager.LoaderCallbacks<List<Article>> topHeadlineLoaderCallbacks = new LoaderManager.LoaderCallbacks<List<Article>>() {
        @Override
        public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
            return new TopHeadlineLoader(MainActivity.this, newsRepository);
        }

        @Override
        public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
            hideProgressBar();
            if (!articles.isEmpty()) {
                List<ListItem> listItems = convertArticle(articles);
                newsListItemAdapter.replaceListItems(listItems);
            } else {
                newsListItemAdapter.clearListItems();
            }
            showList();
        }

        @Override
        public void onLoaderReset(Loader<List<Article>> loader) {
            // do nothing
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newsRepository = new NewsApiRepository(NewsDatabase.getInstance(getApplicationContext()));

        restoreSavedInstaceState(savedInstanceState);

        bindViews();
        setupToolbars();
        setupNewsList();

//        fetchTopHeadline();
        getTopHeadline();
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
//            fetchTopHeadline();
            refreshTopHeadline();
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

    private void getTopHeadline() {
        showProgressBar();
        hideList();
        getSupportLoaderManager().initLoader(
                TopHeadlineLoader.TOP_HEADLINE_LOADER_ID,
                null,
                topHeadlineLoaderCallbacks
        );
    }

    private void refreshTopHeadline() {
        showProgressBar();
        hideList();
        getSupportLoaderManager().restartLoader(
                TopHeadlineLoader.TOP_HEADLINE_LOADER_ID,
                null,
                topHeadlineLoaderCallbacks
        );
    }

    private void fetchTopHeadline() {
        new TopHeadlineAsyncTask().execute();
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

    List<ListItem> convertArticle(List<Article> articles) {
        List<ListItem> results = new ArrayList<>();
        for (Article article : articles) {
            results.add(new ArticleItemBuilder(article).build());
        }
        return results;
    }

    class TopHeadlineAsyncTask extends AsyncTask<Void, Void, List<Article>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressBar();
            hideList();
        }

        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {
                return newsRepository.getTopHeadlines();
            } catch (Exception e) {
                Log.d("TopHeadlineAsyncTask", "doInBackground: ");
            }
            return Collections.emptyList();
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);

            hideProgressBar();
            if (!articles.isEmpty()) {
                List<ListItem> listItems = convertArticle(articles);
                newsListItemAdapter.replaceListItems(listItems);
            } else {
                newsListItemAdapter.clearListItems();
            }
            showList();
        }
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
    }

}
