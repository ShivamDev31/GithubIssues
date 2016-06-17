package com.shivamdev.githubissues.fragments;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.adapters.CommentsAdapter;
import com.shivamdev.githubissues.data.CommentsData;
import com.shivamdev.githubissues.main.CommonUtils;
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
public class CommentsDialog extends DialogFragment {

    private static final String COMMENTS_URL = "comments_url";

    private CommentsAdapter adapter;
    private ProgressBar pbLoader;
    private TextView tvError;
    private RecyclerView rvComments;

    public static CommentsDialog newInstance(String url) {
        CommentsDialog fragment = new CommentsDialog();
        Bundle args = new Bundle();
        args.putString(COMMENTS_URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.comments_dialog, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvComments = (RecyclerView) view.findViewById(R.id.rv_comments_list);
        pbLoader = (ProgressBar) view.findViewById(R.id.pb_loader);
        tvError = (TextView) view.findViewById(R.id.tv_error);
        rvComments.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CommentsAdapter(getActivity());
        rvComments.setAdapter(adapter);

        if (!CommonUtils.isNetworkAvailable(getActivity())) {
            return;
        }
        getCommentsFromAsync();
    }

    private void getCommentsFromAsync() {
        new AsyncTask<Void, Void, List<CommentsData>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pbLoader.setVisibility(View.VISIBLE);
            }

            @Override
            protected List<CommentsData> doInBackground(Void... params) {
                if (!TextUtils.isEmpty(getCommentsFromServer())) {
                    CommentsData[] issuesArray = GsonUtil.getInstance().fromJson(getCommentsFromServer(), CommentsData[].class);
                    return Arrays.asList(issuesArray);
                } else {
                    showError(true);
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<CommentsData> commentsData) {
                super.onPostExecute(commentsData);
                pbLoader.setVisibility(View.GONE);
                if (commentsData == null || commentsData.size() == 0) {
                    showError(true);
                } else {
                    showError(false);
                    adapter.refreshList(commentsData);
                }
            }
        }.execute();
    }

    private String getCommentsFromServer() {
        try {
            URL url = new URL(getArguments().getString(COMMENTS_URL));
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
            showError(true);
        } catch (IOException e) {
            e.printStackTrace();
            showError(true);
        }
        return null;
    }

    private void showError(boolean isVisible) {
        if (isVisible) {
            tvError.setVisibility(View.VISIBLE);
            rvComments.setVisibility(View.GONE);
            pbLoader.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.GONE);
            rvComments.setVisibility(View.VISIBLE);
            pbLoader.setVisibility(View.GONE);
        }
    }
}