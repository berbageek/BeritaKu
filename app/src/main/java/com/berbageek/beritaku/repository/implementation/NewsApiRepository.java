package com.berbageek.beritaku.repository.implementation;

import android.support.annotation.NonNull;
import android.util.Log;

import com.berbageek.beritaku.BuildConfig;
import com.berbageek.beritaku.api.NewsApiSource;
import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.api.model.response.TopHeadlinesResponse;
import com.berbageek.beritaku.db.NewsDatabase;
import com.berbageek.beritaku.repository.NewsRepository;
import com.berbageek.beritaku.repository.callback.TopHeadlineCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsApiRepository implements NewsRepository {
    private static final String TAG = "NewsApiRepository";

    private static final String BASE_URL = "https://newsapi.org";
    private static final String API_KEY = BuildConfig.NEWSAPI_KEY;

    private NewsApiSource newsApiSource;
    private NewsDatabase newsDatabase;

    public NewsApiRepository(NewsDatabase newsDb) {
        newsDatabase = newsDb;

        // create custom http client
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new RequestInterceptor())
                .build();

        // build the newsApiSource from retrofit builder
        newsApiSource = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApiSource.class);
    }

    @Override
    public void getTopHeadlines(@NonNull final TopHeadlineCallback callback) {
        Call<TopHeadlinesResponse> topHeadlineCall = newsApiSource.getTopHeadlines();

        topHeadlineCall.enqueue(new Callback<TopHeadlinesResponse>() {
            @Override
            public void onResponse(Call<TopHeadlinesResponse> call, Response<TopHeadlinesResponse> response) {
                TopHeadlinesResponse topHeadlines = response.body();
                if (topHeadlines != null) {
                    Log.d(TAG, "onResponse: " + topHeadlines.getTotalResults());
                    if ("ok".equalsIgnoreCase(topHeadlines.getStatus())) {
                        if (topHeadlines.getTotalResults() > 0 && topHeadlines.getResults() != null) {
                            addArticlesToDb(topHeadlines.getResults());
                            callback.onArticleLoaded(topHeadlines.getResults());
                        } else {
                            callback.onEmptyArticle();
                        }
                    }
                } else {
                    Log.d(TAG, "onResponse: null");
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<TopHeadlinesResponse> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                callback.onError();
            }
        });
    }

    @Override
    public List<Article> getTopHeadlines() throws Exception {
        Call<TopHeadlinesResponse> topHeadlineCall = newsApiSource.getTopHeadlines();
        List<Article> articles = new ArrayList<>();
        try {
            Response<TopHeadlinesResponse> response = topHeadlineCall.execute();
            TopHeadlinesResponse topHeadlines = response.body();
            if (topHeadlines != null) {
                Log.d(TAG, "onResponse: " + topHeadlines.getTotalResults());
                if ("ok".equalsIgnoreCase(topHeadlines.getStatus())) {
                    if (topHeadlines.getTotalResults() > 0 && topHeadlines.getResults() != null) {
                        addArticlesToDb(topHeadlines.getResults());
                        articles = topHeadlines.getResults();
                    }
                }
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            Log.e(TAG, "onFailure: ", e);
            throw e;
        }
        return articles;
    }

    private void addArticlesToDb(List<Article> articles) {
        for (Article article : articles) {
            newsDatabase.addArticle(article);
        }
    }

    public class RequestInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request originalRequest = chain.request();
            Request request = originalRequest.newBuilder()
                    .header("Authorization", API_KEY)
                    .build();
            return chain.proceed(request);
        }
    }
}
