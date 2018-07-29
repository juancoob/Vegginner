package com.juancoob.nanodegree.and.vegginner.di.databaseScope;


import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Room;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeDao;
import com.juancoob.nanodegree.and.vegginner.data.VegginnerDatabase;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
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
    ViewModelProvider.Factory provideViewModelFactory(FavoriteRecipeRepository favoriteRecipeRepository, IRecipeApiService recipeApiService) {
        return new VegginnerViewModelFactory(favoriteRecipeRepository, recipeApiService);
    }

    @Provides
    @RecipeScope
    FavoriteRecipeRepository provideFavoriteRecipeRepository(FavoriteRecipeDao favoriteRecipeDao) {
        return new FavoriteRecipeRepository(favoriteRecipeDao);
    }

    @Provides
    @RecipeScope
    FavoriteRecipeDao provideFavoriteRecipeDao(VegginnerDatabase vegginnerDatabase) {
        return vegginnerDatabase.favoriteRecipeDao();
    }

    @Provides
    @RecipeScope
    VegginnerDatabase provideFavoriteRecipeDatabase() {
        return mVegginnerDatabase;
    }
}
