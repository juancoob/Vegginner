package com.juancoob.nanodegree.and.vegginner.data.places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * This class gets all places by place type
 * <p>
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class FirstPlaceResponse implements Parcelable {

    public static final Creator<FirstPlaceResponse> CREATOR = new Creator<FirstPlaceResponse>() {
        @Override
        public FirstPlaceResponse createFromParcel(Parcel in) {
            return new FirstPlaceResponse(in);
        }

        @Override
        public FirstPlaceResponse[] newArray(int size) {
            return new FirstPlaceResponse[size];
        }
    };

    @SerializedName("results")
    private List<Place> mPlaceList;

    protected FirstPlaceResponse(Parcel in) {
        mPlaceList = in.createTypedArrayList(Place.CREATOR);
    }

    public List<Place> getPlaceList() {
        return mPlaceList;
    }

    public void setPlaceList(List<Place> mPlaceList) {
        this.mPlaceList = mPlaceList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(mPlaceList);
    }
}
