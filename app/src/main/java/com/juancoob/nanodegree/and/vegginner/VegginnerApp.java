package com.juancoob.nanodegree.and.vegginner;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.juancoob.nanodegree.and.vegginner.di.DaggerVegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.di.RetrofitModule;
import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppModule;
import com.juancoob.nanodegree.and.vegginner.di.databaseScope.DaggerVegginnerRoomComponent;
import com.juancoob.nanodegree.and.vegginner.di.databaseScope.VegginnerRoomComponent;
import com.juancoob.nanodegree.and.vegginner.di.databaseScope.VegginnerRoomModule;
import com.juancoob.nanodegree.and.vegginner.util.timber.TimberLog;

import io.fabric.sdk.android.Fabric;

/**
 * This is the Application class with initializes Firebase Crashlitycs and Dagger 2 components
 *
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class VegginnerApp extends Application {

    private VegginnerRoomComponent mVegginnerRoomComponent;

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

        mVegginnerRoomComponent = DaggerVegginnerRoomComponent.builder()
                .vegginnerAppComponent(vegginnerAppComponent)
                .vegginnerRoomModule(new VegginnerRoomModule(this))
                .build();
    }

    public VegginnerRoomComponent getVegginnerRoomComponent() {
        return mVegginnerRoomComponent;
    }
}
