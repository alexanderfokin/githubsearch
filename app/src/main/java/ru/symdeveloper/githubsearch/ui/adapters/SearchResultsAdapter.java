package ru.symdeveloper.githubsearch.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.symdeveloper.githubsearch.R;
import ru.symdeveloper.githubsearch.model.GitHubItem;
import ru.symdeveloper.githubsearch.ui.viewHolders.GitHubItemViewHolder;
import ru.symdeveloper.githubsearch.ui.viewHolders.LoaderViewHolder;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class SearchResultsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final static int TYPE_ITEM = 0;
    private final static int TYPE_LOADER = 1;

    private List<GitHubItem> items;
    private LayoutInflater inflater;
    private SearchResultsAdapterListener listener;

    public SearchResultsAdapter(Context context, SearchResultsAdapterListener listener) {
        items = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void addItems(@NonNull List<GitHubItem> items) {
        if (!items.isEmpty()) {
            int startPosition = this.items.size();
            this.items.addAll(items);
            notifyItemRangeInserted(startPosition, items.size());
        } else {
            notifyDataSetChanged();
        }
    }

    public void clear() {
        items.clear();
        notifyDataSetChanged();
    }

    public GitHubItem getItem(final int position) {
        if (position >= 0 && position < items.size()) {
            return items.get(position);
        } else {
            return null;
        }
    }

    public boolean isEmpty() {
        return items.size() == 0;
    }

    public int getItemViewType(int position) {
        if (position < items.size()) {
            return TYPE_ITEM;
        } else {
            return TYPE_LOADER;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.listitem_search_result, parent, false);
            return new GitHubItemViewHolder(view, listener);
        } else {
            View view = inflater.inflate(R.layout.listitem_loader, parent, false);
            return new LoaderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < items.size()) {
            ((GitHubItemViewHolder)holder).bindView(getItem(position));
            if (position == items.size() - 1) {
                listener.loadNextPage();
            }
        }
    }

    @Override
    public int getItemCount() {
        if (!items.isEmpty()) {
            if (listener.canLoadNextPage()) {
                return items.size() + 1;
            } else {
                return items.size();
            }
        } else {
            return 0;
        }
    }

    public interface SearchResultsAdapterListener {
        void itemSelected(int position);
        void loadNextPage();
        boolean canLoadNextPage();
    }
}
