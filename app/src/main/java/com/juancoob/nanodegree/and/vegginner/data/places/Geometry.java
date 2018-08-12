package com.juancoob.nanodegree.and.vegginner.data.places;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * This class gets information about the location
 * <p>
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class Geometry implements Parcelable {

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel in) {
            return new Geometry(in);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };

    @SerializedName("location")
    private Location mLocation;

    protected Geometry(Parcel in) {
        mLocation = in.readParcelable(Location.class.getClassLoader());
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(mLocation, i);
    }
}
