package com.berbageek.beritaku.api.model.response;

import com.berbageek.beritaku.api.model.data.Article;

import java.util.List;

public class TopHeadlinesResponse extends NewsApiResponse<List<Article>> {
    private List<Article> articles;

    @Override
    public List<Article> getResults() {
        return articles;
    }

    @Override
    public void setResults(List<Article> results) {
        articles = results;
    }
}
