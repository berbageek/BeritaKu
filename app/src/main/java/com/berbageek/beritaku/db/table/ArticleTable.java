package com.berbageek.beritaku.db.table;

public class ArticleTable {
    public static final String TABLE_NAME = "articles";

    public static final String KEY_ARTICLE_ID = "id";
    public static final String KEY_ARTICLE_URL = "url";
    public static final String KEY_ARTICLE_IS_READ = "is_read";
    public static final String KEY_ARTICLE_DATE_ADDED = "date_added";
    public static final String KEY_ARTICLE_DATE_UPDATED = "date_updated";

    public static String getCreationQuery() {
        return "CREATE TABLE " + TABLE_NAME
                + " ( " + KEY_ARTICLE_ID + " INTEGER PRIMARY KEY, "
                + KEY_ARTICLE_URL + " TEXT, "
                + KEY_ARTICLE_IS_READ + " INTEGER, "
                + KEY_ARTICLE_DATE_ADDED + " TEXT, "
                + KEY_ARTICLE_DATE_UPDATED + " TEXT "
                + ") ";
    }
}
