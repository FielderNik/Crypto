package com.example.crypto.service


import com.example.crypto.model.ExchangeUsdRub
import retrofit2.Call
import retrofit2.http.GET

interface ServiceExchange {

    @GET("/api/latest?base=USD&symbols=RUB")
    fun getExchangeData(

    ): Call<ExchangeUsdRub>
}