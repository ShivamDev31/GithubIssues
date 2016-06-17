package com.shivamdev.githubissues.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.adapters.IssuesAdapter;
import com.shivamdev.githubissues.data.IssuesData;
import com.shivamdev.githubissues.main.CommonUtils;
import com.shivamdev.githubissues.main.Constants;
import com.shivamdev.githubissues.main.GsonUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Shivam on 16-06-2016.
 */
public class IssuesFragment extends Fragment {
    private IssuesAdapter adapter;
    private ProgressBar pbLoader;
    private TextView tvError;
    private SwipeRefreshLayout refreshIssues;
    private RecyclerView rvIssues;

    public static IssuesFragment newInstance() {
        Bundle args = new Bundle();
        IssuesFragment fragment = new IssuesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvIssues = (RecyclerView) view.findViewById(R.id.rv_issues_list);
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        refreshIssues = (SwipeRefreshLayout) view.findViewById(R.id.srl_refresh_list);
        rvIssues.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new IssuesAdapter(getActivity());
        rvIssues.setAdapter(adapter);

        refreshIssues.setOnRefreshListener(new RefreshIssues());

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            return;
        }
        getIssuesFromAsync();
    }

    private void getIssuesFromAsync() {
        new AsyncTask<Void, Void, List<IssuesData>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                refreshIssues.setRefreshing(false);
                pbLoader.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<IssuesData> doInBackground(Void... params) {
                if (!TextUtils.isEmpty(getIssuesFromServer())) {
                    IssuesData[] issuesArray = GsonUtil.getInstance().fromJson(getIssuesFromServer(), IssuesData[].class);
                    return Arrays.asList(issuesArray);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<IssuesData> issuesData) {
                super.onPostExecute(issuesData);
                pbLoader.setVisibility(View.GONE);
                if (issuesData == null || issuesData.size() == 0) {
                    showError(true);
                } else {
                    showError(false);
                    adapter.refreshList(issuesData);
                }
            }
        }.execute();
    }

    private String getIssuesFromServer() {
        try {
            URL url = new URL(Constants.ISSUES_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            InputStream in = conn.getInputStream();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            return stringBuilder.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private class RefreshIssues implements SwipeRefreshLayout.OnRefreshListener {
        @Override
        public void onRefresh() {
            if (!CommonUtils.isNetworkAvailable(getActivity())) {
                refreshIssues.setRefreshing(false);
                showError(true);
                return;
            }
            refreshIssues.setRefreshing(true);
            getIssuesFromAsync();
        }
    }

    private void showError(boolean isVisible) {
        if (isVisible) {
            tvError.setVisibility(View.VISIBLE);
            rvIssues.setVisibility(View.GONE);
            pbLoader.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.GONE);
            rvIssues.setVisibility(View.VISIBLE);
            pbLoader.setVisibility(View.GONE);
        }
    }
}