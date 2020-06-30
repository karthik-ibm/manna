package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Cart implements Parcelable {
    ArrayList<CatalogItems> catalogItemsArrayList = new ArrayList<>();
    ArrayList<RequestItems> requestItemsArrayList = new ArrayList<>();
    Requests requests;
    int count;

    public Cart()
    {

    }

    protected Cart(Parcel in) {
        catalogItemsArrayList = in.createTypedArrayList(CatalogItems.CREATOR);
        requestItemsArrayList = in.createTypedArrayList(RequestItems.CREATOR);
        requests = in.readParcelable(Requests.class.getClassLoader());
        count = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    public ArrayList<CatalogItems> getCatalogItemsArrayList() {
        return catalogItemsArrayList;
    }

    public void setCatalogItemsArrayList(ArrayList<CatalogItems> catalogItemsArrayList) {
        this.catalogItemsArrayList = catalogItemsArrayList;
    }

    public ArrayList<RequestItems> getRequestItemsArrayList() {
        return requestItemsArrayList;
    }

    public void setRequestItemsArrayList(ArrayList<RequestItems> requestItemsArrayList) {
        this.requestItemsArrayList = requestItemsArrayList;
    }

    public Requests getRequests() {
        return requests;
    }

    public void setRequests(Requests requests) {
        this.requests = requests;
    }

    public int getCount() {
        return count;
    }




    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(catalogItemsArrayList);
        dest.writeTypedList(requestItemsArrayList);
        dest.writeParcelable(requests, flags);
        dest.writeInt(count);
    }
}
