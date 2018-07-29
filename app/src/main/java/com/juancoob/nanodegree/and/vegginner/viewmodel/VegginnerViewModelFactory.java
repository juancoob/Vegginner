package com.juancoob.nanodegree.and.vegginner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;

import javax.inject.Inject;

/**
 * Created by Juan Antonio Cobos Obrero on 29/07/18.
 */
public class VegginnerViewModelFactory implements ViewModelProvider.Factory {

    private FavoriteRecipeRepository mFavoriteRecipeRepository;
    private IRecipeApiService mRecipeApiService;

    @Inject
    public VegginnerViewModelFactory(FavoriteRecipeRepository favoriteRecipeRepository, IRecipeApiService recipeApiService) {
        mFavoriteRecipeRepository = favoriteRecipeRepository;
        mRecipeApiService = recipeApiService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(RecipesViewModel.class)) {
            return (T) new RecipesViewModel(mFavoriteRecipeRepository, mRecipeApiService);
        } else {
            throw new IllegalArgumentException("ViewModel " + modelClass + " not found");
        }
    }
}
