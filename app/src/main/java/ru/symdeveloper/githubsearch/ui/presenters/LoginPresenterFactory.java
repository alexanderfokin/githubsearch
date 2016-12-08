package ru.symdeveloper.githubsearch.ui.presenters;

import ru.symdeveloper.githubsearch.ui.presenters.base.BasePresenterFactory;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class LoginPresenterFactory implements BasePresenterFactory<LoginPresenter> {

    @Override
    public LoginPresenter create() {
        return new LoginPresenter();
    }
}
