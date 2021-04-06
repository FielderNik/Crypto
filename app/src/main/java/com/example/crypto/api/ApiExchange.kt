package com.example.crypto.api

import com.example.crypto.service.ServiceCrypto
import com.example.crypto.service.ServiceExchange
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiExchange {

    val BASE_URL_EXCHANGE = "https://api.ratesapi.io"
    val retrofitExchange = Retrofit.Builder()
            .baseUrl(BASE_URL_EXCHANGE)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val exchangeService: ServiceExchange = retrofitExchange.create(ServiceExchange::class.java)


}