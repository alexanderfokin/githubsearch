package ru.symdeveloper.githubsearch.ui.presenters;

import ru.symdeveloper.githubsearch.ui.presenters.base.MvpView;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public interface LoginView extends MvpView {
    void loginCompleted();

    void loginError();
}
