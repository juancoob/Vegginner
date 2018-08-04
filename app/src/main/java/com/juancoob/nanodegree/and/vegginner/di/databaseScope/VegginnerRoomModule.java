package com.juancoob.nanodegree.and.vegginner.di.databaseScope;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeDao;
import com.juancoob.nanodegree.and.vegginner.data.VegginnerDatabase;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientDao;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.util.AppExecutors;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * This is the Room module to get the Room and ViewModel factory instances
 *
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */
@Module
public class VegginnerRoomModule {

    private VegginnerDatabase mVegginnerDatabase;

    public VegginnerRoomModule(Application app) {
        mVegginnerDatabase =
                Room.databaseBuilder(app, VegginnerDatabase.class, "VegginnerDatabase.db").build();
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(FavoriteRecipeRepository favoriteRecipeRepository,
                                                      IngredientRepository ingredientRepository,
                                                      IRecipeApiService recipeApiService) {
        return new VegginnerViewModelFactory(favoriteRecipeRepository, ingredientRepository, recipeApiService);
    }

    @Provides
    @RoomScope
    IngredientRepository provideIngredientRepository(IngredientDao ingredientDao, AppExecutors appExecutors) {
        return new IngredientRepository(ingredientDao, appExecutors);
    }

    @Provides
    @RoomScope
    IngredientDao provideIngredientDao(VegginnerDatabase vegginnerDatabase) {
        return vegginnerDatabase.ingredientDao();
    }

    @Provides
    @RoomScope
    FavoriteRecipeRepository provideFavoriteRecipeRepository(FavoriteRecipeDao favoriteRecipeDao, AppExecutors appExecutors) {
        return new FavoriteRecipeRepository(favoriteRecipeDao, appExecutors);
    }

    @Provides
    @RoomScope
    FavoriteRecipeDao provideFavoriteRecipeDao(VegginnerDatabase vegginnerDatabase) {
        return vegginnerDatabase.favoriteRecipeDao();
    }

    @Provides
    @RoomScope
    VegginnerDatabase provideFavoriteRecipeDatabase() {
        return mVegginnerDatabase;
    }
}
