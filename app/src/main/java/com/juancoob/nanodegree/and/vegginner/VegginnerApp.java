package com.juancoob.nanodegree.and.vegginner;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.juancoob.nanodegree.and.vegginner.di.DaggerVegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.di.RetrofitModule;
import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppModule;
import com.juancoob.nanodegree.and.vegginner.di.recipes.DaggerRecipeComponent;
import com.juancoob.nanodegree.and.vegginner.di.recipes.RecipeComponent;
import com.juancoob.nanodegree.and.vegginner.di.recipes.RecipesRoomModule;
import com.juancoob.nanodegree.and.vegginner.util.timber.TimberLog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class VegginnerApp extends Application {

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
        VegginnerAppComponent vegginnerAppComponent = DaggerVegginnerAppComponent.builder()
                .vegginnerAppModule(new VegginnerAppModule(this))
                .retrofitModule(new RetrofitModule())
                .build();

        mRecipeComponent = DaggerRecipeComponent.builder()
                .vegginnerAppComponent(vegginnerAppComponent)
                .recipesRoomModule(new RecipesRoomModule(this))
                .build();
    }

    public RecipeComponent getRecipeComponent() {
        return mRecipeComponent;
    }
}
