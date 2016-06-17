package com.shivamdev.githubissues.activities;

import android.os.Bundle;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.fragments.IssuesFragment;

/**
 * Created by Shivam on 16-06-2016.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeToolbar(getString(R.string.app_name));
        addIssuesFragment();
    }

    private void addIssuesFragment() {
        IssuesFragment issuesFragment = IssuesFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.ll_fragment, issuesFragment)
                .commit();
    }
}