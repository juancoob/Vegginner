package com.juancoob.nanodegree.and.vegginner.di;

import com.juancoob.nanodegree.and.vegginner.VegginnerApp;

import dagger.Module;
import dagger.Provides;

/**
 * This is the Application module to get the Application instance
 *
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */

@Module
public class VegginnerAppModule {

    private VegginnerApp mVegginnerApp;

    public VegginnerAppModule(VegginnerApp mVegginnerApp) {
        this.mVegginnerApp = mVegginnerApp;
    }

    @Provides
    VegginnerApp provideApplication() {
        return mVegginnerApp;
    }

}
