package ru.symdeveloper.githubsearch.ui.presenters.base;

import android.support.annotation.UiThread;

public interface MvpPresenter<V extends MvpView> {
    @UiThread
    void onViewAttached(V view);

    @UiThread
    void onViewDetached(boolean retainInstance);

    @UiThread
    void onDestroyed();
}
