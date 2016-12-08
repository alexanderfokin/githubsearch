package ru.symdeveloper.githubsearch.ui.viewHolders;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.symdeveloper.githubsearch.R;
import ru.symdeveloper.githubsearch.model.GitHubItem;
import ru.symdeveloper.githubsearch.ui.adapters.SearchResultsAdapter;
import ru.symdeveloper.githubsearch.utils.CropCircleTransformation;

/**
 * Created by Alexander Fokin on 07.12.2016.
 */

public class GitHubItemViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.description)
    TextView description;

    public GitHubItemViewHolder(View view, SearchResultsAdapter.SearchResultsAdapterListener listener) {
        super(view);
        ButterKnife.bind(this, view);
        view.setOnClickListener(v -> listener.itemSelected(getAdapterPosition()));
    }

    public void bindView(GitHubItem item) {
        Glide.with(itemView.getContext())
                .load(item.getOwnerAvatar())
                .asBitmap()
                .transform(new CropCircleTransformation(itemView.getContext()))
                .into(avatar);
        title.setText(String.format(itemView.getResources().getString(R.string.title_format), item.getName(), item.getOwnerName()));
        if (!TextUtils.isEmpty(item.getDescription())) {
            description.setVisibility(View.VISIBLE);
            description.setText(item.getDescription());
        } else {
            description.setVisibility(View.GONE);
        }
    }
}
