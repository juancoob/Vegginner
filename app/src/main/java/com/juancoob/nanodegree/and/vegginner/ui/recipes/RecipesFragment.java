package com.juancoob.nanodegree.and.vegginner.ui.recipes;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.juancoob.nanodegree.and.vegginner.R;
import com.juancoob.nanodegree.and.vegginner.VegginnerApp;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.util.CheckInternetConnection;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.viewmodel.RecipesViewModel;
import com.juancoob.nanodegree.and.vegginner.viewmodel.VegginnerViewModelFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * This fragment shows the recipe list
 * <p>
 * Created by Juan Antonio Cobos Obrero on 26/07/18.
 */
public class RecipesFragment extends Fragment implements IRetryLoadingCallback {

    @BindView(R.id.rv_recipes)
    public RecyclerView recipesRecyclerView;

    @BindView(R.id.pb_loading)
    public ProgressBar loadingProgressBar;

    @BindView(R.id.iv_edamam_logo)
    public ImageView edamamLogo;

    @BindView(R.id.tv_no_recipes)
    public TextView noRecipesTextView;

    @BindView(R.id.btn_retry)
    public Button retryButton;

    @Inject
    public VegginnerViewModelFactory vegginnerViewModelFactory;

    private Context mCtx;
    private RecipesListAdapter mRecipesListAdapter;
    private FavoriteListAdapter mFavoriteListAdapter;
    private RecipesViewModel mRecipesViewModel;
    private Menu mRecipesMenu;
    private String mOptionSelected;
    private GridLayoutManager mGridLayoutManager;
    private Parcelable mCurrentFavRecipeRecyclerViewState;
    private List<String> mFavoriteElementListById;

    public RecipesFragment() {
        // Required empty public constructor
    }

