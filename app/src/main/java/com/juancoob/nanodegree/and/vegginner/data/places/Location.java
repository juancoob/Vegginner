package com.juancoob.nanodegree.and.vegginner.data.places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class gets the longitude and latitude
 * <p>
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class Location implements Parcelable {

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    @SerializedName("lat")
    private double mLatitude;
    @SerializedName("lng")
    private double mLongitude;

    protected Location(Parcel in) {
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
    }
}
