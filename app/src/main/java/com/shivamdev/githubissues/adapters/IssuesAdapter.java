package com.shivamdev.githubissues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.activities.MainActivity;
import com.shivamdev.githubissues.data.IssuesData;
import com.shivamdev.githubissues.fragments.CommentsDialog;
import com.shivamdev.githubissues.main.CommonUtils;
import com.shivamdev.githubissues.main.LogToast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 16-06-2016.
 */
public class IssuesAdapter extends RecyclerView.Adapter<IssuesAdapter.IssuesHolder> {
    private static final String COMMENTS_TAG = "dialog";
    private Context mContext;
    private List<IssuesData> issuesData;

    public IssuesAdapter(Context context) {
        this.mContext = context;
        this.issuesData = new ArrayList<>();
    }

    @Override
    public IssuesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
        return new IssuesHolder(view);
    }

    @Override
    public void onBindViewHolder(IssuesHolder holder, int position) {
        holder.tvTitle.setText(issuesData.get(position).title);
        holder.tvDesc.setText(issuesData.get(position).body);
    }

    @Override
    public int getItemCount() {
        return issuesData.size();
    }

    public void refreshList(List<IssuesData> data) {
        issuesData.clear();
        issuesData.addAll(data);
        notifyDataSetChanged();
    }

    class IssuesHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvDesc;

        public IssuesHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity activity = (MainActivity) mContext;
                    String commentsUrl = issuesData.get(getLayoutPosition()).commentsUrl;
                    if (CommonUtils.isNetworkAvailable(mContext) && !TextUtils.isEmpty(commentsUrl)) {
                        CommentsDialog dialog = CommentsDialog.newInstance(issuesData.get(getLayoutPosition()).commentsUrl);
                        activity.getSupportFragmentManager().beginTransaction()
                                .add(dialog, COMMENTS_TAG).commit();
                    } else {
                        LogToast.toast(mContext, mContext.getString(R.string.no_comments_url));
                    }

                }
            });
        }
    }
}