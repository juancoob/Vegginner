package com.juancoob.nanodegree.and.vegginner.data.recipes.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public interface IRecipeApiService {

    //https://api.edamam.com/search?q=vegan&app_id=610e2e22&app_key=68a4b20b60fbdef2990300314806e027&excluded=soy&to=31

    @GET("search")
    Call<FirstRecipeResponse> getFirstRecipeResponse(@Query("q") String query,
                                                           @Query("app_id") String appId,
                                                           @Query("app_key") String appKey,
                                                           @Query("excluded") String excluded,
                                                           @Query("from") Long page,
                                                           @Query("to") int maxPages);
}
