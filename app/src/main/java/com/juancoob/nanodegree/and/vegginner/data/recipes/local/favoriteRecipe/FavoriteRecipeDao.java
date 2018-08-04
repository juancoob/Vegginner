package com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * This interface is the DAO for favorite recipes
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */

@Dao
public interface FavoriteRecipeDao {

    /**
     * Get all favorite recipes
     * @return  LiveData favorite recipe list
     */
    @Query("SELECT * FROM FavoriteRecipe")
    DataSource.Factory<Integer, FavoriteRecipe> getFavoriteRecipeList();

    /**
     * Get all favorite recipes by website
     * @return  LiveData favorite recipe list
     */
    @Query("SELECT mRecipeWeb FROM FavoriteRecipe")
    LiveData<List<String>> getFavoriteRecipeListByWebsite();

    /**
     * Get one favorite recipe by website
     * @return  LiveData favorite recipe
     */
    @Query("SELECT * FROM FavoriteRecipe WHERE mRecipeWeb = :recipeWeb")
    LiveData<FavoriteRecipe> getFavoriteRecipeByWebsite(String recipeWeb);

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

    /**
     * Delete a favorite recipe by web
     */
    @Query("DELETE FROM favoriterecipe WHERE mRecipeWeb = :recipeWeb")
    void deleteFavoriteRecipeByWeb(String recipeWeb);

}
