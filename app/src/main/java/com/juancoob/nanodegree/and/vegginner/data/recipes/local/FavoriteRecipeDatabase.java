package com.juancoob.nanodegree.and.vegginner.data.recipes.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */

@Database(entities = {FavoriteRecipe.class}, version = 1, exportSchema = false)
public abstract class FavoriteRecipeDatabase extends RoomDatabase {
    public abstract FavoriteRecipeDao favoriteRecipeDao();
}
