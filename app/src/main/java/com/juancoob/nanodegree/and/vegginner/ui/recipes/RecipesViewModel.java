package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.SecondRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.RecipeDataSource;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.factory.RecipeDataSourceFactory;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipesViewModel extends ViewModel {

    //todo dagger 2
    private Executor mExecutor;
    private LiveData<NetworkState> mNetworkStateLiveData;
    private LiveData<NetworkState> mInitialLoadingLiveData;
    private LiveData<PagedList<SecondRecipeResponse>> mSecondRecipeResponseLiveData;
    //todo dagger 2
    private VegginnerApp mVegginnerApp;
    private RecipeDataSourceFactory mRecipeDataSourceFactory;

    public RecipesViewModel(VegginnerApp vegginnerApp) {
        mVegginnerApp = vegginnerApp;

        mExecutor = Executors.newFixedThreadPool(Constants.MAXIMUN_POOL_SIZE);

        mRecipeDataSourceFactory = new RecipeDataSourceFactory(mVegginnerApp);

        mNetworkStateLiveData = Transformations.switchMap(mRecipeDataSourceFactory.getRecipeDataSourceMutableLiveData(),
                RecipeDataSource::getNetworkState);

        mInitialLoadingLiveData = Transformations.switchMap(mRecipeDataSourceFactory.getRecipeDataSourceMutableLiveData(),
                RecipeDataSource::getInitialLoading);

        getData();
    }

    private void getData() {
        PagedList.Config pagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(Constants.INITIAL_LOAD)
                .setPageSize(Constants.ELEMENTS_BY_PAGE)
                .build();

        mSecondRecipeResponseLiveData = (new LivePagedListBuilder<Long, SecondRecipeResponse>(mRecipeDataSourceFactory, pagedListConfig))
                .setFetchExecutor(mExecutor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkStateLiveData;
    }

    public LiveData<NetworkState> getInitialLoadingLiveData() {
        return mInitialLoadingLiveData;
    }

    public LiveData<PagedList<SecondRecipeResponse>> getSecondRecipeResponseLiveData() {
        return mSecondRecipeResponseLiveData;
    }

    public LiveData<PagedList<SecondRecipeResponse>> getAgainSecondRecipeResponseLiveData() {
        getData();
        return mSecondRecipeResponseLiveData;
    }
}
