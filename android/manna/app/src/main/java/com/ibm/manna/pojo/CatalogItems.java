package com.ibm.manna.pojo;

import android.os.Parcel;
import android.os.Parcelable;

public class CatalogItems implements Parcelable {
    String item_id, item_descr,metric;
    String image;
    double quantity;

    public CatalogItems(String item_id, String item_descr, String metric, String image, double quantity) {
        this.item_id = item_id;
        this.item_descr = item_descr;
        this.metric = metric;
        this.image = image;
        this.quantity = quantity;
    }

    public CatalogItems(String item_id, String item_descr, String metric, String image) {
        this.item_id = item_id;
        this.item_descr = item_descr;
        this.metric = metric;
        this.image = image;
    }

    protected CatalogItems(Parcel in) {
        item_id = in.readString();
        item_descr = in.readString();
        metric = in.readString();
        image = in.readString();
    }

    public static final Creator<CatalogItems> CREATOR = new Creator<CatalogItems>() {
        @Override
        public CatalogItems createFromParcel(Parcel in) {
            return new CatalogItems(in);
        }

        @Override
        public CatalogItems[] newArray(int size) {
            return new CatalogItems[size];
        }
    };

    public CatalogItems() {

    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_descr() {
        return item_descr;
    }

    public void setItem_descr(String item_descr) {
        this.item_descr = item_descr;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(item_id);
        dest.writeString(item_descr);
        dest.writeString(metric);
        dest.writeString(image);
    }
}
