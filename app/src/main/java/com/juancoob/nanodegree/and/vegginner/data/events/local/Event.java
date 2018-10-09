package com.juancoob.nanodegree.and.vegginner.data.events.local;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;

/**
 * This class is the event's model
 * <p>
 * Created by Juan Antonio Cobos Obrero on 27/09/18.
 */

@Entity
public class Event implements Parcelable {

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    public static DiffUtil.ItemCallback<Event> DIFF_CALLBACK = new DiffUtil.ItemCallback<Event>() {
        @Override
        public boolean areItemsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Event oldItem, @NonNull Event newItem) {
            return oldItem.equals(newItem);
        }
    };
    @PrimaryKey
    private Integer mId; //id
    private String mEventName; //name
    private Long mTime; //time
    private String mPhoto; //photo_url
    private String mDescription; //description
    private String mUrl; //event_url
    private int mCapacity; //rsvp_limit
    private int mPeopleGoing; //yes_rsvp_count

    public Event(Integer id, String eventName, Long time, String photo, String description, String url, int capacity, int peopleGoing) {
        mId = id;
        mEventName = eventName;
        mTime = time;
        mPhoto = photo;
        mDescription = description;
        mUrl = url;
        mCapacity = capacity;
        mPeopleGoing = peopleGoing;
    }

    protected Event(Parcel in) {
        mId = in.readInt();
        mEventName = in.readString();
        if (in.readByte() == 0) {
            mTime = null;
        } else {
            mTime = in.readLong();
        }
        mPhoto = in.readString();
        mDescription = in.readString();
        mUrl = in.readString();
        mCapacity = in.readInt();
        mPeopleGoing = in.readInt();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        Event event = (Event) obj;
        return event.getId().equals(this.getId());
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer mId) {
        this.mId = mId;
    }

    public String getEventName() {
        return mEventName;
    }

    public void setEventName(String mEventName) {
        this.mEventName = mEventName;
    }

    public Long getTime() {
        return mTime;
    }

    public void setTime(Long mTime) {
        this.mTime = mTime;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    //private Venue mVenue;

    public int getCapacity() {
        return mCapacity;
    }

    public void setCapacity(int mCapacity) {
        this.mCapacity = mCapacity;
    }

    public int getPeopleGoing() {
        return mPeopleGoing;
    }

    public void setPeopleGoing(int mPeopleGoing) {
        this.mPeopleGoing = mPeopleGoing;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mEventName);
        if (mTime == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeLong(mTime);
        }
        parcel.writeString(mPhoto);
        parcel.writeString(mDescription);
        parcel.writeString(mUrl);
        parcel.writeInt(mCapacity);
        parcel.writeInt(mPeopleGoing);
    }
}
