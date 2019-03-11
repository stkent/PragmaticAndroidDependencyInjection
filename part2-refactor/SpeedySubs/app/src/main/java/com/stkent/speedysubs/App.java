package com.stkent.speedysubs;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

public final class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);
    }
}
