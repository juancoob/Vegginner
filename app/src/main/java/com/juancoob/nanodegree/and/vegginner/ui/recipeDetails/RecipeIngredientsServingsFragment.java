package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.Ingredient;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * This fragment shows the recipe's ingredients and servings
 * <p>
 * Created by Juan Antonio Cobos Obrero on 30/07/18.
 */
public class RecipeIngredientsServingsFragment extends Fragment implements ISelectedIngredientToListCallback {

    @BindView(R.id.tv_servings)
    public TextView servingsTextView;

    @BindView(R.id.rv_ingredients)
    public RecyclerView ingredientsRecyclerView;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    private RecipesViewModel mRecipesViewModel;

    private Recipe mRecipe;
    private Context mCtx;
    private RecyclerView.Adapter mAdapter;
    private List<String> mIngredientsFromShoppingList = new ArrayList<>();
    private Ingredient mIngredient;
    private LinearLayoutManager mIngredientLinearLayoutManager;
    private Parcelable mCurrentIngredientRecyclerViewState;

    public RecipeIngredientsServingsFragment() {
        // Empty constructor
    }

    public static RecipeIngredientsServingsFragment getInstance(Recipe recipe) {
        RecipeIngredientsServingsFragment fragment = new RecipeIngredientsServingsFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.RECIPE, recipe);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) mCtx.getApplicationContext()).getRecipeComponent().injectRecipeIngredientsServingsFragment(this);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mRecipe = bundle.getParcelable(Constants.RECIPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_ingredients_servings, container, false);
        ButterKnife.bind(this, view);
        if (mRecipe != null) {
            servingsTextView.setText(String.valueOf(mRecipe.getRecipeServings()));
            mRecipesViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), vegginnerViewModelFactory).get(RecipesViewModel.class);
            initRecyclerView(mRecipe.getIngredientList(), mIngredientsFromShoppingList);
            mRecipesViewModel.getIngredientNameList().observe(Objects.requireNonNull(getActivity()), ingredientsFromShoppingList -> {
                if (ingredientsFromShoppingList != null) {
                    mIngredientsFromShoppingList.clear();
                    mIngredientsFromShoppingList.addAll(ingredientsFromShoppingList);
                }
                ((RecipeIngredientsAdapter) ingredientsRecyclerView.getAdapter()).updateIngredientsFromShoppingList(mIngredientsFromShoppingList);
                if (mCurrentIngredientRecyclerViewState != null) {
                    mIngredientLinearLayoutManager.onRestoreInstanceState(mCurrentIngredientRecyclerViewState);
                }
            });
        }
        return view;
    }

    private void initRecyclerView(List<String> ingredientList, List<String> ingredientsFromShoppingList) {
        mIngredientLinearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsRecyclerView.setLayoutManager(mIngredientLinearLayoutManager);
        mAdapter = new RecipeIngredientsAdapter(this,
                mCtx, ingredientList, ingredientsFromShoppingList);
        ingredientsRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void addIngredientToTheShoppingList(String ingredientName) {
        mIngredient = new Ingredient(ingredientName, false);
        mRecipesViewModel.insertIngredient(mIngredient);
    }

    @Override
    public void removeIngredientFromTheShoppingList(String ingredientName) {
        mRecipesViewModel.deleteIngredientByName(ingredientName);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.CURRENT_INGREDIENT_POSITION, mIngredientLinearLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mCurrentIngredientRecyclerViewState = savedInstanceState.getParcelable(Constants.CURRENT_INGREDIENT_POSITION);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecipesViewModel.getIngredientNameList().removeObservers(Objects.requireNonNull(getActivity()));
    }
}
