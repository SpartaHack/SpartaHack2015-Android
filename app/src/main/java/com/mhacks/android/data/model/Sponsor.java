package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Omkar Moghe on 10/13/2014.
 * edited by Carl Johnson sometime in Spring 2015
 */
@ParseClassName("Concierge")
public class Sponsor extends ParseObject implements Parcelable {

    public static final String TITLE_COL = "Title";
    public static final String NAME_COL = "Name";
    public static final String SPECIALTY_COL = "Specialty";
    public static String twitter = null;
    public static String email = null;

    public String getEmail() {
        return email;
    }

    public String getName() {
        return getString(NAME_COL);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public String getTwitter() {
        return twitter;
    }
    public String getSpecialty() {
        return getString(SPECIALTY_COL);
    }
    public void setTitle(String title ) {
        put(TITLE_COL, title);
    }
    public void setName(String _name ) { put(NAME_COL,_name); }
    public void setTwitter(String _twitter ) {
        twitter = _twitter;
    }
    public void setEmail(String _email) {
        email = _email;
    }
    public void setSpecialty(String _speciality) {
        put(SPECIALTY_COL,_speciality);
    }
    public Sponsor() {}


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getTitle());
        parcel.writeString(getName());
        parcel.writeString(getTwitter());
    }

    public static final Creator<Sponsor> CREATOR = new Creator<Sponsor>() {
        @Override
        public Sponsor createFromParcel(Parcel source) {
            return new Sponsor(source);
        }

        @Override
        public Sponsor[] newArray(int size) {
            return new Sponsor[size];
        }
    };

    private Sponsor(Parcel source) {
        setObjectId(source.readString());
        setName(source.readString());
        setTitle(source.readString());
        setTwitter(source.readString());
    }
}
