package ru.symdeveloper.githubsearch.ui.viewHolders;

import android.support.v7.widget.RecyclerView;
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

public class LoaderViewHolder extends RecyclerView.ViewHolder {

    public LoaderViewHolder(View view) {
        super(view);
    }
}
