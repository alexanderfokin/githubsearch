package ru.symdeveloper.githubsearch.ui.presenters;

import ru.symdeveloper.githubsearch.ui.presenters.base.BasePresenterFactory;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class MainPresenterFactory implements BasePresenterFactory<MainPresenter> {

    @Override
    public MainPresenter create() {
        return new MainPresenter();
    }
}
