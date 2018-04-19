package com.berbageek.beritaku;

import com.facebook.stetho.Stetho;

public class DebugApp extends BeritaKuApp {
    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }
}
