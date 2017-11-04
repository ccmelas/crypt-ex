package com.example.melas.cryptex.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable{
    private String name;
    private double btcConversionRate;
    private double ethConversionRate;

    public Currency(String name, double btcConversionRate, double ethConversionRate) {
        this.name = name;
        this.btcConversionRate = btcConversionRate;
        this.ethConversionRate = ethConversionRate;
    }

    protected Currency(Parcel in) {
        name = in.readString();
        btcConversionRate = in.readDouble();
        ethConversionRate = in.readDouble();
    }

    public static final Parcelable.Creator<Currency> CREATOR = new Parcelable.Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeDouble(btcConversionRate);
        parcel.writeDouble(ethConversionRate);
    }

    public String getName() {
        return name;
    }

    public double getBtcConversionRate() {
        return btcConversionRate;
    }

    public double getEthConversionRate() {
        return ethConversionRate;
    }
}
