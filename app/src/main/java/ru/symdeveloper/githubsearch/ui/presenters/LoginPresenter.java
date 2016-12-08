package ru.symdeveloper.githubsearch.ui.presenters;

import ru.symdeveloper.githubsearch.App;
import ru.symdeveloper.githubsearch.ui.presenters.base.BaseMvpPresenter;
import ru.symdeveloper.githubsearch.utils.ApiUtils;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class LoginPresenter extends BaseMvpPresenter<LoginView> {

    private static final String TAG = LoginPresenter.class.getName();

    public void login() {
        App.getApiService().login()
                    .subscribeOn(ApiUtils.networkScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(serverResponse -> {
                        notifyLoginCompleted();
                    }, throwable -> {
                        // обработка ошибок - это отдельная, обширная тема.
                        // в рамках этой задачи считаем, что у нас одна ошибка - неправильные авторизационные данные
                        notifyLoginError();
                    });
    }

    private void notifyLoginCompleted() {
        if (getView() != null) {
            getView().loginCompleted();
        }
    }

    private void notifyLoginError() {
        if (getView() != null) {
            getView().loginError();
        }
    }
}
