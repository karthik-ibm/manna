package com.ibm.manna.pojo;

public class AuthenticationRequest {
    String deviceID,type,mannaID,mobile;
    double latitude,longitude;

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

    public String getMannaID() {
        return mannaID;
    }

    public void setMannaID(String mannaID) {
        this.mannaID = mannaID;
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

    /* public Coord getUserCoordinates() {
        return userCoordinates;
    }

    public void setUserCoordinates(Coord userCoordinates) {
        this.userCoordinates = userCoordinates;
    }*/
}
