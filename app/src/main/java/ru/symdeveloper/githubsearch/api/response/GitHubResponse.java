package ru.symdeveloper.githubsearch.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import ru.symdeveloper.githubsearch.model.GitHubItem;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class GitHubResponse {
    @SerializedName("total_count")
    private int totalCount;

    @SerializedName("incomplete_results")
    private boolean incompleteResults;

    private ArrayList<GitHubItem> items = new ArrayList<>();


    public int getTotalCount() {
        return totalCount;
    }

    public boolean isIncompleteResults() {
        return incompleteResults;
    }

    public ArrayList<GitHubItem> getItems() {
        return items;
    }
}
