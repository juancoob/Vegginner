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

    public LiveData<List<Ingredient>> getIngredientListFromShoppingList() {
        return mIngredientDao.getIngredientListFromShoppingList();
    }

    public LiveData<List<String>> getIngredientNameListFromShoppingList() {
        return mIngredientDao.getIngredientNameListFromShoppingList();
    }

    public List<Ingredient> getIngredientListForWidgetFromShoppingList() {
        return mIngredientDao.getIngredientListForWidgetFromShoppingList();
    }

    public void insertIngredient(Ingredient ingredient) {
        mAppExecutors.getDiskIO().execute(() -> mIngredientDao.insertIngredient(ingredient));
    }

    public void deleteIngredientByName(String ingredientName) {
        mAppExecutors.getDiskIO().execute(() -> mIngredientDao.deleteIngredientByName(ingredientName));
    }

    public void updateIngredientFromWidgetShoppingList(int ingredientId, boolean isBought) {
        mAppExecutors.getDiskIO().execute(() ->
                mIngredientDao.updateIngredientFromWidgetShoppingList(ingredientId, isBought));
    }

    public void deleteBoughtIngredientsFromWidgetShoppingList() {
        mAppExecutors.getDiskIO().execute(mIngredientDao::deleteBoughtIngredientsFromWidgetShoppingList);
    }
}
