package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationResponse implements Parcelable {
    String status;
    int statusCode;
    ArrayList<UserProfile> user_profile = new ArrayList<>();
    ArrayList<Requests> my_request = new ArrayList<>();
    ArrayList<Requests> all_requests=new ArrayList<>();
    ArrayList<Catalog> cat_items= new ArrayList<>();

    public AuthenticationResponse(String status, int statusCode, ArrayList<UserProfile> user_profile, ArrayList<Requests> my_request, ArrayList<Requests> all_requests, ArrayList<Catalog> cat_items) {
        this.status = status;
        this.statusCode = statusCode;
        this.user_profile = user_profile;
        this.my_request = my_request;
        this.all_requests = all_requests;
        this.cat_items = cat_items;
    }

    protected AuthenticationResponse(Parcel in) {
        status = in.readString();
        statusCode = in.readInt();
        user_profile = in.createTypedArrayList(UserProfile.CREATOR);
        my_request = in.createTypedArrayList(Requests.CREATOR);
        all_requests = in.createTypedArrayList(Requests.CREATOR);
        cat_items = in.createTypedArrayList(Catalog.CREATOR);
    }

    public static final Creator<AuthenticationResponse> CREATOR = new Creator<AuthenticationResponse>() {
        @Override
        public AuthenticationResponse createFromParcel(Parcel in) {
            return new AuthenticationResponse(in);
        }

        @Override
        public AuthenticationResponse[] newArray(int size) {
            return new AuthenticationResponse[size];
        }
    };

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<UserProfile> getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(ArrayList<UserProfile> user_profile) {
        this.user_profile = user_profile;
    }

    public ArrayList<Requests> getMy_request() {
        return my_request;
    }

    public void setMy_request(ArrayList<Requests> my_request) {
        this.my_request = my_request;
    }

    public ArrayList<Requests> getAll_requests() {
        return all_requests;
    }

    public void setAll_requests(ArrayList<Requests> all_requests) {
        this.all_requests = all_requests;
    }

    public ArrayList<Catalog> getCat_items() {
        return cat_items;
    }

    public void setCat_items(ArrayList<Catalog> cat_items) {
        this.cat_items = cat_items;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status);
        dest.writeInt(statusCode);
        dest.writeTypedList(user_profile);
        dest.writeTypedList(my_request);
        dest.writeTypedList(all_requests);
        dest.writeTypedList(cat_items);
    }
}
