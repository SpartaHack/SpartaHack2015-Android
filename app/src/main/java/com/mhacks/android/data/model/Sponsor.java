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
    public static String TWITTER_COL = "Twitter";
    public static String EMAIL_COL = "email";


    public String getEmail() {
        return getString(EMAIL_COL);
    }

    public String getName() {
        return getString(NAME_COL);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public String getTwitter() {
        return getString(TWITTER_COL);
    }
    public String getSpecialty() {
        return getString(SPECIALTY_COL);
    }
    public void setTitle(String title ) {
        if(title != null) {
            put(TITLE_COL, title);
        }
    }
    public void setName(String _name ) {
        if(_name != null) {
            put(NAME_COL,_name);
        }
    }
    public void setTwitter(String _twitter ) {
        if(_twitter != null) {
            put(TWITTER_COL, _twitter);
        }
    }
    public void setEmail(String _email) {
        if(_email != null) {
            put(EMAIL_COL,_email);
        }
    }
    public void setSpecialty(String _specialty) {
        if(_specialty != null) {
            put(SPECIALTY_COL,_specialty);
        }
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
