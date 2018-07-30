package com.juancoob.nanodegree.and.vegginner.di.databaseScope;

import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.ui.MainActivity;
import com.juancoob.nanodegree.and.vegginner.ui.recipeDetails.RecipeDetailsFragment;
import com.juancoob.nanodegree.and.vegginner.ui.recipeDetails.RecipeIngredientsServingsFragment;
import com.juancoob.nanodegree.and.vegginner.ui.recipeDetails.RecipeStepsFragment;
import com.juancoob.nanodegree.and.vegginner.ui.recipes.RecipesFragment;

import dagger.Component;

/**
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */

@RecipeScope
@Component(modules = VegginnerRoomModule.class, dependencies = VegginnerAppComponent.class)
public interface VegginnerRoomComponent {

    void injectRecipesSection(RecipesFragment recipesFragment);
    void injectRecipeDetailsSection(RecipeDetailsFragment recipeDetailsFragment);
    void injectRecipeIngredientsServingsSection(RecipeIngredientsServingsFragment recipeIngredientsServingsFragment);
    void injectRecipeStepsSection(RecipeStepsFragment recipeStepsFragment);
    void injectMainActivity(MainActivity mainActivity);

}
