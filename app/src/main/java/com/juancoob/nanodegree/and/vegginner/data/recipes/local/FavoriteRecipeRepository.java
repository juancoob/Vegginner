package com.juancoob.nanodegree.and.vegginner.data.recipes.local;

import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class FavoriteRecipeRepository {

    private final FavoriteRecipeDao mFavoriteRecipeDao;

    //@Inject
    public FavoriteRecipeRepository(FavoriteRecipeDao mFavoriteRecipeDao) {
        this.mFavoriteRecipeDao = mFavoriteRecipeDao;
    }

    public LiveData<List<FavoriteRecipe>> getFavoriteRecipeList() {
        return mFavoriteRecipeDao.getFavoriteRecipeList();
    }

    public LiveData<List<String>> getFavoriteRecipeListById() {
        return mFavoriteRecipeDao.getFavoriteRecipeListByWebsite();
    }


    public void deleteFavoriteRecipe(final FavoriteRecipe recipe) {
        //todo try appExecutor from github example
        new Executor() {
            @Override
            public void execute(@NonNull Runnable runnable) {
                mFavoriteRecipeDao.deleteFavoriteRecipe(recipe);
            }
        };
    }

    public void insertFavoriteRecipe(FavoriteRecipe recipe) {
        new insertRecipeAsyncTask(mFavoriteRecipeDao).execute(recipe);
    }

    private static class insertRecipeAsyncTask extends AsyncTask<FavoriteRecipe, Void, Void> {

        private final FavoriteRecipeDao mFavoriteRecipeDao;

        public insertRecipeAsyncTask(FavoriteRecipeDao mFavoriteRecipeDao) {
            this.mFavoriteRecipeDao = mFavoriteRecipeDao;
        }

        @Override
        protected Void doInBackground(FavoriteRecipe... recipes) {
            mFavoriteRecipeDao.insertFavoriteRecipe(recipes[0]);
            return null;
        }
    }
}
