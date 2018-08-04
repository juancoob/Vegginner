package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

/**
 * Created by Juan Antonio Cobos Obrero on 2/08/18.
 */
public interface ISelectedIngredientToListCallback {
    void addIngredientToTheShoppingList(String ingredientName);
    void removeIngredientFromTheShoppingList(String ingredientName);
}
