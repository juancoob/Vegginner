package com.juancoob.nanodegree.and.vegginner.util;

/**
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */
public final class Constants {
    // Fragment names
    public static final String RECIPE_DETAILS = "recipeDetails";

    // ViewPager
    public static final int NUM_RECIPE_DETAIL_PAGES = 2;

    // Network states
    public static final String SUCCESS = "Success";
    public static final String RUNNING = "Running";

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
}