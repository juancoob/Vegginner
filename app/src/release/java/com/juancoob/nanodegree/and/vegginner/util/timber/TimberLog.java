package com.juancoob.nanodegree.and.vegginner.util.timber;

import timber.log.Timber;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class TimberLog {

    public static void initTimber() {
        Timber.plant(new ReleaseTree());
    }

}
