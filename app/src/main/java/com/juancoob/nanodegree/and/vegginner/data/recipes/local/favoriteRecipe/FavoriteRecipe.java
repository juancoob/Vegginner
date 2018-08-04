package com.juancoob.nanodegree.and.vegginner.data.recipes.local.favoriteRecipe;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.util.DiffUtil;

import java.util.List;

/**
 * This class is the favorite recipe's model
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */
@Entity
public class FavoriteRecipe implements Parcelable {

    public static final Creator<FavoriteRecipe> CREATOR = new Creator<FavoriteRecipe>() {
        @Override
        public FavoriteRecipe createFromParcel(Parcel in) {
            return new FavoriteRecipe(in);
        }

        @Override
        public FavoriteRecipe[] newArray(int size) {
            return new FavoriteRecipe[size];
        }
    };

    public static DiffUtil.ItemCallback<FavoriteRecipe> DIFF_CALLBACK = new DiffUtil.ItemCallback<FavoriteRecipe>() {
        @Override
        public boolean areItemsTheSame(FavoriteRecipe oldItem, FavoriteRecipe newItem) {
            return oldItem.getRecipeId() == newItem.getRecipeId();
        }

        @Override
        public boolean areContentsTheSame(FavoriteRecipe oldItem, FavoriteRecipe newItem) {
            return oldItem.equals(newItem);
        }
    };

    @PrimaryKey(autoGenerate = true)
    private int mRecipeId;
    private String mRecipeName;
    private String mRecipeImage;
    private String mRecipeAuthor;
    private String mRecipeWeb;
    private int mRecipeServings;
    @TypeConverters(FavoriteRecipeTypeConverter.class)
    private List<String> mIngredientList;

    protected FavoriteRecipe(Parcel in) {
        mRecipeId = in.readInt();
        mRecipeName = in.readString();
        mRecipeImage = in.readString();
        mRecipeAuthor = in.readString();
        mRecipeWeb = in.readString();
        mRecipeServings = in.readInt();
        mIngredientList = in.createStringArrayList();
    }

    public FavoriteRecipe(String mRecipeName, String mRecipeImage, String mRecipeAuthor, String mRecipeWeb, int mRecipeServings, List<String> mIngredientList) {
        this.mRecipeName = mRecipeName;
        this.mRecipeImage = mRecipeImage;
        this.mRecipeAuthor = mRecipeAuthor;
        this.mRecipeWeb = mRecipeWeb;
        this.mRecipeServings = mRecipeServings;
        this.mIngredientList = mIngredientList;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        FavoriteRecipe favoriteRecipe = (FavoriteRecipe) obj;
        return favoriteRecipe.getRecipeId() == this.getRecipeId();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mRecipeId);
        parcel.writeString(mRecipeName);
        parcel.writeString(mRecipeImage);
        parcel.writeString(mRecipeAuthor);
        parcel.writeString(mRecipeWeb);
        parcel.writeInt(mRecipeServings);
        parcel.writeStringList(mIngredientList);
    }
}

