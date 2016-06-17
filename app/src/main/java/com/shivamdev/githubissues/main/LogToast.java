package com.shivamdev.githubissues.main;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by shivamchopra on 05/06/16.
 */
public class LogToast {

    private static final String TAG = LogToast.class.getSimpleName();

    public static void log(String tag, String text) {
        Log.d(tag, text);
    }

    public static void log(String text) {
        log(TAG, text);
    }

    public static void toast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
