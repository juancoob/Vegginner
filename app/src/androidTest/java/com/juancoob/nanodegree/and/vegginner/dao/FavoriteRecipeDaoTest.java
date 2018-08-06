package com.juancoob.nanodegree.and.vegginner.dao;

import android.arch.lifecycle.LiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.juancoob.nanodegree.and.vegginner.LiveDataTestUtil;
import com.juancoob.nanodegree.and.vegginner.data.VegginnerDatabase;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import timber.log.Timber;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test class to test the favorite recipe dao
 *
 * Created by Juan Antonio Cobos Obrero on 5/08/18.
 */

@RunWith(AndroidJUnit4.class)
public class FavoriteRecipeDaoTest {

    private VegginnerDatabase mVegginnerDatabase;
    private FavoriteRecipeDao mFavoriteRecipeDao;

    @Before
    public void createDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        mVegginnerDatabase = Room.inMemoryDatabaseBuilder(context, VegginnerDatabase.class).build();
        mFavoriteRecipeDao = mVegginnerDatabase.favoriteRecipeDao();
    }

    @After
    public void closeDatabase() throws IOException {
        mVegginnerDatabase.close();
    }

    @Test
    public void createFavoriteRecipeAndReadInList() {
        List<String> ingredientList = Arrays.asList("1 1/2 cup flour", "1/2 cup cocoa powder", "1/2 teaspoon baking soda", "1 pinch salt");
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe("Chocolate cake", "www.myImage.com/image.jpg", "Chocolate fan!", "www.websitewhichcontainstherecipe.com/recipe", 3, ingredientList);
        mFavoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe);
        List<String> favoriteRecipeListByWebsite = new ArrayList<>();
        try {
            favoriteRecipeListByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeListByWebsite());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(favoriteRecipeListByWebsite.get(0), equalTo(favoriteRecipe.getRecipeWeb()));
    }

    @Test
    public void createFavoriteRecipeAndReadInListToCheckIfItIsTheSame() {
        List<String> ingredientList = Arrays.asList("1 1/2 cup flour", "1/2 cup cocoa powder", "1/2 teaspoon baking soda", "1 pinch salt");
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe("Chocolate cake", "www.myImage.com/image.jpg", "Chocolate fan!", "www.websitewhichcontainstherecipe.com/recipe", 3, ingredientList);
        mFavoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe);
        FavoriteRecipe favoriteRecipeByWebsite = null;
        try {
            favoriteRecipeByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeByWebsite(favoriteRecipe.getRecipeWeb()));
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(favoriteRecipeByWebsite.getRecipeWeb(), equalTo(favoriteRecipe.getRecipeWeb()));
        assertThat(favoriteRecipeByWebsite.getIngredientList(), equalTo(favoriteRecipe.getIngredientList()));
        assertThat(favoriteRecipeByWebsite.getRecipeAuthor(), equalTo(favoriteRecipe.getRecipeAuthor()));
        assertThat(favoriteRecipeByWebsite.getRecipeImage(), equalTo(favoriteRecipe.getRecipeImage()));
        assertThat(favoriteRecipeByWebsite.getRecipeName(), equalTo(favoriteRecipe.getRecipeName()));
        assertThat(favoriteRecipeByWebsite.getRecipeServings(), equalTo(favoriteRecipe.getRecipeServings()));
    }

    @Test
    public void createFavoriteRecipeGetItAddToTheListAndCheckIfItIsTheSame() {

        List<String> ingredientList = Arrays.asList("1 1/2 cup flour", "1/2 cup cocoa powder", "1/2 teaspoon baking soda", "1 pinch salt");
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe("Chocolate cake", "www.myImage.com/image.jpg", "Chocolate fan!", "www.websitewhichcontainstherecipe.com/recipe", 3, ingredientList);
        mFavoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe);
        FavoriteRecipe favoriteRecipeByWebsite = null;
        try {
            favoriteRecipeByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeByWebsite(favoriteRecipe.getRecipeWeb()));
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }

        PagedList.Config pagedListConfig = (new PagedList.Config.Builder()).setPageSize(1).build();
        LiveData<PagedList<FavoriteRecipe>> favoriteRecipePagedList = new LivePagedListBuilder<>(mFavoriteRecipeDao.getFavoriteRecipeList(), pagedListConfig).build();
        PagedList<FavoriteRecipe> favoriteRecipesPagedList = null;
        try {
            favoriteRecipesPagedList = LiveDataTestUtil.getValue(favoriteRecipePagedList);
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }

        assertThat(favoriteRecipesPagedList, hasItem(favoriteRecipeByWebsite));
    }

    @Test
    public void createAndDeleteFavoriteRecipeAndCheckIfItIsDeleted() {
        List<String> ingredientList = Arrays.asList("1 1/2 cup flour", "1/2 cup cocoa powder", "1/2 teaspoon baking soda", "1 pinch salt");
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe("Chocolate cake", "www.myImage.com/image.jpg", "Chocolate fan!", "www.websitewhichcontainstherecipe.com/recipe", 3, ingredientList);
        mFavoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe);
        FavoriteRecipe favoriteRecipeByWebsite = null;
        try {
            favoriteRecipeByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeByWebsite(favoriteRecipe.getRecipeWeb()));
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        // I need the correct id to delete the same FavoriteRecipe previously created
        mFavoriteRecipeDao.deleteFavoriteRecipe(favoriteRecipeByWebsite);
        try {
            favoriteRecipeByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeByWebsite(favoriteRecipe.getRecipeWeb()));
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertNull(favoriteRecipeByWebsite);
    }

    @Test
    public void createAndDeleteFavoriteRecipeByWebsiteAndCheckIfItIsDeleted() {
        List<String> ingredientList = Arrays.asList("1 1/2 cup flour", "1/2 cup cocoa powder", "1/2 teaspoon baking soda", "1 pinch salt");
        FavoriteRecipe favoriteRecipe = new FavoriteRecipe("Chocolate cake", "www.myImage.com/image.jpg", "Chocolate fan!", "www.websitewhichcontainstherecipe.com/recipe", 3, ingredientList);
        mFavoriteRecipeDao.insertFavoriteRecipe(favoriteRecipe);
        mFavoriteRecipeDao.deleteFavoriteRecipeByWeb(favoriteRecipe.getRecipeWeb());
        FavoriteRecipe favoriteRecipeByWebsite = null;
        try {
            favoriteRecipeByWebsite = LiveDataTestUtil.getValue(mFavoriteRecipeDao.getFavoriteRecipeByWebsite(favoriteRecipe.getRecipeWeb()));
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertNull(favoriteRecipeByWebsite);
    }

}
