package com.xshengcn.diycode.data.model.user;

import com.google.gson.annotations.SerializedName;

import com.xshengcn.diycode.data.model.common.Abilities;
import com.xshengcn.diycode.data.model.common.User;
import com.xshengcn.diycode.data.model.topic.Topic;

import java.util.Date;

public class Notification {


    @SerializedName("id")
    public int id;
    @SerializedName("type")
    public String type; // TopicReply, Mention, Follow, Topic
    @SerializedName("read")
    public boolean read;
    @SerializedName("actor")
    public User actor;
    @SerializedName("mention_type")
    public String mentionType; // HacknewsReply, Reply
    @SerializedName("mention")
    public Mention mention;
    @SerializedName("topic")
    public Topic topic;
    @SerializedName("reply")
    public Reply reply;
    @SerializedName("node")
    public String node;
    @SerializedName("created_at")
    public Date createdAt;
    @SerializedName("updated_at")
    public Date updatedAt;


    public static class Reply {


        @SerializedName("id")
        public int id;
        @SerializedName("body_html")
        public String bodyHtml;
        @SerializedName("created_at")
        public String createdAt;
        @SerializedName("updated_at")
        public String updatedAt;
        @SerializedName("deleted")
        public boolean deleted;
        @SerializedName("topic_id")
        public int topicId;
        @SerializedName("user")
        public User user;
        @SerializedName("likes_count")
        public int likesCount;
        @SerializedName("abilities")
        public Abilities abilities;
        @SerializedName("body")
        public String body;
        @SerializedName("topic_title")
        public String topicTitle;


    }

    public static class Mention {


        @SerializedName("id")
        public int id;
        @SerializedName("body_html")
        public String bodyHtml;
        @SerializedName("created_at")
        public Date createdAt;
        @SerializedName("updated_at")
        public Date updatedAt;
        @SerializedName("deleted")
        public boolean deleted;
        @SerializedName("topic_id")
        public int topicId;
        @SerializedName("user")
        public User user;
        @SerializedName("likes_count")
        public int likesCount;
        @SerializedName("abilities")
        public Abilities abilities;


    }
}
