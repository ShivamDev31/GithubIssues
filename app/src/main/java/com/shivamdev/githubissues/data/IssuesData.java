package com.shivamdev.githubissues.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shivam on 16-06-2016.
 */
public class IssuesData {

    @SerializedName("title")
    public String title;

    @SerializedName("body")
    public String body;

    @SerializedName("comments_url")
    public String commentsUrl;
}
