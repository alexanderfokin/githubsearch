package ru.symdeveloper.githubsearch.ui.presenters.base;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public interface BasePresenterFactory<T extends MvpPresenter> {
    T create();
}
