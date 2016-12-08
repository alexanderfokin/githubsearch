package ru.symdeveloper.githubsearch.ui.presenters;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ru.symdeveloper.githubsearch.App;
import ru.symdeveloper.githubsearch.model.GitHubItem;
import ru.symdeveloper.githubsearch.ui.activities.MainActivity;
import ru.symdeveloper.githubsearch.ui.presenters.base.BaseMvpPresenter;
import ru.symdeveloper.githubsearch.utils.ApiUtils;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class MainPresenter extends BaseMvpPresenter<MainView> {

    private static final String TAG = MainPresenter.class.getName();

    //т.к. presenter в данном примере очень простой, он выполняет одновременно и функции модели
    private int page = 1;
    private ArrayList<GitHubItem> items = new ArrayList<>();
    private String query = "";
    private boolean lastPage = false;


    private Subscription currentRequest;

    public void search(String newQuery) {
        if (!this.query.equals(newQuery)) {
            Log.d(TAG, "new search: " + newQuery);
            this.query = newQuery;
            page = 1;
            lastPage = false;
            items.clear();
            sendRequest(newQuery);
        } else {
            notifySearchResultReceived(items);
        }
    }

    public String getQuery() {
        return query;
    }

    public void loadNextPage() {
        if (currentRequest == null) {
            Log.d(TAG, "items count: " + items.size() + " | loadNextPage!!!");
            page++;
            sendRequest(query);
        }
    }

    public ArrayList<GitHubItem> getItems() {
        return items;
    }

    public boolean canLoadNextPage() {
        return !lastPage;
    }

    private void sendRequest(String query) {
        if (currentRequest != null) {
            currentRequest.unsubscribe();
        }
        if (!TextUtils.isEmpty(query)) {
            currentRequest = App.getApiService().search(query, page)
                    .subscribeOn(ApiUtils.networkScheduler())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(serverResponse -> {
                        currentRequest = null;
                        items.addAll(serverResponse.getItems());
                        if (serverResponse.getItems().isEmpty()) {
                            lastPage = true;
                        }
                        notifySearchResultReceived(serverResponse.getItems());
                    }, throwable -> {
                        currentRequest = null;
                        notifySearchError();
                    });
        } else {
            //если решили искать пустоту, сразу говорим, что результатов нет
            notifySearchResultReceived(new ArrayList<>());
        }
    }

    private void notifySearchResultReceived(List<GitHubItem> items) {
        if (getView() != null) {
            getView().searchResultReceived(items);
        }
    }

    private void notifySearchError() {
        if (getView() != null) {
            getView().searchError();
        }
    }
}
