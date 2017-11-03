package com.example.melas.cryptex.models;

import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;
import com.google.gson.internal.LinkedTreeMap;

public class ApiResponse {
    @SerializedName("BTC")
    private LinkedTreeMap btc;

    @SerializedName("ETH")
    private LinkedTreeMap eth;

    public LinkedTreeMap getBTC() {
        return btc;
    }

    public LinkedTreeMap getEth() {
        return eth;
    }
}
