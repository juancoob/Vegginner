package com.juancoob.nanodegree.and.vegginner.util;

/**
 * This class contains constants to use on the entire app
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */
public final class Constants {
    // Fragment names
    public static final String BEGINNING = "beginning";
    public static final String ADVICES = "advices";
    public static final String EQUIVALENCIES = "equivalencies";
    public static final String RECIPES = "recipes";
    public static final String RECIPE_DETAILS = "recipeDetails";
    public static final String PLACES = "places";
    public static final String EVENTS = "events";

    // Recipe ViewPager
    public static final int NUM_RECIPE_DETAIL_PAGES = 2;
    public static final String VIEW_PAGER_POSITION = "viewPagerPosition";
    public static final String RECIPE = "recipe";

    // Recipe menu
    public static final String ALL_RECIPES = "allRecipes";
    public static final String FAV_RECIPES = "favRecipes";

    // RecyclerView positions
    public static final String CURRENT_RECIPE_POSITION = "currentFavRecipePosition";
    public static final String CURRENT_INGREDIENT_POSITION = "currentIngredientPosition";

    // Network states
    public static final String SUCCESS = "success";
    public static final String RUNNING = "running";

    // Response type
    public static final int SUCCESS_RESPONSE = 200;

    // Executor configuration
    public static final int MAXIMUN_POOL_SIZE = 5;

    // Paging
    public static final int INITIAL_ELEMENT = 0;
    public static final int INITIAL_LOAD = 25;
    public static final int ELEMENTS_BY_PAGE = 25;
    public static final int EDAMAM_API_MAX_RESULTS = 100;

    // Recipe api service data
    public static final String BASE_URL_RECIPE = "https://api.edamam.com/";
    public static final String QUERY_URL_RECIPE = "vegan";
    public static final String EXCLUDED_RECIPE = "soy";

    // Widget
    public static final String CLEAR_ACTION = "clearAction";
    public static final String BOUGHT_ACTION = "boughtAction";
    public static final String EXTRA_INGREDIENT_ID = "extraIngredient";
    public static final String EXTRA_IS_BOUGHT = "extraIsBought";

    // Places
    public static final int REQUEST_ACCESS_FINE_LOCATION = 123;
    public static final int REQUEST_CONNECTION_FAILURE = 404;
    public static final int UPDATE_LOCATION_INTERVAL = 10000;
    public static final int FASTEST_UPDATE_LOCATION_INTERVAL = 5000;
    public static final String SPINNER_POSITION = "spinnerSelection";
    public static final String LIST_POSITION = "listPosition";
    public static final String CURRENT_LOCATION = "currentLocation";
    public static final String BASE_URL_PLACES = "https://maps.googleapis.com/maps/api/place/nearbysearch/";
    public static final int BASE_RADIUS = 1500;
    public static final String KEYWORD = "vegan";

}