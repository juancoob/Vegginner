package com.juancoob.nanodegree.and.vegginner.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeDao;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.Ingredient;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientDao;

/**
 * This abstract class is the main database
 *
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */

@Database(entities = {FavoriteRecipe.class, Ingredient.class}, version = 1, exportSchema = false)
public abstract class VegginnerDatabase extends RoomDatabase {
    public abstract FavoriteRecipeDao favoriteRecipeDao();
    public abstract IngredientDao ingredientDao();
}
