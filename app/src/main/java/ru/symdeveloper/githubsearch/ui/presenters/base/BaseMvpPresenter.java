package ru.symdeveloper.githubsearch.ui.presenters.base;

import android.support.annotation.Nullable;
import android.support.annotation.UiThread;

import java.lang.ref.WeakReference;

public class BaseMvpPresenter<V extends MvpView> implements MvpPresenter<V> {
    private WeakReference<V> viewRef;

    @UiThread
    @Override
    public void onViewAttached(V view) {
        viewRef = new WeakReference<V>(view);
    }

    @Override
    public void onViewDetached(boolean retainInstance) {
        if(viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    public void onDestroyed() {
    }

    @UiThread
    @Nullable public V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    @UiThread
    public boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }
}
