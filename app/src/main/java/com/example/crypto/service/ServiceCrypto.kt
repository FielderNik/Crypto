package com.example.crypto.service

import com.example.crypto.model.Coin
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ServiceCrypto {

    @GET("/v2/coins?list=ETH")
    fun getCoinInfo(
        @Query("list")
        coin: String
    ) : Call<Coin>



}