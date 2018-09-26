package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.SecondRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.ui.MainActivity;
import com.juancoob.nanodegree.and.vegginner.util.CheckInternetConnection;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;
import com.juancoob.nanodegree.and.vegginner.util.callbacks.IAlertDialogCallback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This class is the recyclerview's adapter to list recipes on RecipesFragment
 * <p>
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipesListAdapter extends PagedListAdapter<SecondRecipeResponse, RecyclerView.ViewHolder> implements IAlertDialogCallback {

    private static final int TYPE_PROGRESS = 0;
    private static final int TYPE_ITEM = 1;
    private IRetryLoadingCallback mRetryLoadingCallback;
    private Context mCtx;
    private NetworkState mNetworkState;
    private List<String> mFavoriteElementListById;

    protected RecipesListAdapter(IRetryLoadingCallback retryLoadingCallback, Context ctx, List<String> favoriteElementListById) {
        super(SecondRecipeResponse.DIFF_CALLBACK);
        mRetryLoadingCallback = retryLoadingCallback;
        mCtx = ctx;
        mFavoriteElementListById = favoriteElementListById;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_PROGRESS) {
            view = LayoutInflater.from(mCtx).inflate(R.layout.item_load_progress, parent, false);
            return new LoadingViewHolder(view);
        } else {
            view = LayoutInflater.from(mCtx).inflate(R.layout.item_recipe_list, parent, false);
            return new RecipeViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof RecipeViewHolder) {
            ((RecipeViewHolder) holder).bindTo(getItem(position));
        } else {
            ((LoadingViewHolder) holder).bindView(mNetworkState);
        }
        mRetryLoadingCallback.hideProgressBar();
    }

    public boolean isLoadingData() {
        return mNetworkState != null && mNetworkState != NetworkState.LOADED;
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadingData() && position == getItemCount() - 1) {
            return TYPE_PROGRESS;
        } else {
            return TYPE_ITEM;
        }
    }

    public void setNetworkState(NetworkState networkState) {

        NetworkState previousState = mNetworkState;
        boolean wasLoading = isLoadingData();
        mNetworkState = networkState;
        boolean willLoad = isLoadingData();

        if (wasLoading != willLoad) {
            if (wasLoading) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (willLoad && previousState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }

    }

    public void checkInitialLoading(NetworkState networkState) {
        if (networkState != null && networkState.getState().equals(NetworkState.Status.FAILED)) {
            mRetryLoadingCallback.hideProgressBar();

            if (CheckInternetConnection.isConnected(mCtx)) {
                CheckInternetConnection.showDialog(RecipesListAdapter.this, mCtx, R.string.something_wrong_title, R.string.something_wrong_message, R.string.retry, R.string.no);
            } else {
                CheckInternetConnection.showDialog(RecipesListAdapter.this, mCtx, R.string.no_internet_title, R.string.no_internet_message, R.string.retry, R.string.no);
            }
        } else if(networkState != null && networkState.getState().equals(NetworkState.Status.SUCCESS)) {
            // If the list loaded the first page and I'm testing the UI
            ((MainActivity) mCtx).decrementCountingIdlingResource();
        }
    }

    public void updateFavoriteElementListById(List<String> favoriteElementListById) {
        mFavoriteElementListById.clear();
        mFavoriteElementListById.addAll(favoriteElementListById);
        notifyDataSetChanged();
    }

    // Alert dialog results
    @Override
    public void showPositiveResult() {
        mRetryLoadingCallback.loadAllListAgain();
        mRetryLoadingCallback.showProgressBar();
    }

    @Override
    public void showNegativeResult() {
        if (getItemCount() == 0) {
            mRetryLoadingCallback.showNoElements();
        }
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_image)
        public ImageView recipeImageView;

        @BindView(R.id.tv_recipe_title)
        public TextView recipeTitleTextView;

        @BindView(R.id.tv_servings)
        public TextView servingsTextView;

        @BindView(R.id.iv_like_button)
        public ImageView likeButtonImageView;

        private FavoriteRecipe mFavoriteRecipe;
        private Recipe mRecipe;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        public void bindTo(SecondRecipeResponse secondRecipeResponse) {
            Picasso.get().load(secondRecipeResponse.getRecipe().getRecipeImage()).placeholder(R.mipmap.ic_launcher_round).into(recipeImageView);
            recipeTitleTextView.setText(secondRecipeResponse.getRecipe().getRecipeName());
            servingsTextView.setText(String.valueOf(secondRecipeResponse.getRecipe().getRecipeServings()));
            if (mFavoriteElementListById.contains(secondRecipeResponse.getRecipe().getRecipeWeb())) {
                Picasso.get().load(R.drawable.ic_starred_24dp).placeholder(R.drawable.ic_starred_24dp).noFade().into(likeButtonImageView);
            } else {
                Picasso.get().load(R.drawable.ic_star_border_24dp).placeholder(R.drawable.ic_star_border_24dp).noFade().into(likeButtonImageView);
            }
        }

        @OnClick(R.id.iv_like_button)
        public void OnClickFavoriteButton() {
            if (mFavoriteElementListById.contains(getItem(getAdapterPosition()).getRecipe().getRecipeWeb())) {
                mRetryLoadingCallback.deleteFavoriteRecipeByWeb(getItem(getAdapterPosition()).getRecipe().getRecipeWeb());
            } else {
                mRecipe = getItem(getAdapterPosition()).getRecipe();
                mFavoriteRecipe = new FavoriteRecipe(mRecipe.getRecipeName(),
                        mRecipe.getRecipeImage(), mRecipe.getRecipeAuthor(), mRecipe.getRecipeWeb(),
                        mRecipe.getRecipeServings(), mRecipe.getIngredientList());
                mRetryLoadingCallback.insertFavoriteRecipe(mFavoriteRecipe);
            }
        }

        @Override
        public void onClick(View view) {
            mRetryLoadingCallback.showRecipeDetails(Objects.requireNonNull(getItem(getAdapterPosition())).getRecipe());
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pb_loading)
        public ProgressBar loadingProgressBar;

        @BindView(R.id.tv_no_results)
        public TextView noResultsTextView;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindView(NetworkState networkState) {

            if (networkState != null && networkState.getState() == NetworkState.Status.RUNNING) {
                loadingProgressBar.setVisibility(View.VISIBLE);
            } else {
                loadingProgressBar.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getState() == NetworkState.Status.FAILED) {
                noResultsTextView.setVisibility(View.VISIBLE);
                noResultsTextView.setText(mCtx.getString(R.string.no_results));
                if (CheckInternetConnection.isConnected(mCtx)) {
                    CheckInternetConnection.showDialog(RecipesListAdapter.this, mCtx, R.string.something_wrong_title, R.string.something_wrong_message, R.string.retry, R.string.no);
                } else {
                    CheckInternetConnection.showDialog(RecipesListAdapter.this, mCtx, R.string.no_internet_title, R.string.no_internet_message, R.string.retry, R.string.no);
                }
            } else {
                noResultsTextView.setVisibility(View.GONE);
            }
        }
    }

}
