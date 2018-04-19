package com.berbageek.beritaku.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.berbageek.beritaku.api.model.data.Article;
import com.berbageek.beritaku.db.table.ArticleTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewsDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NEWS.db";
    public static final int DATABASE_VERSION = 1;

    public static NewsDatabase sInstance;

    private NewsDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized NewsDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NewsDatabase(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticleTable.getCreationQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // do nothing
    }

    public long addArticle(Article article) {
        long articleId = -1;
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            values.put(ArticleTable.KEY_ARTICLE_URL, article.getUrl());
            values.put(ArticleTable.KEY_ARTICLE_DATE_ADDED, article.getPublishedAt());
            articleId = db.insertOrThrow(ArticleTable.TABLE_NAME, null, values);
        } catch (Exception e) {
            // do nothing
        }

        return articleId;
    }

    public void updateArticleDate(String url) {
        SQLiteDatabase db = getWritableDatabase();

        try {
            ContentValues values = new ContentValues();
            Calendar now = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
            values.put(ArticleTable.KEY_ARTICLE_DATE_UPDATED, sdf.format(now.getTime()));
            db.update(ArticleTable.TABLE_NAME, values, ArticleTable.KEY_ARTICLE_URL + "= ?", new String[]{url});
        } catch (Exception e) {
            // do nothing
        }
    }
}
