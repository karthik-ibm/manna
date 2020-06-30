package com.ibm.manna.pojo;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class UserProfile implements Parcelable {
    String mannaID,fname,lname,uid,mobile,deviceID,type,imageurl;
    double latitude,longitude,rating;
    Image image;
    Address address;

    public UserProfile() {
    }

    public UserProfile(String mannaID, String fname, String lname, String uid, String mobile, String deviceID, String type, String imageurl, double latitude, double longitude, double rating, Image image, Address address) {
        this.mannaID = mannaID;
        this.fname = fname;
        this.lname = lname;
        this.uid = uid;
        this.mobile = mobile;
        this.deviceID = deviceID;
        this.type = type;
        this.imageurl = imageurl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.image = image;
        this.address = address;
    }


    protected UserProfile(Parcel in) {
        mannaID = in.readString();
        fname = in.readString();
        lname = in.readString();
        uid = in.readString();
        mobile = in.readString();
        deviceID = in.readString();
        type = in.readString();
        imageurl = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        rating = in.readDouble();
        address = in.readParcelable(Address.class.getClassLoader());
    }

    public static final Creator<UserProfile> CREATOR = new Creator<UserProfile>() {
        @Override
        public UserProfile createFromParcel(Parcel in) {
            return new UserProfile(in);
        }

        @Override
        public UserProfile[] newArray(int size) {
            return new UserProfile[size];
        }
    };

    public String getMannaID() {
        return mannaID;
    }

    public void setMannaID(String mannaID) {
        this.mannaID = mannaID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public static Creator<UserProfile> getCREATOR() {
        return CREATOR;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mannaID);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(uid);
        dest.writeString(mobile);
        dest.writeString(deviceID);
        dest.writeString(type);
        dest.writeString(imageurl);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(rating);
        dest.writeParcelable(address, flags);
    }
}

