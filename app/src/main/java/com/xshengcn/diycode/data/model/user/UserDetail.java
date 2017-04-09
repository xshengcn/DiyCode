package com.xshengcn.diycode.data.model.user;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class UserDetail {

    @SerializedName("id")
    public int id;
    @SerializedName("login")
    public String login;
    @SerializedName("name")
    public String name;
    @SerializedName("avatar_url")
    public String avatarUrl;
    @SerializedName("location")
    public String location;
    @SerializedName("company")
    public String company;
    @SerializedName("twitter")
    public String twitter;
    @SerializedName("website")
    public String website;
    @SerializedName("bio")
    public String bio;
    @SerializedName("tagline")
    public String tagline;
    @SerializedName("github")
    public String github;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("email")
    public String email;
    @SerializedName("topics_count")
    public int topicsCount;
    @SerializedName("replies_count")
    public int repliesCount;
    @SerializedName("following_count")
    public int followingCount;
    @SerializedName("followers_count")
    public int followersCount;
    @SerializedName("favorites_count")
    public int favoritesCount;
    @SerializedName("level")
    public String level;
    @SerializedName("level_name")
    public String levelName;
}
