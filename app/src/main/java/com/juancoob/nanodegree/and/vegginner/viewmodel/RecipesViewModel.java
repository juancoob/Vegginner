package com.juancoob.nanodegree.and.vegginner.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe.FavoriteRecipeRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.Ingredient;
import com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient.IngredientRepository;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.Recipe;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.SecondRecipeResponse;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.RecipeDataSource;
import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.datasource.factory.RecipeDataSourceFactory;
import com.juancoob.nanodegree.and.vegginner.util.Constants;
import com.juancoob.nanodegree.and.vegginner.util.NetworkState;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is the Recipe's ViewModel which connects the View with the database
 * <p>
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RecipesViewModel extends ViewModel {

    private Executor mExecutor;
    private LiveData<NetworkState> mNetworkStateLiveData;
    private LiveData<NetworkState> mInitialLoadingLiveData;
    private LiveData<PagedList<SecondRecipeResponse>> mSecondRecipeResponsePagedList;
    private MutableLiveData<String> mFragmentDetailToReplace;
    private MutableLiveData<Recipe> mSelectedRecipe;
    private MutableLiveData<String> mOptionSelected;
    private RecipeDataSourceFactory mRecipeDataSourceFactory;
    private FavoriteRecipeRepository mFavoriteRecipeRepository;
    private IngredientRepository mIngredientRepository;
    private IRecipeApiService mRecipeApiService;
    private PagedList.Config mPagedListConfig;

    public RecipesViewModel(FavoriteRecipeRepository favoriteRecipeRepository,
                            IngredientRepository ingredientRepository,
                            IRecipeApiService recipeApiService) {
        mFavoriteRecipeRepository = favoriteRecipeRepository;
        mIngredientRepository = ingredientRepository;
        mRecipeApiService = recipeApiService;
        initializeRecipeList();
        getRecipeData();
    }

    private void initializeRecipeList() {

        mExecutor = Executors.newFixedThreadPool(Constants.MAXIMUN_POOL_SIZE);

        mRecipeDataSourceFactory = new RecipeDataSourceFactory(mRecipeApiService);

        mNetworkStateLiveData = Transformations.switchMap(mRecipeDataSourceFactory.getRecipeDataSourceMutableLiveData(),
                RecipeDataSource::getNetworkState);

        mInitialLoadingLiveData = Transformations.switchMap(mRecipeDataSourceFactory.getRecipeDataSourceMutableLiveData(),
                RecipeDataSource::getInitialLoading);

        mFragmentDetailToReplace = new MutableLiveData<>();

        mOptionSelected = new MutableLiveData<>();

        mSelectedRecipe = new MutableLiveData<>();

        mPagedListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(Constants.INITIAL_LOAD)
                .setPageSize(Constants.ELEMENTS_BY_PAGE)
                .build();
    }

    private void getRecipeData() {
        mSecondRecipeResponsePagedList = new LivePagedListBuilder<Long, SecondRecipeResponse>(mRecipeDataSourceFactory, mPagedListConfig)
                .setFetchExecutor(mExecutor)
                .build();
    }

    private LiveData<PagedList<FavoriteRecipe>> getFavoriteRecipeData() {
        return new LivePagedListBuilder<>(mFavoriteRecipeRepository.getFavoriteRecipeList(), mPagedListConfig)
                .setFetchExecutor(mExecutor)
                .build();
    }

    public LiveData<NetworkState> getNetworkState() {
        return mNetworkStateLiveData;
    }

    public LiveData<NetworkState> getInitialLoadingLiveData() {
        return mInitialLoadingLiveData;
    }

    public LiveData<PagedList<SecondRecipeResponse>> getSecondRecipeResponsePagedList() {
        return mSecondRecipeResponsePagedList;
    }

    public LiveData<PagedList<SecondRecipeResponse>> getAgainSecondRecipeResponsePagedList() {
        getRecipeData();
        return mSecondRecipeResponsePagedList;
    }

    public LiveData<PagedList<FavoriteRecipe>> getFavoriteRecipePagedList() {
        return getFavoriteRecipeData();
    }

    public MutableLiveData<String> getFragmentDetailToReplace() {
        return mFragmentDetailToReplace;
    }

    public MutableLiveData<Recipe> getSelectedRecipe() {
        return mSelectedRecipe;
    }

    public MutableLiveData<String> getOptionSelected() {
        return mOptionSelected;
    }


    // Favorite recipe repository methods

    public LiveData<List<String>> getFavoriteRecipeListById() {
        return mFavoriteRecipeRepository.getFavoriteRecipeListById();
    }

    public LiveData<FavoriteRecipe> getFavoriteRecipeById(String recipeWeb) {
        return mFavoriteRecipeRepository.getFavoriteRecipeById(recipeWeb);
    }

    public void deleteFavoriteRecipe(final FavoriteRecipe recipe) {
        mFavoriteRecipeRepository.deleteFavoriteRecipe(recipe);
    }

    public void deleteFavoriteRecipeByWeb(final String recipeWeb) {
        mFavoriteRecipeRepository.deleteFavoriteRecipeByWeb(recipeWeb);
    }

    public void insertFavoriteRecipe(final FavoriteRecipe recipe) {
        mFavoriteRecipeRepository.insertFavoriteRecipe(recipe);
    }


    // Ingredient repository methods

    public LiveData<List<Ingredient>> getIngredientList() {
        return mIngredientRepository.getIngredientList();
    }

    public LiveData<List<String>> getIngredientNameList() {
        return mIngredientRepository.getIngredientNameList();
    }

    public List<Ingredient> getIngredientListWidget() {
        return mIngredientRepository.getIngredientListWidget();
    }

    public void insertIngredient(Ingredient ingredient) {
        mIngredientRepository.insertIngredient(ingredient);
    }

    public LiveData<Ingredient> getIngredient(String ingredientName) {
        return mIngredientRepository.getIngredient(ingredientName);
    }

    public void deleteIngredientByName(String ingredientName) {
        mIngredientRepository.deleteIngredientByName(ingredientName);
    }

    public void deleteAllIngredientsBought(List<Ingredient> ingredientList) {
        mIngredientRepository.deleteAllIngredientsBought(ingredientList);
    }

    public void updateIngredient(Ingredient ingredient, boolean isBought) {
        mIngredientRepository.updateIngredient(ingredient, isBought);
    }
}
