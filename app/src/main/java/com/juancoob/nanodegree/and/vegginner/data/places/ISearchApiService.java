package com.juancoob.nanodegree.and.vegginner.data.places;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * This interface is the service to get places by type near to the user
 *
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public interface ISearchApiService {

    @GET("json")
    Call<FirstPlaceResponse> getFirstPlaceResponse(@Query("location") String location,
                                                   @Query("radius") int radius,
                                                   @Query("type") String type,
                                                   @Query("keyword") String keyword,
                                                   @Query("key") String key);
}
