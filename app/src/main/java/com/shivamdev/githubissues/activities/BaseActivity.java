package com.shivamdev.githubissues.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.main.CommonUtils;
import com.shivamdev.githubissues.main.LogToast;

/**
 * Created by Shivam on 16-06-2016.
 */
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private Snackbar snackbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
    }

    protected void initializeToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (CommonUtils.isNetworkAvailable(BaseActivity.this)) {
                LogToast.log(TAG, "Network connected");
                hideNoConnectionMessage();
            } else if (intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, Boolean.FALSE)) {
                LogToast.log(TAG, "There's no network connectivity");
                showNoConnectionMessage();
            } else {
                LogToast.log(TAG, "There's no network connectivity");
                showNoConnectionMessage();
            }
        }
    };

    public void showNoConnectionMessage() {
        if (snackbar != null) {
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.transparent_black_60));
            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
        }
    }

    public void hideNoConnectionMessage() {
        if (snackbar != null) {
            snackbar.dismiss();
        }
    }

    @Override
    protected void onStart() {
        IntentFilter networkChangeFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, networkChangeFilter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
        }
        super.onStop();
    }
}
