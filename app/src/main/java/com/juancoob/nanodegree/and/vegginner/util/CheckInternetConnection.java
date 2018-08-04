package com.juancoob.nanodegree.and.vegginner.util;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.juancoob.nanodegree.and.vegginner.util.callbacks.IAlertDialogCallback;

/**
 * This class helps to show if the internet connection is available or not, and show a dialog to notify it
 *
 * Created by Juan Antonio Cobos Obrero on 1/08/18.
 */
public class CheckInternetConnection {

    public static boolean isConnected(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnectedOrConnecting();
        } else {
            return false;
        }
    }

    public static void showDialog(IAlertDialogCallback callback, Context ctx, int titleId, int messageId, int positiveTextId, int negativeTextId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(titleId)
                .setMessage(messageId)
                .setPositiveButton(positiveTextId, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    callback.showPositiveResult();
                })
                .setNegativeButton(negativeTextId, (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    callback.showNegativeResult();
                })
                .setCancelable(false)
                .show();

    }
}
