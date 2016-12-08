package ru.symdeveloper.githubsearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class GitHubItem {
    private int id;

    private String name;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("html_url")
    private String htmlUrl;

    private String description;

    private Owner owner;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getOwnerName() {
        if (owner != null) {
            return owner.getLogin();
        }
        return "";
    }

    public String getOwnerAvatar() {
        if (owner != null) {
            return owner.getAvatarUrl();
        }
        return "";
    }

    @Override
    public String toString() {
        if (owner != null) {
            return "id: " + id + " | name: " + name + " " + owner;
        } else {
            return "id: " + id + " | name: " + name;
        }
    }
}
