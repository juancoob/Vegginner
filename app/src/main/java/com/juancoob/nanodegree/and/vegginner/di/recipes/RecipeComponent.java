package com.juancoob.nanodegree.and.vegginner.di.recipes;

import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.ui.recipes.RecipesFragment;

import dagger.Component;

/**
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */

@RecipeScope
@Component(modules = RecipesRoomModule.class, dependencies = VegginnerAppComponent.class)
public interface RecipeComponent {

    void injectRecipesSection(RecipesFragment recipesFragment);

}
