package com.juancoob.nanodegree.and.vegginner.data;

import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Juan Antonio Cobos Obrero on 25/07/18.
 */
public class RestApiFactory {

    public static IRecipeApiService createRecipeApi() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(loggingInterceptor);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL_RECIPE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();
        return retrofit.create(IRecipeApiService.class);
    }

}