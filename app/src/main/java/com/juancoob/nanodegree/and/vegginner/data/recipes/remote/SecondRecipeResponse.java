package com.juancoob.nanodegree.and.vegginner.data.recipes.remote;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.SerializedName;

/**
 * This class takes recipes from EDAMAM API
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */

public class SecondRecipeResponse implements Parcelable {


    public static final Creator<SecondRecipeResponse> CREATOR = new Creator<SecondRecipeResponse>() {
        @Override
        public SecondRecipeResponse createFromParcel(Parcel in) {
            return new SecondRecipeResponse(in);
        }

        @Override
        public SecondRecipeResponse[] newArray(int size) {
            return new SecondRecipeResponse[size];
        }
    };

    public static DiffUtil.ItemCallback<SecondRecipeResponse> DIFF_CALLBACK = new DiffUtil.ItemCallback<SecondRecipeResponse>() {
        @Override
        public boolean areItemsTheSame(SecondRecipeResponse oldItem, SecondRecipeResponse newItem) {
            return oldItem.getRecipe().getRecipeWeb().equals(newItem.getRecipe().getRecipeWeb());
        }

        @Override
        public boolean areContentsTheSame(SecondRecipeResponse oldItem, SecondRecipeResponse newItem) {
            return oldItem.equals(newItem);
        }

    };

    @SerializedName("recipe")
    private Recipe mRecipe;

    protected SecondRecipeResponse(Parcel in) {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        SecondRecipeResponse secondRecipeResponse = (SecondRecipeResponse) obj;
        return secondRecipeResponse.getRecipe().getRecipeWeb()
                .equals(this.getRecipe().getRecipeWeb());
    }

    public Recipe getRecipe() {
        return mRecipe;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
