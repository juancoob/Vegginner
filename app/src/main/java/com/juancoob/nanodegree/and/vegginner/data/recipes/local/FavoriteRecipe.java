package com.juancoob.nanodegree.and.vegginner.data.recipes.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */
@Entity
public class FavoriteRecipe {

    @PrimaryKey(autoGenerate = true)
    private int mRecipeId;
    private String mRecipeName;
    private String mRecipeImage;
    private String mRecipeAuthor;
    private String mRecipeWeb;
    private int mRecipeServings;
    @TypeConverters(FavoriteRecipeTypeConverter.class)
    private List<String> mIngredientList;

    public FavoriteRecipe(String mRecipeName, String mRecipeImage, String mRecipeAuthor, String mRecipeWeb, int mRecipeServings, List<String> mIngredientList) {
        this.mRecipeName = mRecipeName;
        this.mRecipeImage = mRecipeImage;
        this.mRecipeAuthor = mRecipeAuthor;
        this.mRecipeWeb = mRecipeWeb;
        this.mRecipeServings = mRecipeServings;
        this.mIngredientList = mIngredientList;
    }

    public int getRecipeId() {
        return mRecipeId;
    }

    public void setRecipeId(int mRecipeId) {
        this.mRecipeId = mRecipeId;
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
}

