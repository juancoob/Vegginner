package com.juancoob.nanodegree.and.vegginner.data.places.datasource.factory;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.juancoob.nanodegree.and.vegginner.data.places.ISearchApiService;
import com.juancoob.nanodegree.and.vegginner.data.places.datasource.PlaceDataSource;

/**
 * This is the data source factory to create place data sources wrapping it on a MutableLiveData
 *
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class PlaceDataSourceFactory extends DataSource.Factory{

    private ISearchApiService mSearchApiService;
    private String mLocation;
    private String mType;
    private MutableLiveData<PlaceDataSource> mPlaceDataSourceMutableLiveData;

    public PlaceDataSourceFactory(ISearchApiService searchApiService, String location, String type) {
        mSearchApiService = searchApiService;
        mLocation = location;
        mType = type;
        mPlaceDataSourceMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<PlaceDataSource> getPlaceDataSourceMutableLiveData() {
        return mPlaceDataSourceMutableLiveData;
    }

    @Override
    public DataSource create() {
        PlaceDataSource placeDataSource = new PlaceDataSource(mSearchApiService, mLocation, mType);
        mPlaceDataSourceMutableLiveData.postValue(placeDataSource);
        return placeDataSource;
    }
}
