package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class RequestDetails implements Parcelable {
    String reqID ,reqType ,reqDt ,image ,mannaID ,firstname ,lastname ,phone ,reqStatus ,reqAcpt_Rjct;
    Address pickup_address;
    Coord pickup_coords;
    ArrayList<RequestItems> reqItems;

    public RequestDetails() {
    }

    public RequestDetails(String reqID, String reqType, String reqDt, String image, String mannaID, String firstname, String lastname, String phone, String reqStatus, String reqAcpt_Rjct, Address pickup_address, Coord pickup_coords, ArrayList<RequestItems> reqItems) {
        this.reqID = reqID;
        this.reqType = reqType;
        this.reqDt = reqDt;
        this.image = image;
        this.mannaID = mannaID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phone = phone;
        this.reqStatus = reqStatus;
        this.reqAcpt_Rjct = reqAcpt_Rjct;
        this.pickup_address = pickup_address;
        this.pickup_coords = pickup_coords;
        this.reqItems = reqItems;
    }

    protected RequestDetails(Parcel in) {
        reqID = in.readString();
        reqType = in.readString();
        reqDt = in.readString();
        image = in.readString();
        mannaID = in.readString();
        firstname = in.readString();
        lastname = in.readString();
        phone = in.readString();
        reqStatus = in.readString();
        reqAcpt_Rjct = in.readString();
        pickup_address = in.readParcelable(Address.class.getClassLoader());
        pickup_coords = in.readParcelable(Coord.class.getClassLoader());
        reqItems = in.createTypedArrayList(RequestItems.CREATOR);
    }

    public static final Creator<RequestDetails> CREATOR = new Creator<RequestDetails>() {
        @Override
        public RequestDetails createFromParcel(Parcel in) {
            return new RequestDetails(in);
        }

        @Override
        public RequestDetails[] newArray(int size) {
            return new RequestDetails[size];
        }
    };

    public String getReqID() {
        return reqID;
    }

    public void setReqID(String reqID) {
        this.reqID = reqID;
    }

    public String getReqType() {
        return reqType;
    }

    public void setReqType(String reqType) {
        this.reqType = reqType;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMannaID() {
        return mannaID;
    }

    public void setMannaID(String mannaID) {
        this.mannaID = mannaID;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getReqStatus() {
        return reqStatus;
    }

    public void setReqStatus(String reqStatus) {
        this.reqStatus = reqStatus;
    }

    public String getReqAcpt_Rjct() {
        return reqAcpt_Rjct;
    }

    public void setReqAcpt_Rjct(String reqAcpt_Rjct) {
        this.reqAcpt_Rjct = reqAcpt_Rjct;
    }

    public Address getPickup_address() {
        return pickup_address;
    }

    public void setPickup_address(Address pickup_address) {
        this.pickup_address = pickup_address;
    }

    public Coord getPickup_coords() {
        return pickup_coords;
    }

    public void setPickup_coords(Coord pickup_coords) {
        this.pickup_coords = pickup_coords;
    }

    public ArrayList<RequestItems> getReqItems() {
        return reqItems;
    }

    public void setReqItems(ArrayList<RequestItems> reqItems) {
        this.reqItems = reqItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reqID);
        dest.writeString(reqType);
        dest.writeString(reqDt);
        dest.writeString(image);
        dest.writeString(mannaID);
        dest.writeString(firstname);
        dest.writeString(lastname);
        dest.writeString(phone);
        dest.writeString(reqStatus);
        dest.writeString(reqAcpt_Rjct);
        dest.writeParcelable(pickup_address, flags);
        dest.writeParcelable(pickup_coords, flags);
        dest.writeTypedList(reqItems);
    }
}
