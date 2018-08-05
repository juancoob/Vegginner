package com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * This interface is the DAO for ingredients to buy
 *
 * Created by Juan Antonio Cobos Obrero on 1/08/18.
 */

@Dao
public interface IngredientDao {

    /**
     * Get all ingredients from the shopping list
     * @return  LiveData ingredient list
     */
    @Query("SELECT * FROM Ingredient")
    LiveData<List<Ingredient>> getIngredientListFromShoppingList();

    /**
     * Get all ingredient names from the shopping list
     * @return  LiveData ingredient list
     */
    @Query("SELECT mIngredientName FROM Ingredient")
    LiveData<List<String>> getIngredientNameListFromShoppingList();

    /**
     * Get all ingredients from the shopping list for the widget
     * @return  Ingredient list
     */
    @Query("SELECT * FROM Ingredient")
    List<Ingredient> getIngredientListForWidgetFromShoppingList();

    /**
     * Insert ingredient
     */
    @Insert(onConflict = REPLACE)
    void insertIngredient(Ingredient ingredient);

    /**
     * Delete an ingredient
     */
    @Delete
    void deleteIngredient(Ingredient ingredient);

    /**
     * Delete an ingredient by its ingredient name
     */
    @Query("DELETE FROM Ingredient WHERE mIngredientName = :ingredientName")
    void deleteIngredientByName(String ingredientName);

    /**
    * Update ingredients as bought or not
    */
    @Query("UPDATE Ingredient SET mIsBought = :isBought WHERE mIngredientId = :ingredientId")
    void updateIngredientFromWidgetShoppingList(int ingredientId, boolean isBought);

    /**
     * Remove all ingredients bought
     */
    @Query("DELETE FROM Ingredient WHERE mIsBought = 1")
    void deleteBoughtIngredientsFromWidgetShoppingList();

}
