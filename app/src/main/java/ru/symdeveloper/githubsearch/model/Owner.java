package ru.symdeveloper.githubsearch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class Owner {

    private String login;

    @SerializedName("avatar_url")
    private String avatarUrl;

    public String getLogin() {
        return login;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public String toString() {
        return "owner name: " + login + " | owner avatar: " + avatarUrl;
    }
}
