package com.juancoob.nanodegree.and.vegginner.di.databaseScope;

import com.juancoob.nanodegree.and.vegginner.di.VegginnerAppComponent;
import com.juancoob.nanodegree.and.vegginner.ui.recipes.RecipesFragment;

import dagger.Component;

/**
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */

@RecipeScope
@Component(modules = VegginnerRoomModule.class, dependencies = VegginnerAppComponent.class)
public interface VegginnerRoomComponent {

    void injectRecipesSection(RecipesFragment recipesFragment);

}
