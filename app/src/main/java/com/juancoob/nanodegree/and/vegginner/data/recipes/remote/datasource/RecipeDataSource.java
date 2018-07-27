package com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.juancoob.nanodegree.and.vegginner.BuildConfig;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.FirstRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.SecondRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipeDataSource extends PageKeyedDataSource<Long, SecondRecipeResponse> {

    private VegginnerApp mVegginnerApp;
    private MutableLiveData<NetworkState> mNetworkState;
    private MutableLiveData<NetworkState> mInitialLoading;

    public RecipeDataSource(VegginnerApp vegginnerApp) {
        this.mVegginnerApp = vegginnerApp;
        mNetworkState = new MutableLiveData<>();
        mInitialLoading = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return mInitialLoading;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Long> params, @NonNull final LoadInitialCallback<Long, SecondRecipeResponse> callback) {

        mNetworkState.postValue(NetworkState.LOADING);
        mInitialLoading.postValue(NetworkState.LOADING);

        Timber.d("0 elements of %s loaded", params.requestedLoadSize);

        mVegginnerApp.getRecipeRestApi().getFirstRecipeResponse(Constants.QUERY_URL_RECIPE,
                BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_APP_KEY, Constants.EXCLUDED_RECIPE, (long) Constants.INITIAL_ELEMENT, Constants.ELEMENTS_BY_PAGE)
                .enqueue(new Callback<FirstRecipeResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FirstRecipeResponse> call, @NonNull Response<FirstRecipeResponse> response) {
                        if (response.isSuccessful() && response.code() == Constants.SUCCESS_RESPONSE) {
                            callback.onResult(response.body().getHitList(), (long) Constants.INITIAL_ELEMENT, (long) Constants.ELEMENTS_BY_PAGE);
                            mNetworkState.postValue(NetworkState.LOADED);
                            mInitialLoading.postValue(NetworkState.LOADED);
                        } else {
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstRecipeResponse> call, @NonNull Throwable throwable) {
                        // No internet connection
                        mInitialLoading.postValue(new NetworkState(NetworkState.Status.NO_INTERNET, throwable.getMessage()));
                    }
                });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Long> params, @NonNull LoadCallback<Long, SecondRecipeResponse> callback) {

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Long> params, @NonNull final LoadCallback<Long, SecondRecipeResponse> callback) {

        Timber.d("%s elements of %s loaded", params.key, params.key.intValue() + Constants.ELEMENTS_BY_PAGE);

        mNetworkState.postValue(NetworkState.LOADING);

        mVegginnerApp.getRecipeRestApi().getFirstRecipeResponse(Constants.QUERY_URL_RECIPE,
                BuildConfig.EDAMAM_APP_ID, BuildConfig.EDAMAM_APP_KEY, Constants.EXCLUDED_RECIPE, params.key, params.key.intValue() + Constants.ELEMENTS_BY_PAGE)
                .enqueue(new Callback<FirstRecipeResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FirstRecipeResponse> call, @NonNull Response<FirstRecipeResponse> response) {
                        if (response.isSuccessful() && response.code() == Constants.SUCCESS_RESPONSE) {
                            Long nextKey = params.key == Constants.EDAMAM_API_MAX_RESULTS ? null : params.key + Constants.ELEMENTS_BY_PAGE;
                            callback.onResult(response.body().getHitList(), nextKey);
                            mNetworkState.postValue(NetworkState.LOADED);
                        } else {
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstRecipeResponse> call, @NonNull Throwable throwable) {
                        // No internet connection
                        mNetworkState.postValue(new NetworkState(NetworkState.Status.NO_INTERNET, throwable.getMessage()));
                    }
                });
    }
}