    public static RecipesFragment getInstance() {
        return new RecipesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCtx = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((VegginnerApp) mCtx.getApplicationContext()).getVegginnerRoomComponent().injectRecipesSection(this);
        mFavoriteElementListById = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mRecipesViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()), vegginnerViewModelFactory).get(RecipesViewModel.class);
        initAllRecipesRecyclerview();
        initFavoriteRecipesRecyclerView();
        mRecipesViewModel.getOptionSelected().observe(getViewLifecycleOwner(), optionSelected -> {
            mOptionSelected = optionSelected;

            if (Constants.ALL_RECIPES.equals(mOptionSelected)) {
                mRecipesViewModel.getSecondRecipeResponsePagedList().observe(getViewLifecycleOwner(), secondRecipeResponseList -> {
                    if (Constants.ALL_RECIPES.equals(mOptionSelected)) {
                        mRecipesListAdapter.submitList(secondRecipeResponseList);
                    }
                });
                mRecipesViewModel.getNetworkState().observe(getViewLifecycleOwner(), networkState -> {
                    if (Constants.ALL_RECIPES.equals(mOptionSelected)) {
                        mRecipesListAdapter.setNetworkState(networkState);
                    }
                });
                mRecipesViewModel.getInitialLoadingLiveData().observe(getViewLifecycleOwner(), networkState -> {
                    if (Constants.ALL_RECIPES.equals(mOptionSelected)) {
                        mRecipesListAdapter.checkInitialLoading(networkState);
                    }
                });
                recipesRecyclerView.setAdapter(mRecipesListAdapter);
            } else if (Constants.FAV_RECIPES.equals(mOptionSelected)) {
                mRecipesViewModel.getFavoriteRecipePagedList().observe(getViewLifecycleOwner(), favoriteRecipes -> {
                    if (Constants.FAV_RECIPES.equals(mOptionSelected)) {
                        hideProgressBar();
                        if (favoriteRecipes == null || favoriteRecipes.size() == 0) {
                            showNoFavElements();
                        }
                        mFavoriteListAdapter.submitList(favoriteRecipes);
                        if (mCurrentFavRecipeRecyclerViewState != null) {
                            mGridLayoutManager.onRestoreInstanceState(mCurrentFavRecipeRecyclerViewState);
                        }
                    }
                });
                recipesRecyclerView.setAdapter(mFavoriteListAdapter);
            }
        });
        mRecipesViewModel.getFavoriteRecipeListById().observe(getViewLifecycleOwner(), favoriteElementListById -> {
            if (favoriteElementListById != null && favoriteElementListById != mFavoriteElementListById) {
                mFavoriteElementListById = favoriteElementListById;
                mRecipesListAdapter.updateFavoriteElementListById(mFavoriteElementListById);
            }
        });

        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null && mOptionSelected == null) {
            mRecipesViewModel.getOptionSelected().setValue(Constants.ALL_RECIPES);
        } else if (savedInstanceState != null) {
            mCurrentFavRecipeRecyclerViewState = savedInstanceState.getParcelable(Constants.CURRENT_RECIPE_POSITION);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.CURRENT_RECIPE_POSITION, mGridLayoutManager.onSaveInstanceState());
        if(CheckInternetConnection.getAlertDialog() != null) {
            CheckInternetConnection.getAlertDialog().dismiss();
        }
    }

    private void initAllRecipesRecyclerview() {
        mGridLayoutManager = new GridLayoutManager(mCtx, getNumberColumns());
        recipesRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecipesListAdapter = new RecipesListAdapter(this, mCtx, mFavoriteElementListById);
    }

    private void initFavoriteRecipesRecyclerView() {
        mFavoriteListAdapter = new FavoriteListAdapter(this);
    }

    private int getNumberColumns() {
        if (getActivity() != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            int columns = width / getResources().getInteger(R.integer.width_divider);
            if (columns >= 2) return columns;
        }
        return 2;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.recipes_menu, menu);
        mRecipesMenu = menu;
        setMenuAfterRestoringState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_all_recipes:
                if (recipesRecyclerView.getAdapter() instanceof FavoriteListAdapter) {
                    showAllRecipesOption();
                    mRecipesViewModel.getOptionSelected().setValue(Constants.ALL_RECIPES);
                }
                break;
            case R.id.nav_fav_recipes:
                if (recipesRecyclerView.getAdapter() instanceof RecipesListAdapter) {
                    showFavoriteRecipesOption();
                    mRecipesViewModel.getOptionSelected().setValue(Constants.FAV_RECIPES);
                }
                break;
        }
        hideNoElements();
        return super.onOptionsItemSelected(item);
    }

    private void setMenuAfterRestoringState() {
        if (mOptionSelected != null) {
            if (Constants.ALL_RECIPES.equals(mOptionSelected)) {
                showAllRecipesOption();
            } else {
                showFavoriteRecipesOption();
            }
        }
    }

    private void showAllRecipesOption() {
        mRecipesMenu.findItem(R.id.nav_all_recipes_label).setVisible(true);
        mRecipesMenu.findItem(R.id.nav_fav_recipes_label).setVisible(false);
    }

    private void showFavoriteRecipesOption() {
        mRecipesMenu.findItem(R.id.nav_all_recipes_label).setVisible(false);
        mRecipesMenu.findItem(R.id.nav_fav_recipes_label).setVisible(true);
    }

    @Override
    public void loadAllListAgain() {
        mRecipesViewModel.getAgainSecondRecipeResponsePagedList().observe(getViewLifecycleOwner(), secondRecipeResponses -> {
            if (secondRecipeResponses != null && secondRecipeResponses.size() > 0) {
                mRecipesListAdapter.submitList(secondRecipeResponses);
            }
        });
    }

    @Override
    public void showProgressBar() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        edamamLogo.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        loadingProgressBar.setVisibility(View.GONE);
        edamamLogo.setVisibility(View.GONE);
    }

    @Override
    public void showNoElements() {
        noRecipesTextView.setVisibility(View.VISIBLE);
        retryButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoFavElements() {
        noRecipesTextView.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retry)
    public void OnRetryPressed() {
        hideNoElements();
        showProgressBar();
        loadAllListAgain();
    }

    public void hideNoElements() {
        noRecipesTextView.setVisibility(View.GONE);
        retryButton.setVisibility(View.GONE);
    }

    @Override
    public void showRecipeDetails(Recipe selectedRecipe) {
        mRecipesViewModel.getFragmentDetailToReplace().setValue(Constants.RECIPE_DETAILS);
        mRecipesViewModel.getSelectedRecipe().setValue(selectedRecipe);
        mRecipesViewModel.getOptionSelected().removeObservers(getViewLifecycleOwner());
        mRecipesViewModel.getFavoriteRecipeListById().removeObservers(getViewLifecycleOwner());
    }

    @Override
    public void deleteFavoriteRecipe(FavoriteRecipe favoriteRecipe) {
        mRecipesViewModel.deleteFavoriteRecipe(favoriteRecipe);
    }

    @Override
    public void deleteFavoriteRecipeByWeb(String webRecipe) {
        mRecipesViewModel.deleteFavoriteRecipeByWeb(webRecipe);
    }

    @Override
    public void insertFavoriteRecipe(FavoriteRecipe favoriteRecipe) {
        mRecipesViewModel.insertFavoriteRecipe(favoriteRecipe);
    }
}
