package com.juancoob.nanodegree.and.vegginner.di;

import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.places.ISearchApiService;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;

import javax.inject.Singleton;

import dagger.Component;

/**
 * This is the Application component which connects the Application modules
 *
 * Created by Juan Antonio Cobos Obrero on 29/07/18.
 */

@Singleton
@Component(modules = {VegginnerAppModule.class, RetrofitModule.class})
public interface VegginnerAppComponent {

    VegginnerApp getVegginnerApp();

    IRecipeApiService getRecipeApiService();

    ISearchApiService getSearchApiService();

}
