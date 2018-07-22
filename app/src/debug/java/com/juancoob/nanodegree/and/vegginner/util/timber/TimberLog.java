package com.juancoob.nanodegree.and.vegginner.util.timber;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import timber.log.Timber;

/**
 * Created by Juan Antonio Cobos Obrero on 21/07/18.
 */
public class TimberLog {

    public static void initTimber() {
        Timber.plant(new Timber.DebugTree() {
            @Override
            protected @Nullable String createStackElementTag(@NotNull StackTraceElement element) {
                return String.format("Class %s on %s at line %s ",
                        super.createStackElementTag(element), element.getMethodName(), element.getLineNumber());

            }
        });
    }

}