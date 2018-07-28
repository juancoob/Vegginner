package com.juancoob.nanodegree.and.vegginner.data.recipes.local;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */


@Dao
public interface FavoriteRecipeDao {

    /**
     * Get all favorite recipes
     * @return  LiveData favorite recipe list
     */
    @Query("SELECT * FROM FavoriteRecipe")
    LiveData<List<FavoriteRecipe>> getFavoriteRecipeList();

    /**
     * Get all favorite recipes by website
     */
    @Query("SELECT mRecipeWeb FROM FavoriteRecipe")
    LiveData<List<String>> getFavoriteRecipeListByWebsite();

    /**
     * Insert a favorite recipe
     */
    @Insert(onConflict = REPLACE)
    void insertFavoriteRecipe(FavoriteRecipe recipe);

    /**
     * Delete a favorite recipe
     */
    @Delete
    void deleteFavoriteRecipe(FavoriteRecipe recipe);

}
