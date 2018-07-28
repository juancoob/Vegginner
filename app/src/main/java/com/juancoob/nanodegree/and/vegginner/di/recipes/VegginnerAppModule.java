package com.juancoob.nanodegree.and.vegginner.di.recipes;

import android.app.Application;

import com.juancoob.nanodegree.and.vegginner.VegginnerApp;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */

@Module
public class VegginnerAppModule {

    private VegginnerApp mVegginnerApp;

    public VegginnerAppModule(VegginnerApp mVegginnerApp) {
        this.mVegginnerApp = mVegginnerApp;
    }

    @Provides
    Application provideApplication() {
        return mVegginnerApp;
    }

}
