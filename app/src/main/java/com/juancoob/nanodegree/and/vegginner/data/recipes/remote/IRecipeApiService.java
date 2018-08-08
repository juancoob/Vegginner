package com.juancoob.nanodegree.and.vegginner.data.recipes.remote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface is the service to get recipes
 *
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public interface IRecipeApiService {

    @GET("search")
    Call<FirstRecipeResponse> getFirstRecipeResponse(@Query("q") String query,
                                                           @Query("app_id") String appId,
                                                           @Query("app_key") String appKey,
                                                           @Query("excluded") String excluded,
                                                           @Query("from") Long page,
                                                           @Query("to") int maxPages);
}
