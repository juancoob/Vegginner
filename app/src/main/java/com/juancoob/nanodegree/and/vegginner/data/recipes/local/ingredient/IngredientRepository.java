package com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient;

import android.arch.lifecycle.LiveData;

import com.juancoob.nanodegree.and.vegginner.util.AppExecutors;

import java.util.List;

import javax.inject.Inject;

/**
 * This is the ingredient repository to get the data using the IngredientDao
 * <p>
 * Created by Juan Antonio Cobos Obrero on 1/08/18.
 */
public class IngredientRepository {

    private final IngredientDao mIngredientDao;
    private final AppExecutors mAppExecutors;

    @Inject
    public IngredientRepository(IngredientDao ingredientDao, AppExecutors appExecutors) {
        mIngredientDao = ingredientDao;
        mAppExecutors = appExecutors;
    }

    public LiveData<List<Ingredient>> getIngredientList() {
        return mIngredientDao.getIngredientList();
    }

    public LiveData<List<String>> getIngredientNameList() {
        return mIngredientDao.getIngredientNameList();
    }

    public List<Ingredient> getIngredientListWidget() {
        return mIngredientDao.getIngredientListWidget();
    }

    public void insertIngredient(Ingredient ingredient) {
        mAppExecutors.getDiskIO().execute(() -> mIngredientDao.insertIngredient(ingredient));
    }

    public LiveData<Ingredient> getIngredient(String ingredientName) {
        return mIngredientDao.getIngredient(ingredientName);
    }

    public void deleteIngredientByName(String ingredientName) {
        mAppExecutors.getDiskIO().execute(() -> mIngredientDao.deleteIngredientByName(ingredientName));
    }

    public void deleteAllIngredientsBought(List<Ingredient> ingredientList) {
        mAppExecutors.getDiskIO().execute(() -> {
            for (Ingredient ingredient : ingredientList) {
                mIngredientDao.deleteIngredient(ingredient);
            }
        });
    }

    public void updateIngredient(Ingredient ingredient, boolean isBought) {
        mAppExecutors.getDiskIO().execute(() ->
                mIngredientDao.updateIngredient(isBought, ingredient.getIngredientName()));
    }
}
