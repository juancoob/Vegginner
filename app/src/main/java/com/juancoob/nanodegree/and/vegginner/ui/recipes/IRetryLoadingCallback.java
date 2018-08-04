package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;

/**
 * This interface helps to communicate from RecipesListAdapter to RecipesFragment
 *
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public interface IRetryLoadingCallback {
    void loadAllListAgain();
    void showProgressBar();
    void hideProgressBar();
    void showNoElements();
    void showNoFavElements();
    void showRecipeDetails(Recipe selectedRecipe);
    void deleteFavoriteRecipe(FavoriteRecipe favoriteRecipe);
    void deleteFavoriteRecipeByWeb(String webRecipe);
    void insertFavoriteRecipe(FavoriteRecipe favoriteRecipe);
}
