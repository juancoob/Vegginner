package com.juancoob.nanodegree.and.vegginner.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.juancoob.nanodegree.and.vegginner.data.places.ISearchApiService;
import com.juancoob.nanodegree.and.vegginner.data.places.PlaceRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;

import javax.inject.Inject;

/**
 * This class is the ViewModel factory which gives the correct ViewModel for every class
 *
 * Created by Juan Antonio Cobos Obrero on 29/07/18.
 */
public class VegginnerViewModelFactory implements ViewModelProvider.Factory {

    private FavoriteRecipeRepository mFavoriteRecipeRepository;
    private IngredientRepository mIngredientRepository;
    private PlaceRepository mPlaceRepository;
    private IRecipeApiService mRecipeApiService;
    private ISearchApiService mSearchApiService;

    @Inject
    public VegginnerViewModelFactory(FavoriteRecipeRepository favoriteRecipeRepository,
                                     IngredientRepository ingredientRepository,
                                     PlaceRepository placeRepository,
                                     IRecipeApiService recipeApiService,
                                     ISearchApiService searchApiService) {
        mFavoriteRecipeRepository = favoriteRecipeRepository;
        mIngredientRepository = ingredientRepository;
        mPlaceRepository = placeRepository;
        mRecipeApiService = recipeApiService;
        mSearchApiService = searchApiService;
    }

    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(RecipesViewModel.class)) {
            return (T) new RecipesViewModel(mFavoriteRecipeRepository, mIngredientRepository, mRecipeApiService);
        } if(modelClass.isAssignableFrom(PlacesViewModel.class)) {
            return (T) new PlacesViewModel(mSearchApiService, mPlaceRepository);
        } else {
            throw new IllegalArgumentException("ViewModel " + modelClass + " not found");
        }
    }
}
