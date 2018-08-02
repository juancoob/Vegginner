package com.juancoob.nanodegree.and.vegginner.di;

import com.juancoob.nanodegree.and.vegginner.data.recipes.remote.IRecipeApiService;
import com.juancoob.nanodegree.and.vegginner.util.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * This is the Retrofit module to get the HttpLoggingInterceptor, OkHttpClient.Builder, Retrofit
 * and Api services instance
 *
 * Created by Juan Antonio Cobos Obrero on 29/07/18.
 */

@Module
public class RetrofitModule {

    @Provides
    @Singleton
    IRecipeApiService provideRecipeApiService(Retrofit retrofit) {
        return retrofit.create(IRecipeApiService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient.Builder httpClientBuilder) {
        return new Retrofit.Builder().baseUrl(Constants.BASE_URL_RECIPE)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(httpClientBuilder.build())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient.Builder providesOkHttpClientBuilder(HttpLoggingInterceptor httpLoggingInterceptor) {
        return new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor);
    }


    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        return new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
    }

}
