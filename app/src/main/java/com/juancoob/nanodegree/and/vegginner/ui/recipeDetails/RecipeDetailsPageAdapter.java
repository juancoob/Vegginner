package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

/**
 * This class is the viewpager's adapter to show recipe details
 *
 * Created by Juan Antonio Cobos Obrero on 31/07/18.
 */
public class RecipeDetailsPageAdapter extends FragmentStatePagerAdapter {

    private Context mCtx;
    private Recipe mRecipe;

    public RecipeDetailsPageAdapter(FragmentManager fm, Context ctx, Recipe recipe) {
        super(fm);
        mCtx = ctx;
        mRecipe = recipe;
    }

    @Override
    public Fragment getItem(int position) {
        return position == 0 ? RecipeIngredientsServingsFragment.getInstance(mRecipe) : RecipeStepsFragment.getInstance(mRecipe);
    }

    @Override
    public int getCount() {
        return Constants.NUM_RECIPE_DETAIL_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? mCtx.getString(R.string.details) : mCtx.getString(R.string.steps);
    }
}
