package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.arch.paging.PagedListAdapter;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Juan Antonio Cobos Obrero on 3/08/18.
 */
public class FavoriteListAdapter extends PagedListAdapter<FavoriteRecipe, FavoriteListAdapter.FavoriteRecipeViewHolder> {

    private IRetryLoadingCallback mRetryLoadingCallback;

    public FavoriteListAdapter(IRetryLoadingCallback retryLoadingCallback) {
        super(FavoriteRecipe.DIFF_CALLBACK);
        mRetryLoadingCallback = retryLoadingCallback;
    }

    @NonNull
    @Override
    public FavoriteRecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_list, parent, false);
        return new FavoriteRecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteRecipeViewHolder holder, int position) {
        FavoriteRecipe favoriteRecipe = getItem(position);
        Picasso.get().load(favoriteRecipe.getRecipeImage()).placeholder(R.mipmap.ic_launcher_round).into(holder.recipeImageView);
        holder.recipeTitleTextView.setText(favoriteRecipe.getRecipeName());
        holder.servingsTextView.setText(String.valueOf(favoriteRecipe.getRecipeServings()));
        Picasso.get().load(R.drawable.ic_starred_24dp).placeholder(R.drawable.ic_starred_24dp).noFade().into(holder.likeButtonImageView);
    }

    public class FavoriteRecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_recipe_image)
        public ImageView recipeImageView;

        @BindView(R.id.tv_recipe_title)
        public TextView recipeTitleTextView;

        @BindView(R.id.tv_servings)
        public TextView servingsTextView;

        @BindView(R.id.iv_like_button)
        public ImageView likeButtonImageView;

        public FavoriteRecipeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @OnClick(R.id.iv_like_button)
        public void OnClickFavoriteButton() {
            mRetryLoadingCallback.deleteFavoriteRecipe(getItem(getAdapterPosition()));
        }

        @Override
        public void onClick(View view) {
            FavoriteRecipe favoriteRecipe = getItem(getAdapterPosition());
            Recipe recipe = new Recipe(favoriteRecipe.getRecipeName(),
                    favoriteRecipe.getRecipeImage(), favoriteRecipe.getRecipeAuthor(),
                    favoriteRecipe.getRecipeWeb(), favoriteRecipe.getRecipeServings(),
                    favoriteRecipe.getIngredientList());
            mRetryLoadingCallback.showRecipeDetails(recipe);
        }
    }
}