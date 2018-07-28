package com.juancoob.nanodegree.and.vegginner;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.juancoob.nanodegree.and.vegginner.data.RestApiFactory;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.di.recipes.RecipeComponent;
import com.juancoob.nanodegree.and.vegginner.util.timber.TimberLog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class VegginnerApp extends Application{

    //todo dagger 2
    public IRecipeApiService recipeApiService;

    private RecipeComponent mRecipeComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        TimberLog.initTimber();

        // Init Firebase crashlytics
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true) // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);

        // Init Dagger 2 components
        DaggerRecipeComponent

    }

    //todo dagger 2
    public static VegginnerApp get(Context ctx) {
        return (VegginnerApp) ctx.getApplicationContext();
    }

    //todo dagger 2
    public IRecipeApiService getRecipeRestApi() {
        if(recipeApiService == null) {
            recipeApiService = RestApiFactory.createRecipeApi();
        }
        return recipeApiService;
    }

    public RecipeComponent getRecipeComponent() {
        return mRecipeComponent;
    }
}
