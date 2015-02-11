package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable {

    private static final String TAG = "Event";

    public static final String DETAILS_COL    = "Details";
    public static final String START_TIME_COL = "Time";
    public static final String TITLE_COL      = "Title";

    public Event() {}

    public String getDetails() {
        return getString(DETAILS_COL);
    }

    public void setDetails(String details) {
        put(DETAILS_COL, details);
    }

    public Date getStartTime() {
        return getDate(START_TIME_COL);
    }

    public String getDay() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getStartTime()); // someDate is a Date
        Locale locale = Locale.getDefault();
        return cal.getDisplayName(Calendar.DAY_OF_WEEK,Calendar.LONG,locale);
    }
    public String getTime() throws ParseException {
        //get date from parse
        //can't get right dates from parse for whatever reason,
        //so this hack is an offset of what should be displayed
        Date dateOfEvent = getStartTime();
        Calendar cal = Calendar.getInstance(); // creates calendar
        cal.setTime(dateOfEvent); // sets calendar time/date
        cal.add(Calendar.HOUR_OF_DAY, 4); // adds one hour
        String offsetTimeStr = cal.getTime().toString().split(" ")[3]; // returns new date object, one hour in the future

        SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss");
        Date offsetTime = parseFormat.parse(offsetTimeStr);
        SimpleDateFormat printFormat = new SimpleDateFormat("h:mm a");
//        Date date = parseFormat.parse(offsetTime);
        return printFormat.format(offsetTime);
    }

    public void setStartTime(Date date) {
        put(START_TIME_COL, date);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getObjectId());
        parcel.writeString(getDetails());
        parcel.writeValue(getStartTime());
        parcel.writeString(getTitle());
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel source) {
            return new Event(source);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    private Event(Parcel source) {
        setObjectId(source.readString());
        setDetails(source.readString());
        setStartTime((Date) source.readValue(Date.class.getClassLoader()));
        setTitle(source.readString());
    }
}
