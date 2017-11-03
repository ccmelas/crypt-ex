package com.example.melas.cryptex.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable{
    private String name;
    private String shortCode;
    private String symbol;
    private double btcConversionRate;
    private double ethConversionRate;

    public Currency(String name, String shortCode, String symbol, double btcConversionRate, double ethConversionRate) {
        this.name = name;
        this.shortCode = shortCode;
        this.symbol = symbol;
        this.btcConversionRate = btcConversionRate;
        this.ethConversionRate = ethConversionRate;
    }

    protected Currency(Parcel in) {
        name = in.readString();
        shortCode = in.readString();
        symbol = in.readString();
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
        parcel.writeString(shortCode);
        parcel.writeString(symbol);
        parcel.writeDouble(btcConversionRate);
        parcel.writeDouble(ethConversionRate);
    }

    public String getName() {
        return name;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getBtcConversionRate() {
        return btcConversionRate;
    }

    public double getEthConversionRate() {
        return ethConversionRate;
    }
}
