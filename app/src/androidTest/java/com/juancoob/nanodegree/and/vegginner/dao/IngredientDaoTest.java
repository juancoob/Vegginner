package com.juancoob.nanodegree.and.vegginner.dao;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.juancoob.nanodegree.and.vegginner.LiveDataTestUtil;
import com.juancoob.nanodegree.and.vegginner.data.VegginnerDatabase;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.Ingredient;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientDao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;

import timber.log.Timber;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

/**
 * Test class to test the ingredient dao
 *
 * Created by Juan Antonio Cobos Obrero on 6/08/18.
 */

@RunWith(AndroidJUnit4.class)
public class IngredientDaoTest {

    private VegginnerDatabase mVegginnerDatabase;
    private IngredientDao mIngredientDao;

    @Before
    public void createDatabase() {
        Context context = InstrumentationRegistry.getTargetContext();
        mVegginnerDatabase = Room.inMemoryDatabaseBuilder(context, VegginnerDatabase.class).build();
        mIngredientDao = mVegginnerDatabase.ingredientDao();
    }

    @After
    public void closeDatabase() throws IOException{
        mVegginnerDatabase.close();
    }

    @Test
    public void insertAnIngredientAndGetItFromListToCheckIfItIsTheSame() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        List<Ingredient> ingredientList = null;
        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientList.get(0).getIngredientName(), equalTo(ingredient.getIngredientName()));
        assertThat(ingredientList.get(0).isBought(), equalTo(ingredient.isBought()));
    }

    @Test
    public void insertAnIngredientAndGetItFromListToCheckIfItIsTheSameName() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        List<String> ingredientNameList = null;
        try {
            ingredientNameList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientNameListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientNameList.get(0), equalTo(ingredient.getIngredientName()));
    }

    @Test
    public void insertAnIngredientAndGetItFromWidgetListToCheckIfItIsTheSame() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        List<Ingredient> ingredientList = mIngredientDao.getIngredientListForWidgetFromShoppingList();
        assertThat(ingredientList.get(0).getIngredientName(), equalTo(ingredient.getIngredientName()));
        assertThat(ingredientList.get(0).isBought(), equalTo(ingredient.isBought()));
    }

    @Test
    public void insertAndDeleteAnIngredientAndCheckIfItIsDeleted() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        List<Ingredient> ingredientList = null;
        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        // Get the correct id to delete the ingredient
        mIngredientDao.deleteIngredient(ingredientList.get(0));

        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientList, is(empty()));
    }

    @Test
    public void insertAndDeleteAnIngredientByNameAndCheckIfItIsDeleted() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        mIngredientDao.deleteIngredientByName(ingredient.getIngredientName());
        List<Ingredient> ingredientList = null;
        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientList, is(empty()));
    }

    @Test
    public void insertAndDeleteAnIngredientFromWidgetListIfItIsBoughtAndCheckIfItIsDeleted() {
        Ingredient ingredient = new Ingredient("1 Carrot", true);
        mIngredientDao.insertIngredient(ingredient);
        mIngredientDao.deleteBoughtIngredientsFromWidgetShoppingList();
        List<Ingredient> ingredientList = null;
        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientList, is(empty()));
    }

    @Test
    public void insertAndUpdateAnIngredientFromWidgetAndCheckIfItIsUpdated() {
        Ingredient ingredient = new Ingredient("1 Carrot", false);
        mIngredientDao.insertIngredient(ingredient);
        List<Ingredient> ingredientList = null;
        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        // Get the correct id to update the ingredient
        mIngredientDao.updateIngredientFromWidgetShoppingList(ingredientList.get(0).getIngredientId(), true);

        try {
            ingredientList = LiveDataTestUtil.getValue(mIngredientDao.getIngredientListFromShoppingList());
        } catch (InterruptedException exception) {
            Timber.e(exception);
        }
        assertThat(ingredientList.get(0).isBought(), is(true));
    }

}
