package com.juancoob.nanodegree.and.vegginner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.juancoob.nanodegree.and.vegginner.data.places.ISearchApiService;
import com.juancoob.nanodegree.and.vegginner.data.places.Place;
import com.juancoob.nanodegree.and.vegginner.data.places.PlaceRepository;
import com.juancoob.nanodegree.and.vegginner.data.places.datasource.PlaceDataSource;
import com.juancoob.nanodegree.and.vegginner.data.places.datasource.factory.PlaceDataSourceFactory;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class PlacesViewModel extends ViewModel {

    private Executor mExecutor;
    private ISearchApiService mSearchApiService;
    private LiveData<NetworkState> mInitialLoading;
    private LiveData<NetworkState> mNetworkState;
    private LiveData<PagedList<Place>> mPlacePagedList;
    private PlaceDataSourceFactory mPlaceDataSourceFactory;
    private PagedList.Config mPagedListConfig;
    private PlaceRepository mPlaceRepository;
    private MutableLiveData<String> mLocation;

    private LiveData<Boolean> mIsReady;


    public PlacesViewModel(ISearchApiService searchApiService, PlaceRepository placeRepository) {
        mSearchApiService = searchApiService;
        mPlaceRepository = placeRepository;
        initializePlaceList();
        loadPlaceData();
    }

    public void initializePlaceList() {

        mExecutor = Executors.newFixedThreadPool(Constants.MAXIMUN_POOL_SIZE);

        mPagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setPageSize(Constants.PLACES_PAGE_SIZE)
                .build();
    }

    public void loadPlaceData() {
        mPlacePagedList = Transformations.switchMap(getLocation(), location -> {

            mPlaceDataSourceFactory = new PlaceDataSourceFactory(mSearchApiService, location, mPlaceRepository.getPlaceType());

            mInitialLoading = Transformations.switchMap(mPlaceDataSourceFactory.getPlaceDataSourceMutableLiveData(),
                    PlaceDataSource::getInitialLoading);

            mNetworkState = Transformations.switchMap(mPlaceDataSourceFactory.getPlaceDataSourceMutableLiveData(),
                    PlaceDataSource::getNetworkState);

            mIsReady = Transformations.switchMap(mPlaceDataSourceFactory.getPlaceDataSourceMutableLiveData(),
                    PlaceDataSource::isPagedListReady);

            return new LivePagedListBuilder<String, Place>(mPlaceDataSourceFactory, mPagedListConfig)
                    .setFetchExecutor(mExecutor)
                    .build();
        });
    }

    public LiveData<NetworkState> getInitialLoading() {
        return mInitialLoading;
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkState;
    }

    public LiveData<PagedList<Place>> getPlacePagedList() {
        return mPlacePagedList;
    }

    public MutableLiveData<String> getLocation() {
        if (mLocation == null) {
            mLocation = new MutableLiveData<>();
        }
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation.setValue(location);
    }

    public LiveData<Boolean> getIsReady() {
        return mIsReady;
    }

}
