package com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.RecipeDataSource;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipeDataSourceFactory extends DataSource.Factory {

    private IRecipeApiService mRecipeApiService;
    private MutableLiveData<RecipeDataSource> mRecipeDataSourceMutableLiveData;

    public RecipeDataSourceFactory(IRecipeApiService recipeApiService) {
        mRecipeApiService = recipeApiService;
        mRecipeDataSourceMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RecipeDataSource> getRecipeDataSourceMutableLiveData() {
        return mRecipeDataSourceMutableLiveData;
    }

    @Override
    public DataSource create() {
        RecipeDataSource recipeDataSource = new RecipeDataSource(mRecipeApiService);
        mRecipeDataSourceMutableLiveData.postValue(recipeDataSource);
        return recipeDataSource;
    }
}
