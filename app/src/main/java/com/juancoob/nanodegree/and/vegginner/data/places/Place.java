package com.juancoob.nanodegree.and.vegginner.data.places;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.util.DiffUtil;

import com.google.gson.annotations.SerializedName;

/**
 * This class is the place model
 * <p>
 * Created by Juan Antonio Cobos Obrero on 8/08/18.
 */
public class Place implements Parcelable {

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public static DiffUtil.ItemCallback<Place> DIFF_CALLBACK = new DiffUtil.ItemCallback<Place>() {
        @Override
        public boolean areItemsTheSame(Place oldItem, Place newItem) {
            return oldItem.getPlaceId().equals(newItem.getPlaceId());
        }

        @Override
        public boolean areContentsTheSame(Place oldItem, Place newItem) {
            return oldItem.equals(newItem);
        }
    };

    @SerializedName("id")
    private String mId;
    @SerializedName("place_id")
    private String mPlaceId;
    @SerializedName("name")
    private String mPlaceName;
    @SerializedName("geometry")
    private Geometry mGeometry;
    @SerializedName("icon")
    private String mIcon;
    @SerializedName("rating")
    private float mRating;
    @SerializedName("vicinity")
    private String mVicinity;

    protected Place(Parcel in) {
        mId = in.readString();
        mPlaceId = in.readString();
        mPlaceName = in.readString();
        mIcon = in.readString();
        mRating = in.readFloat();
        mVicinity = in.readString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        Place place = (Place) obj;
        return place.getPlaceId().equals(this.getPlaceId());
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getPlaceId() {
        return mPlaceId;
    }

    public void setPlaceId(String mPlaceId) {
        this.mPlaceId = mPlaceId;
    }

    public String getPlaceName() {
        return mPlaceName;
    }

    public void setPlaceName(String mPlaceName) {
        this.mPlaceName = mPlaceName;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }

    public void setGeometry(Geometry mGeometry) {
        this.mGeometry = mGeometry;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String mIcon) {
        this.mIcon = mIcon;
    }

    public float getRating() {
        return mRating;
    }

    public void setRating(float mRating) {
        this.mRating = mRating;
    }

    public String getVicinity() {
        return mVicinity;
    }

    public void setVicinity(String mVicinity) {
        this.mVicinity = mVicinity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mId);
        parcel.writeString(mPlaceId);
        parcel.writeString(mPlaceName);
        parcel.writeString(mIcon);
        parcel.writeFloat(mRating);
        parcel.writeString(mVicinity);
    }
}
