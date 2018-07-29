package com.juancoob.nanodegree.and.vegginner.di.recipes;


import android.app.Application;
import android.arch.persistence.room.Room;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeDao;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeDatabase;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Juan Antonio Cobos Obrero on 28/07/18.
 */
@Module
public class RecipesRoomModule {

    private FavoriteRecipeDatabase mFavoriteRecipeDatabase;

    public RecipesRoomModule(Application app) {
        mFavoriteRecipeDatabase =
                Room.databaseBuilder(app, FavoriteRecipeDatabase.class, "FavoriteRecipe.db").build();
    }

    @Provides
    @RecipeScope
    RecipesViewModel provideRecipesViewModel(FavoriteRecipeRepository favoriteRecipeRepository, IRecipeApiService recipeApiService) {
        return new RecipesViewModel(favoriteRecipeRepository, recipeApiService);
    }

    @Provides
    @RecipeScope
    FavoriteRecipeRepository provideFavoriteRecipeRepository(FavoriteRecipeDao favoriteRecipeDao) {
        return new FavoriteRecipeRepository(favoriteRecipeDao);
    }

    @Provides
    @RecipeScope
    FavoriteRecipeDao provideFavoriteRecipeDao(FavoriteRecipeDatabase favoriteRecipeDatabase) {
        return favoriteRecipeDatabase.favoriteRecipeDao();
    }

    @Provides
    @RecipeScope
    FavoriteRecipeDatabase provideFavoriteRecipeDatabase() {
        return mFavoriteRecipeDatabase;
    }
}
