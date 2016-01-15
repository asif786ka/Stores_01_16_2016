package com.stores.dallas.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Asif on 01/11/2016.
 */
public class StoresInfo implements Parcelable {
    //Data Variables
    private String storeImageUrl;
    private String name;
    private int storeID;
    private String storeZipcode;
    private String storePhone;
    private String storeCity;
    private String storeAddress;

    public StoresInfo() {
    }

    public StoresInfo(String storeImageUrl, String name, int storeID, String storeZipcode, String storePhone, String storeCity, String storeAddress) {
        this.storeImageUrl = storeImageUrl;
        this.name = name;
        this.storeID = storeID;
        this.storeZipcode = storeZipcode;
        this.storePhone = storePhone;
        this.storeCity = storeCity;
        this.storeAddress = storeAddress;

    }

    //Getters and Setters
    public String getStoreImageUrl() {
        return storeImageUrl;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStoreID() {
        return storeID;
    }

    public void setStoreID(int storeID) {
        this.storeID = storeID;
    }

    public String getStoreZipcode() {
        return storeZipcode;
    }

    public void setStoreZipcode(String storeZipcode) {
        this.storeZipcode = storeZipcode;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getStoreCity() {
        return storeCity;
    }

    public void setStoreCity(String storeCity) {
        this.storeCity = storeCity;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(storeImageUrl);
        dest.writeString(name);
        dest.writeInt(storeID);
        dest.writeString(storeZipcode);
        dest.writeString(storePhone);
        dest.writeString(storeCity);
        dest.writeString(storeAddress);
    }

    private void readFromParcel(Parcel in) {
        storeImageUrl = in.readString();
        name = in.readString();
        storeID = in.readInt();
        storeZipcode = in.readString();
        storePhone = in.readString();
        storeCity = in.readString();
        storeAddress = in.readString();
    }

    public StoresInfo(Parcel in) {
        readFromParcel(in);
    }

    public static final Parcelable.Creator<StoresInfo> CREATOR = new Parcelable.Creator<StoresInfo>() {

        @Override
        public StoresInfo createFromParcel(Parcel source) {
            return new StoresInfo(source);
        }

        @Override
        public StoresInfo[] newArray(int size) {
            return new StoresInfo[size];
        }
    };
}
