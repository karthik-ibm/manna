package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Requests implements Parcelable {
    String mannaID,mobile,fname,lname,reqID,reqType,reqCat,reqById;
    String reqDt,reqExp;
    String reqtm;
    Address pickupAddress=new Address();
    Coord pickupCoord = new Coord();
    ArrayList<RequestItems> reqItems = new ArrayList<>();
    ArrayList<RequestDetails> outgoingReqs = new ArrayList<>();
    ArrayList<RequestDetails> incomingReqs = new ArrayList<>();

    public Requests(String mannaID, String mobile, String fname, String lname, String reqID, String reqType, String reqCat, String reqById, String reqDt, String reqExp, String reqtm, Address pickupAddress, Coord pickupCoord, ArrayList<RequestItems> reqItems, ArrayList<RequestDetails> outgoingReqs, ArrayList<RequestDetails> incomingReqs) {
        this.mannaID = mannaID;
        this.mobile = mobile;
        this.fname = fname;
        this.lname = lname;
        this.reqID = reqID;
        this.reqType = reqType;
        this.reqCat = reqCat;
        this.reqById = reqById;
        this.reqDt = reqDt;
        this.reqExp = reqExp;
        this.reqtm = reqtm;
        this.pickupAddress = pickupAddress;
        this.pickupCoord = pickupCoord;
        this.reqItems = reqItems;
        this.outgoingReqs = outgoingReqs;
        this.incomingReqs = incomingReqs;
    }

    protected Requests(Parcel in) {
        mannaID = in.readString();
        mobile = in.readString();
        fname = in.readString();
        lname = in.readString();
        reqID = in.readString();
        reqType = in.readString();
        reqCat = in.readString();
        reqById = in.readString();
        reqDt = in.readString();
        reqExp = in.readString();
        reqtm = in.readString();
        pickupAddress = in.readParcelable(Address.class.getClassLoader());
        pickupCoord = in.readParcelable(Coord.class.getClassLoader());
        reqItems = in.createTypedArrayList(RequestItems.CREATOR);
    }

    public static final Creator<Requests> CREATOR = new Creator<Requests>() {
        @Override
        public Requests createFromParcel(Parcel in) {
            return new Requests(in);
        }

        @Override
        public Requests[] newArray(int size) {
            return new Requests[size];
        }
    };

    public Requests() {

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

    public String getReqCat() {
        return reqCat;
    }

    public void setReqCat(String reqCat) {
        this.reqCat = reqCat;
    }

    public String getReqById() {
        return reqById;
    }

    public void setReqById(String reqById) {
        this.reqById = reqById;
    }

    public String getReqDt() {
        return reqDt;
    }

    public void setReqDt(String reqDt) {
        this.reqDt = reqDt;
    }

    public String getReqExp() {
        return reqExp;
    }

    public void setReqExp(String reqExp) {
        this.reqExp = reqExp;
    }

    public String getReqtm() {
        return reqtm;
    }

    public void setReqtm(String reqtm) {
        this.reqtm = reqtm;
    }

    public Address getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(Address pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Coord getPickupCoord() {
        return pickupCoord;
    }

    public void setPickupCoord(Coord pickupCoord) {
        this.pickupCoord = pickupCoord;
    }

    public ArrayList<RequestItems> getReqItems() {
        return reqItems;
    }

    public void setReqItems(ArrayList<RequestItems> reqItems) {
        this.reqItems = reqItems;
    }

    public ArrayList<RequestDetails> getOutgoingReqs() {
        return outgoingReqs;
    }

    public void setOutgoingReqs(ArrayList<RequestDetails> outgoingReqs) {
        this.outgoingReqs = outgoingReqs;
    }

    public ArrayList<RequestDetails> getIncomingReqs() {
        return incomingReqs;
    }

    public void setIncomingReqs(ArrayList<RequestDetails> incomingReqs) {
        this.incomingReqs = incomingReqs;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mannaID);
        dest.writeString(mobile);
        dest.writeString(fname);
        dest.writeString(lname);
        dest.writeString(reqID);
        dest.writeString(reqType);
        dest.writeString(reqCat);
        dest.writeString(reqById);
        dest.writeString(reqDt);
        dest.writeString(reqExp);
        dest.writeString(reqtm);
        dest.writeParcelable(pickupAddress, flags);
        dest.writeParcelable(pickupCoord, flags);
        dest.writeTypedList(reqItems);
    }
}
