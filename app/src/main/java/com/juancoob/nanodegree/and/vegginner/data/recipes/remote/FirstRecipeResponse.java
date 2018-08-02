package com.juancoob.nanodegree.and.vegginner.data.recipes.remote;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class is going to take data from EDAMAM API
 *
 * Created by Juan Antonio Cobos Obrero on 24/07/18.
 */

public class FirstRecipeResponse implements Parcelable {

    public static final Creator<FirstRecipeResponse> CREATOR = new Creator<FirstRecipeResponse>() {
        @Override
        public FirstRecipeResponse createFromParcel(Parcel in) {
            return new FirstRecipeResponse(in);
        }

        @Override
        public FirstRecipeResponse[] newArray(int size) {
            return new FirstRecipeResponse[size];
        }
    };

    @SerializedName("hits")
    private List<SecondRecipeResponse> mHitList;

    protected FirstRecipeResponse(Parcel in) {
        mHitList = in.createTypedArrayList(SecondRecipeResponse.CREATOR);
    }

    public List<SecondRecipeResponse> getHitList() {
        return mHitList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mHitList);
    }
}
