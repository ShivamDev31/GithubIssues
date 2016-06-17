package com.shivamdev.githubissues.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shivamdev.githubissues.R;
import com.shivamdev.githubissues.data.CommentsData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shivam on 16-06-2016.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentsHolder> {
    private Context mContext;
    private List<CommentsData> commentsData;

    public CommentsAdapter(Context context) {
        this.mContext = context;
        commentsData = new ArrayList<>();
    }

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.comments_item, parent, false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsHolder holder, int position) {
        holder.tvAuthorName.setText(commentsData.get(position).user.userName);
        holder.tvCommentBody.setText(commentsData.get(position).body);
    }

    @Override
    public int getItemCount() {
        return commentsData.size();
    }

    public void refreshList(List<CommentsData> data) {
        commentsData.clear();
        commentsData.addAll(data);
        notifyDataSetChanged();
    }

    class CommentsHolder extends RecyclerView.ViewHolder {
        private TextView tvAuthorName;
        private TextView tvCommentBody;

        public CommentsHolder(View view) {
            super(view);
            tvAuthorName = (TextView) view.findViewById(R.id.tv_author);
            tvCommentBody = (TextView) view.findViewById(R.id.tv_body);
        }
    }
}
