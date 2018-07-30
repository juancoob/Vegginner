package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;

/**
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public interface IRetryLoadingCallback {
    void loadListAgain();
    void showProgressBar();
    void hideProgressBar();
    void showNoElements();
    void showRecipeDetails(Recipe selectedRecipe);
}
