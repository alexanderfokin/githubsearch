package ru.symdeveloper.githubsearch.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import icepick.Icepick;
import icepick.State;
import ru.symdeveloper.githubsearch.Constants;
import ru.symdeveloper.githubsearch.R;
import ru.symdeveloper.githubsearch.model.GitHubItem;
import ru.symdeveloper.githubsearch.ui.adapters.SearchResultsAdapter;
import ru.symdeveloper.githubsearch.ui.presenters.MainPresenter;
import ru.symdeveloper.githubsearch.ui.presenters.MainPresenterFactory;
import ru.symdeveloper.githubsearch.ui.presenters.MainView;
import ru.symdeveloper.githubsearch.ui.presenters.base.BasePresenterLoader;
import ru.symdeveloper.githubsearch.utils.Settings;

public class MainActivity extends AppCompatActivity implements MainView,
        SearchResultsAdapter.SearchResultsAdapterListener,
        LoaderManager.LoaderCallbacks<MainPresenter> {

    private static final String TAG = MainActivity.class.getName();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    FrameLayout emptyView;

    @BindView(R.id.progressView)
    ProgressBar progressView;

    SearchView searchView;

    private SearchResultsAdapter adapter;

    private MainPresenter presenter;

    @State
    boolean searchViewExpanded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Settings.hasAuthInformation()) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            Icepick.restoreInstanceState(this, savedInstanceState);
            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);

            getSupportLoaderManager().initLoader(Constants.LOADER_ID_MAIN, null, this);

            adapter = new SearchResultsAdapter(this, this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);

            setEmptyState();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onViewAttached(this);
    }

    @Override
    protected void onDestroy() {
        if (presenter != null) {
            presenter.onViewDetached(false);
        }
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Icepick.restoreInstanceState(this, savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.onActionViewCollapsed();
            setScreenTitle();
        } else {
            super.onBackPressed();
        }
    }

    // после вызова onQueryTextSubmit происходит сброс текста в searchView и, в итоге, вызывается onQueryTextChange с пустой строкой
    // если мы хотим сохранить значение в searchView, нам нужно игнорировать один вызов onQueryTextChange сразу после onQueryTextSubmit
    // для этого и служит этот флаг. такой сценарий, лично мне, видится более логичным, нежели дефолтный сценарий от Гугла
    private boolean skipNextTextChange = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity, menu);
        MenuItem searchMenuItem = menu.findItem(R.id.actionSearch);

        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String text) {
                skipNextTextChange = true;
                searchView.onActionViewCollapsed();
                searchViewExpanded = false;
                setScreenTitle();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (!skipNextTextChange) {
                    doSearch(query);
                }
                skipNextTextChange = false;
                return true;
            }
        });

        searchView.setOnSearchClickListener(v -> {
            searchView.setQuery(presenter.getQuery(), false);
            skipNextTextChange = true;
            searchView.onActionViewExpanded();
            searchViewExpanded = true;
        });

        searchView.setQuery(presenter.getQuery(), false);
        skipNextTextChange = true;
        if (searchViewExpanded) {
            searchView.onActionViewExpanded();
        }
        return true;
    }

    private void setScreenTitle() {
        if (!TextUtils.isEmpty(presenter.getQuery())) {
            setTitle(presenter.getQuery());
        } else {
            setTitle(getString(R.string.app_name));
        }
    }

    private void doSearch(String query) {
        adapter.clear();
        setProgressState();
        presenter.search(query);
    }

    @Override
    public void searchResultReceived(@NonNull List<GitHubItem> items) {
        if (items.isEmpty() && adapter.isEmpty()) {
            setEmptyState();
        } else {
            setContentState();
            adapter.addItems(items);
        }
    }

    @Override
    public void searchError() {
    }

    @Override
    public void itemSelected(int position) {
        GitHubItem item = adapter.getItem(position);
        if (item != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(item.getHtmlUrl()));
            startActivity(intent);
        }
    }

    @Override
    public void loadNextPage() {
        if (presenter.canLoadNextPage()) {
            presenter.loadNextPage();
        }
    }

    @Override
    public boolean canLoadNextPage() {
        return presenter.canLoadNextPage();
    }

    @Override
    public Loader<MainPresenter> onCreateLoader(int id, Bundle args) {
        return new BasePresenterLoader<>(this, new MainPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<MainPresenter> loader, MainPresenter presenter) {
        this.presenter = presenter;
        searchResultReceived(presenter.getItems());
        setScreenTitle();
    }

    @Override
    public void onLoaderReset(Loader<MainPresenter> loader) {
        this.presenter = null;
    }

    private void setEmptyState() {
        recyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.GONE);
    }

    private void setContentState() {
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        progressView.setVisibility(View.GONE);
    }

    private void setProgressState() {
        progressView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
    }
}
