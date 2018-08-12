package com.juancoob.nanodegree.and.vegginner.data.places.datasource;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.ItemKeyedDataSource;
import android.support.annotation.NonNull;

import com.juancoob.nanodegree.and.vegginner.BuildConfig;
import com.juancoob.nanodegree.and.vegginner.data.places.FirstPlaceResponse;
import com.juancoob.nanodegree.and.vegginner.data.places.ISearchApiService;
import com.juancoob.nanodegree.and.vegginner.data.places.Place;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

/**
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class PlaceDataSource extends ItemKeyedDataSource<String, Place> {

    private ISearchApiService mSearchApiService;
    private MutableLiveData<NetworkState> mNetworkState;
    private MutableLiveData<NetworkState> mInitialLoading;
    private String mLocation;
    private String mType;

    private MutableLiveData<Boolean> mIsPagedListReady;

    public PlaceDataSource(ISearchApiService searchApiService, String location, String type) {
        mSearchApiService = searchApiService;
        mLocation = location;
        mType = type;
        mNetworkState = new MutableLiveData<>();
        mInitialLoading = new MutableLiveData<>();

        mIsPagedListReady = new MutableLiveData<>();
    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public MutableLiveData<NetworkState> getInitialLoading() {
        return mInitialLoading;
    }

    public MutableLiveData<Boolean> isPagedListReady() {
        return mIsPagedListReady;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull LoadInitialCallback<Place> callback) {

        mNetworkState.postValue(NetworkState.LOADING);
        mInitialLoading.postValue(NetworkState.LOADING);

        mSearchApiService.getFirstPlaceResponse(mLocation, Constants.BASE_RADIUS, mType, Constants.KEYWORD, BuildConfig.PLACE_API_KEY)
                .enqueue(new Callback<FirstPlaceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FirstPlaceResponse> call, @NonNull Response<FirstPlaceResponse> response) {
                        if (response.isSuccessful() && response.code() == Constants.SUCCESS_RESPONSE) {
                            callback.onResult(response.body().getPlaceList());
                            mNetworkState.postValue(NetworkState.LOADED);
                            mInitialLoading.postValue(NetworkState.LOADED);
                            mIsPagedListReady.postValue(true);
                        } else {
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            Timber.w(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstPlaceResponse> call, @NonNull Throwable throwable) {
                        // No internet connection or service failed
                        mInitialLoading.postValue(new NetworkState(NetworkState.Status.FAILED, throwable.getMessage()));
                        Timber.e(throwable);
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<Place> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<Place> callback) {

        mNetworkState.postValue(NetworkState.LOADING);

        mSearchApiService.getFirstPlaceResponse(mLocation, Constants.BASE_RADIUS, mType, Constants.KEYWORD, BuildConfig.PLACE_API_KEY)
                .enqueue(new Callback<FirstPlaceResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<FirstPlaceResponse> call, @NonNull Response<FirstPlaceResponse> response) {
                        if (response.isSuccessful() && response.code() == Constants.SUCCESS_RESPONSE) {
                            callback.onResult(response.body().getPlaceList());
                            mNetworkState.postValue(NetworkState.LOADED);
                        } else {
                            mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, response.message()));
                            Timber.w(response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<FirstPlaceResponse> call, @NonNull Throwable throwable) {
                        // No internet connection or service failed
                        mNetworkState.postValue(new NetworkState(NetworkState.Status.FAILED, throwable.getMessage()));
                        Timber.e(throwable);
                    }
                });
    }

    @NonNull
    @Override
    public String getKey(@NonNull Place item) {
        return item.getPlaceId();
    }
}
