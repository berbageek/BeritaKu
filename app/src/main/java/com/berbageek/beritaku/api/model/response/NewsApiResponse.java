package com.berbageek.beritaku.api.model.response;

public abstract class NewsApiResponse<T> {
    protected String status;
    protected String code;
    protected String message;
    protected int totalResults;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public abstract T getResults();

    public abstract void setResults(T results);
}
