package ru.symdeveloper.githubsearch.ui.presenters.base;

import android.content.Context;
import android.support.v4.content.Loader;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class BasePresenterLoader<T extends MvpPresenter> extends Loader<T> {
    private final BasePresenterFactory<T> factory;
    private T presenter;

    public BasePresenterLoader(Context context, BasePresenterFactory<T> factoryImpl) {
        super(context);
        factory = factoryImpl;
    }

    @Override
    protected void onStartLoading() {
        // If we already own an instance, simply deliver it.
        if (presenter != null) {
            deliverResult(presenter);
            return;
        }
        // Otherwise, force a load
        forceLoad();
    }

    @Override
    protected void onForceLoad() {
        // Create the Presenter using the Factory
        presenter = factory.create();
        // Deliver the result
        deliverResult(presenter);
    }

    @Override
    protected void onReset() {
        presenter.onDestroyed();
        presenter = null;
    }
}