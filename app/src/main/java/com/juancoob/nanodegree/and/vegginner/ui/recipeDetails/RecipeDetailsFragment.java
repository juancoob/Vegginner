package com.juancoob.nanodegree.and.vegginner.ui.recipeDetails;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment shows the recipe's details using its toolbar, image and viewpager
 * <p>
 * Created by Juan Antonio Cobos Obrero on 30/07/18.
 */
public class RecipeDetailsFragment extends Fragment {

    @BindView(R.id.iv_recipe_backdrop)
    public ImageView recipeBackdropImageView;

    @BindView(R.id.t_recipe_toolbar)
    public Toolbar toolbarRecipe;

    @BindView(R.id.tl_recipe_detail)
    public TabLayout recipeDetailTabLayout;

    @BindView(R.id.vp_recipe_detail)
    public ViewPager recipeDetailViewPager;

    @BindView(R.id.fab_favorite)
    public FloatingActionButton favoriteFloatingActionButton;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    private RecipesViewModel mRecipesViewModel;
    private Recipe mRecipe;
    private int mPosition = 0;
    private FavoriteRecipe mFavoriteRecipe;
    private Context mCtx;

    public RecipeDetailsFragment() {
        // Empty constructor
    }

    public static RecipeDetailsFragment getInstance() {
        return new RecipeDetailsFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) mCtx.getApplicationContext()).getVegginnerRoomComponent().injectRecipeDetailsSection(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, view);
        mRecipesViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), vegginnerViewModelFactory).get(RecipesViewModel.class);
        mRecipesViewModel.getSelectedRecipe().observe(getViewLifecycleOwner(), recipe -> {
            if (getActivity() != null) {
                mRecipe = recipe;
                Picasso.get().load(mRecipe.getRecipeImage()).placeholder(R.mipmap.ic_launcher).into(recipeBackdropImageView);
                setToolbar();
                setPager();
                ObserveFavoriteRecipeById();
            }
        });
        return view;
    }

    private void ObserveFavoriteRecipeById() {
        mRecipesViewModel.getFavoriteRecipeById(mRecipe.getRecipeWeb()).observe(getViewLifecycleOwner(), favoriteRecipe -> {
            if (favoriteRecipe != null) {
                mFavoriteRecipe = favoriteRecipe;
                favoriteFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.ic_starred_24dp));
            } else {
                mFavoriteRecipe = null;
                favoriteFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.ic_star_border_24dp));
            }
        });
    }

    private void setToolbar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbarRecipe);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setHomeActionContentDescription(R.string.come_back_recipes);
            actionBar.setTitle(mRecipe.getRecipeName());
        }
    }

    private void setPager() {
        RecipeDetailsPageAdapter adapter = new RecipeDetailsPageAdapter(getFragmentManager(), mCtx, mRecipe);
        recipeDetailViewPager.setAdapter(adapter);
        recipeDetailViewPager.setCurrentItem(mPosition);
        recipeDetailTabLayout.setupWithViewPager(recipeDetailViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecipesViewModel.getSelectedRecipe().removeObservers(getViewLifecycleOwner());
        mRecipesViewModel.getFavoriteRecipeById(mRecipe.getRecipeWeb()).removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.VIEW_PAGER_POSITION, recipeDetailViewPager.getCurrentItem());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt(Constants.VIEW_PAGER_POSITION);
        }
    }

    @OnClick(R.id.fab_favorite)
    public void onClickFavoriteFloatingActionButton() {
        if (mFavoriteRecipe != null) {
            mRecipesViewModel.deleteFavoriteRecipe(mFavoriteRecipe);
        } else {
            mFavoriteRecipe = new FavoriteRecipe(mRecipe.getRecipeName(), mRecipe.getRecipeImage(),
                    mRecipe.getRecipeAuthor(), mRecipe.getRecipeWeb(), mRecipe.getRecipeServings(),
                    mRecipe.getIngredientList());
            mRecipesViewModel.insertFavoriteRecipe(mFavoriteRecipe);
        }
    }
}
