package com.juancoob.nanodegree.and.vegginner.data.recipes.remote;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 * <p>
 * This class is going to take recipes from EDAMAM API
 */


public class Recipe implements Parcelable {

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @SerializedName("label")
    private String mRecipeName;
    @SerializedName("image")
    private String mRecipeImage;
    @SerializedName("source")
    private String mRecipeAuthor;
    @SerializedName("url")
    private String mRecipeWeb;
    @SerializedName("yield")
    private int mRecipeServings;
    @SerializedName("ingredientLines")
    private List<String> mIngredientList;

    public Recipe(String mRecipeName, String mRecipeImage, String mRecipeAuthor, String mRecipeWeb, int mRecipeServings, List<String> mIngredientList) {
        this.mRecipeName = mRecipeName;
        this.mRecipeImage = mRecipeImage;
        this.mRecipeAuthor = mRecipeAuthor;
        this.mRecipeWeb = mRecipeWeb;
        this.mRecipeServings = mRecipeServings;
        this.mIngredientList = mIngredientList;
    }

    protected Recipe(Parcel in) {
        mRecipeName = in.readString();
        mRecipeImage = in.readString();
        mRecipeAuthor = in.readString();
        mRecipeWeb = in.readString();
        mRecipeServings = in.readInt();
        mIngredientList = in.createStringArrayList();
    }

    public String getRecipeName() {
        return mRecipeName;
    }

    public void setRecipeName(String mRecipeName) {
        this.mRecipeName = mRecipeName;
    }

    public String getRecipeImage() {
        return mRecipeImage;
    }

    public void setRecipeImage(String mRecipeImage) {
        this.mRecipeImage = mRecipeImage;
    }

    public String getRecipeAuthor() {
        return mRecipeAuthor;
    }

    public void setRecipeAuthor(String mRecipeAuthor) {
        this.mRecipeAuthor = mRecipeAuthor;
    }

    public String getRecipeWeb() {
        return mRecipeWeb;
    }

    public void setRecipeWeb(String mRecipeWeb) {
        this.mRecipeWeb = mRecipeWeb;
    }

    public int getRecipeServings() {
        return mRecipeServings;
    }

    public void setRecipeServings(int mRecipeServings) {
        this.mRecipeServings = mRecipeServings;
    }

    public List<String> getIngredientList() {
        return mIngredientList;
    }

    public void setIngredientList(List<String> mIngredientList) {
        this.mIngredientList = mIngredientList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mRecipeName);
        parcel.writeString(mRecipeImage);
        parcel.writeString(mRecipeAuthor);
        parcel.writeString(mRecipeWeb);
        parcel.writeInt(mRecipeServings);
        parcel.writeStringList(mIngredientList);
    }
}
