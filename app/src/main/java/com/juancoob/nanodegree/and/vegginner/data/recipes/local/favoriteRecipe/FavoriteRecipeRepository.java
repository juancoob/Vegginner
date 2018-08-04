package com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;

import com.juancoob.nanodegree.and.vegginner.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;

/**
 * This is the favorite recipe repository to get the data using the FavoriteRecipeDao
 *
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class FavoriteRecipeRepository {

    private final FavoriteRecipeDao mFavoriteRecipeDao;
    private final AppExecutors mAppExecutors;

    @Inject
    public FavoriteRecipeRepository(FavoriteRecipeDao favoriteRecipeDao, AppExecutors appExecutors) {
        mFavoriteRecipeDao = favoriteRecipeDao;
        mAppExecutors = appExecutors;
    }

    public DataSource.Factory<Integer, FavoriteRecipe> getFavoriteRecipeList() {
        return mFavoriteRecipeDao.getFavoriteRecipeList();
    }

    public LiveData<List<String>> getFavoriteRecipeListById() {
        return mFavoriteRecipeDao.getFavoriteRecipeListByWebsite();
    }

    public LiveData<FavoriteRecipe> getFavoriteRecipeById(String recipeWeb) {
        return mFavoriteRecipeDao.getFavoriteRecipeByWebsite(recipeWeb);
    }


    public void deleteFavoriteRecipe(final FavoriteRecipe recipe) {
        mAppExecutors.getDiskIO().execute(() -> mFavoriteRecipeDao.deleteFavoriteRecipe(recipe));
    }

    public void deleteFavoriteRecipeByWeb(final String recipeWeb) {
        mAppExecutors.getDiskIO().execute(() -> mFavoriteRecipeDao.deleteFavoriteRecipeByWeb(recipeWeb));
    }

    public void insertFavoriteRecipe(final FavoriteRecipe recipe) {
        mAppExecutors.getDiskIO().execute(() -> mFavoriteRecipeDao.insertFavoriteRecipe(recipe));
    }
}
