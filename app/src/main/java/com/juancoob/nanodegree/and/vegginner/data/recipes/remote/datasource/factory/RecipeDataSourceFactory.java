package com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.RecipeDataSource;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipeDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<RecipeDataSource> mRecipeDataSourceMutableLiveData;
    private RecipeDataSource mRecipeDataSource;
    private VegginnerApp mVegginnerApp;

    public RecipeDataSourceFactory(VegginnerApp vegginnerApp) {
        mVegginnerApp = vegginnerApp;
        mRecipeDataSourceMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<RecipeDataSource> getRecipeDataSourceMutableLiveData() {
        return mRecipeDataSourceMutableLiveData;
    }

    @Override
    public DataSource create() {
        mRecipeDataSource = new RecipeDataSource(mVegginnerApp);
        mRecipeDataSourceMutableLiveData.postValue(mRecipeDataSource);
        return mRecipeDataSource;
    }
}
