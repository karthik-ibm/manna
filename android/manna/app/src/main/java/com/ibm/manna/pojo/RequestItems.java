package com.ibm.manna.pojo;

import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

public class RequestItems implements Parcelable {
    String category, cat_descr,item_id,item_descr,metric,image;
    int imageres,item_qty,avail_qty;

    public RequestItems(String category, String cat_descr, String item_id, String item_descr, String metric, String image, int imageres, int item_qty, int avail_qty) {
        this.category = category;
        this.cat_descr = cat_descr;
        this.item_id = item_id;
        this.item_descr = item_descr;
        this.metric = metric;
        this.image = image;
        this.imageres = imageres;
        this.item_qty = item_qty;
        this.avail_qty = avail_qty;
    }

    protected RequestItems(Parcel in) {
        category = in.readString();
        cat_descr = in.readString();
        item_id = in.readString();
        item_descr = in.readString();
        item_qty = in.readInt();
        metric = in.readString();
        image = in.readString();
        imageres = in.readInt();
    }

    public static final Creator<RequestItems> CREATOR = new Creator<RequestItems>() {
        @Override
        public RequestItems createFromParcel(Parcel in) {
            return new RequestItems(in);
        }

        @Override
        public RequestItems[] newArray(int size) {
            return new RequestItems[size];
        }
    };

    public RequestItems() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCat_descr() {
        return cat_descr;
    }

    public void setCat_descr(String cat_descr) {
        this.cat_descr = cat_descr;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }

    public String getMetric() {
        return metric;
    }

    public void setMetric(String metric) {
        this.metric = metric;
    }

    public String getItem_descr() {
        return item_descr;
    }

    public void setItem_descr(String item_descr) {
        this.item_descr = item_descr;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getImageres() {
        return imageres;
    }

    public void setImageres(int imageres) {
        this.imageres = imageres;
    }

    public int getAvail_qty() {
        return avail_qty;
    }

    public void setAvail_qty(int avail_qty) {
        this.avail_qty = avail_qty;
    }

    public static Creator<RequestItems> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category);
        dest.writeString(cat_descr);
        dest.writeString(item_id);
        dest.writeString(item_descr);
        dest.writeInt(item_qty);
        dest.writeString(metric);
        dest.writeString(image);
        dest.writeInt(imageres);
        dest.writeInt(avail_qty);
    }
}
