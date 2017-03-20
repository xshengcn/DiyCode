package com.xshengcn.diycode.data.model.project;

import com.google.gson.annotations.SerializedName;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Project implements Parcelable {

    public static final Creator<Project> CREATOR = new Creator<Project>() {
        @Override
        public Project createFromParcel(Parcel in) {
            return new Project(in);
        }

        @Override
        public Project[] newArray(int size) {
            return new Project[size];
        }
    };
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("description")
    public String description;
    @SerializedName("readme")
    public String readme;
    @SerializedName("github")
    public String github;
    @SerializedName("website")
    public String website;
    @SerializedName("download")
    public String download;
    @SerializedName("star")
    public int star;
    @SerializedName("fork")
    public int fork;
    @SerializedName("watch")
    public int watch;
    @SerializedName("project_cover_url")
    public String projectCoverUrl;
    @SerializedName("label_str")
    public String labelStr;
    @SerializedName("category")
    public Category category;
    @SerializedName("sub_category")
    public Category subCategory;
    @SerializedName("last_updated_at")
    public Date lastUpdatedAt;

    public Project() {
    }

    public Project(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        readme = in.readString();
        github = in.readString();
        website = in.readString();
        download = in.readString();
        star = in.readInt();
        fork = in.readInt();
        watch = in.readInt();
        projectCoverUrl = in.readString();
        labelStr = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        subCategory = in.readParcelable(Category.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(readme);
        dest.writeString(github);
        dest.writeString(website);
        dest.writeString(download);
        dest.writeInt(star);
        dest.writeInt(fork);
        dest.writeInt(watch);
        dest.writeString(projectCoverUrl);
        dest.writeString(labelStr);
        dest.writeParcelable(category, flags);
        dest.writeParcelable(subCategory, flags);
    }
}
