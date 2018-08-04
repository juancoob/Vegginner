package com.juancoob.nanodegree.and.vegginner.data.recipes.local.ingredient;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * This class in the ingredient's model
 * <p>
 * Created by Juan Antonio Cobos Obrero on 1/08/18.
 */

@Entity
public class Ingredient implements Parcelable {

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    @PrimaryKey(autoGenerate = true)
    private int mIngredientId;
    private String mIngredientName;
    private boolean mIsBought;

    public Ingredient(String mIngredientName, boolean mIsBought) {
        this.mIngredientName = mIngredientName;
        this.mIsBought = mIsBought;
    }

    protected Ingredient(Parcel in) {
        mIngredientId = in.readInt();
        mIngredientName = in.readString();
        mIsBought = in.readByte() != 0;
    }

    public int getIngredientId() {
        return mIngredientId;
    }

    public void setIngredientId(int mIngredientId) {
        this.mIngredientId = mIngredientId;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public void setIngredientName(String mIngredient) {
        this.mIngredientName = mIngredient;
    }

    public boolean isBought() {
        return mIsBought;
    }

    public void setIsBought(boolean mIsBought) {
        this.mIsBought = mIsBought;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mIngredientId);
        parcel.writeString(mIngredientName);
        parcel.writeByte((byte) (mIsBought ? 1 : 0));
    }
}
