package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    String line1,line2,city,state,pin;

    public Address(String line1, String line2, String city, String state, String pin) {
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.pin = pin;
    }

    public Address() {
    }

    protected Address(Parcel in) {
        line1 = in.readString();
        line2 = in.readString();
        city = in.readString();
        state = in.readString();
        pin = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(line1);
        dest.writeString(line2);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(pin);
    }
}
