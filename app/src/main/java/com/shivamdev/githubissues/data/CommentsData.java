package com.shivamdev.githubissues.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Shivam on 16-06-2016.
 */
public class CommentsData {

    @SerializedName("user")
    public User user;

    @SerializedName("body")
    public String body;

    public static class User {

        @SerializedName("login")
        public String userName;
    }
}
