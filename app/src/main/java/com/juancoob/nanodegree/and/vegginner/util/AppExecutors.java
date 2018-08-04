package com.juancoob.nanodegree.and.vegginner.util;

import com.juancoob.nanodegree.and.vegginner.di.databaseScope.RoomScope;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Inject;

/**
 * This class is the global executor pools for the whole app (Based on GithubBrowser example)
 *
 * Created by Juan Antonio Cobos Obrero on 2/08/18.
 */

@RoomScope
public class AppExecutors {

    private final Executor mDiskIO;

    public AppExecutors(Executor mDiskIO) {
        this.mDiskIO = mDiskIO;
    }

    @Inject
    public AppExecutors() {
        this(Executors.newSingleThreadExecutor());
    }

    public Executor getDiskIO() {
        return mDiskIO;
    }
}
