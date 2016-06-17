package com.shivamdev.githubissues.main;

import android.app.Application;

/**
 * Created by shivamchopra on 03/06/16.
 */
public class MainApplication extends Application {
    private static MainApplication instance;

    public static MainApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}