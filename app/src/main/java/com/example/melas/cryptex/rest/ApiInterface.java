package com.example.melas.cryptex.rest;

import com.example.melas.cryptex.models.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("data/pricemulti")
    Call<ApiResponse> getCurrencies(
            @Query(value = "fsyms", encoded = true) String query,
            @Query(value="tsyms", encoded = true) String query2);
}
