package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 * edited by Carl Johnson sometime in Spring 2015
 */
@ParseClassName("Award")
public class Award extends ParseObject implements Parcelable {

    public static final String DESCRIPTION_COL = "details";
    public static final String SPONSOR_COL     = "Sponsor";
    public static final String TITLE_COL       = "Title";
    public static final String VALUE_COL       = "Value";

    public Award() {}

    public String getDescription() {
        return getString(DESCRIPTION_COL);
    }

    public void setDescription(String description) {
        put(DESCRIPTION_COL, description);
    }

    public Sponsor getSponsor() {
        return (Sponsor) getParseObject(SPONSOR_COL);

    }

//    public String getSponsorName() {
//        return getString(SPONSOR_COL);
//    }

    public void setSponsor(Sponsor sponsor) {
        put(SPONSOR_COL, sponsor);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    public String getValue() {
        return getString(VALUE_COL);
    }

    public void setValue(String value) {
        put(VALUE_COL, value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getDescription());
        parcel.writeParcelable(getSponsor(), i);
//        parcel.writeString(getSponsor());
        parcel.writeString(getTitle());
//        parcel.writeInt(getValue());
    }

    public static final Creator<Award> CREATOR = new Creator<Award>() {
        @Override
        public Award createFromParcel(Parcel source) {
            return new Award(source);
        }

        @Override
        public Award[] newArray(int size) {
            return new Award[size];
        }
    };

    private Award(Parcel source) {
        setObjectId(source.readString());
        setDescription(source.readString());
//        setPrize(source.readString());
        setSponsor((Sponsor) source.readParcelable(Sponsor.class.getClassLoader()));
        setTitle(source.readString());
//        setSponsor(source.readString());
//        setValue(source.readInt());
//        setWebsite(source.readString());
    }
}
