package ru.symdeveloper.githubsearch.ui.presenters;

import android.support.annotation.NonNull;

import java.util.List;

import ru.symdeveloper.githubsearch.model.GitHubItem;
import ru.symdeveloper.githubsearch.ui.presenters.base.MvpView;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public interface MainView extends MvpView {
    void searchResultReceived(@NonNull List<GitHubItem> items);

    void searchError();
}
