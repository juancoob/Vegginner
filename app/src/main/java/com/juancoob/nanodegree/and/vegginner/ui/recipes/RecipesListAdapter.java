package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.app.AlertDialog;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.SecondRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipesListAdapter extends PagedListAdapter<SecondRecipeResponse, RecyclerView.ViewHolder> {

    public static final int TYPE_PROGRESS = 0;
    public static final int TYPE_ITEM = 1;
    private RecipesFragment mRecipesFragment;

    private Context mCtx;
    private NetworkState mNetworkState;

    protected RecipesListAdapter(RecipesFragment recipesFragment, Context ctx) {
        super(SecondRecipeResponse.DIFF_CALLBACK);
        mRecipesFragment = recipesFragment;
        mCtx = ctx;
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
        mRecipesFragment.hideProgressBar();
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
        if (networkState != null
                && (networkState.getState().equals(NetworkState.Status.FAILED)
                || networkState.getState().equals(NetworkState.Status.NO_INTERNET))) {
            showNoInternetDialog();
        }
    }

    private void showNoInternetDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);
        builder.setTitle(R.string.no_internet_title)
                .setMessage(R.string.no_internet_message)
                .setPositiveButton(R.string.retry, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    mRecipesFragment.loadListAgain();
                    mRecipesFragment.showProgressBar();
                })
                .setNegativeButton(R.string.no, (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_image)
        public ImageView recipeImageView;

        @BindView(R.id.tv_recipe_title)
        public TextView recipeTitleTextView;

        @BindView(R.id.tv_servings)
        public TextView servingsTextView;

        @BindView(R.id.iv_like_button)
        public ImageView likeButtonImageView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindTo(SecondRecipeResponse secondRecipeResponse) {
            Picasso.get().load(secondRecipeResponse.getRecipe().getRecipeImage()).placeholder(R.mipmap.ic_launcher_round).into(recipeImageView);
            recipeTitleTextView.setText(secondRecipeResponse.getRecipe().getRecipeName());
            servingsTextView.setText(String.valueOf(secondRecipeResponse.getRecipe().getRecipeServings()));
        }

        @OnClick(R.id.iv_like_button)
        public void OnClickFavoriteButton() {

            //todo
            /*if (likeButtonImageView.getDrawable().getConstantState() != null &&
                    likeButtonImageView.getDrawable().getConstantState().equals(mCtx.getResources().getDrawable(R.drawable.ic_star_border_24dp).getConstantState())) {
                Toast.makeText(mCtx, "Añadir favorito", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.ic_starred_24dp).placeholder(R.drawable.ic_starred_24dp).noFade().into(likeButtonImageView);
            } else {
                Toast.makeText(mCtx, "Quitar favorito", Toast.LENGTH_SHORT).show();
                Picasso.get().load(R.drawable.ic_star_border_24dp).placeholder(R.drawable.ic_star_border_24dp).noFade().into(likeButtonImageView);
            }*/
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
            } else {
                noResultsTextView.setVisibility(View.GONE);
            }

            if (networkState != null && networkState.getState() == NetworkState.Status.NO_INTERNET) {
                showNoInternetDialog();
            }
        }
    }

}
