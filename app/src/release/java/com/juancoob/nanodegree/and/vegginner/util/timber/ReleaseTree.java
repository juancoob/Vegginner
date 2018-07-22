package com.juancoob.nanodegree.and.vegginner.util.timber;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class ReleaseTree extends Timber.Tree {
    @Override
    protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable throwable) {
        switch (priority) {
            case Log.VERBOSE:
            case Log.DEBUG:
            case Log.INFO:
                break;
            case Log.WARN:
                logWarning(priority,tag, message);
                break;
            case Log.ERROR:
                logError(throwable);
                break;
            default:
                break;
        }
    }

    private void logWarning(int priority, @Nullable String tag, @NotNull String message) {
        Crashlytics.log(priority, tag, message);
    }

    private void logError(@Nullable Throwable throwable) {
        Crashlytics.logException(throwable);
    }
}
