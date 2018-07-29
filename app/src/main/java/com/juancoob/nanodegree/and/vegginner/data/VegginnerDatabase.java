package com.juancoob.nanodegree.and.vegginner.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeDao;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */

@Database(entities = {FavoriteRecipe.class}, version = 1, exportSchema = false)
public abstract class VegginnerDatabase extends RoomDatabase {
    public abstract FavoriteRecipeDao favoriteRecipeDao();
}
