package com.juancoob.nanodegree.and.vegginner;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.juancoob.nanodegree.and.vegginner.util.timber.TimberLog;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class VegginnerApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        TimberLog.initTimber();
        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true) // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);
    }
}
