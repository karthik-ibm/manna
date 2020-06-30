package com.ibm.manna.pojo;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Catalog implements Parcelable {
    String image;
    String reqCat,category,cat_descr;
    ArrayList<CatalogItems> Item =new ArrayList<>();

    public Catalog(String image, String reqCat, String category, String cat_descr, ArrayList<CatalogItems> item) {
        this.image = image;
        this.reqCat = reqCat;
        this.category = category;
        this.cat_descr = cat_descr;
        Item = item;
    }

    protected Catalog(Parcel in) {
        image = in.readString();
        reqCat = in.readString();
        category = in.readString();
        cat_descr = in.readString();
        Item = in.createTypedArrayList(CatalogItems.CREATOR);
    }

    public static final Creator<Catalog> CREATOR = new Creator<Catalog>() {
        @Override
        public Catalog createFromParcel(Parcel in) {
            return new Catalog(in);
        }

        @Override
        public Catalog[] newArray(int size) {
            return new Catalog[size];
        }
    };

    public Catalog() {

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReqCat() {
        return reqCat;
    }

    public void setReqCat(String reqCat) {
        this.reqCat = reqCat;
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

    public ArrayList<CatalogItems> getItem() {
        return Item;
    }

    public void setItem(ArrayList<CatalogItems> item) {
        Item = item;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(reqCat);
        dest.writeString(category);
        dest.writeString(cat_descr);
        dest.writeTypedList(Item);
    }
}
