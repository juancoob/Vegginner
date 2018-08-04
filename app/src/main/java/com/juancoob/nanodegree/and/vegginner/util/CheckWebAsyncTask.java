package com.juancoob.nanodegree.and.vegginner.util;

import android.os.AsyncTask;

import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.callbacks.ICheckWebCallback;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

/**
 * This class gets the web page's HTML
 *
 * Created by Juan Antonio Cobos Obrero on 1/08/18.
 */
public class CheckWebAsyncTask extends AsyncTask<Recipe, Void, Boolean> {

    private ICheckWebCallback mCheckWebCallback;

    public CheckWebAsyncTask(ICheckWebCallback checkWebCallback) {
        mCheckWebCallback = checkWebCallback;
    }

    @Override
    protected Boolean doInBackground(Recipe... data) {
        try {
            Document doc = Jsoup.connect(data[0].getRecipeWeb()).get();
            List<String> nameParts = Arrays.asList(data[0].getRecipeName().split(" "));
            for(String part : nameParts) {
                if(doc.body().text().contains(part)) {
                    return true;
                }
            }
        } catch (IOException e) {
            Timber.e(e);
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        mCheckWebCallback.containsSteps(result);
    }
}
