package com.example.crypto.api

import com.example.crypto.service.ServiceCrypto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {

    val BASE_URL = "https://api.minerstat.com"


    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val coinService: ServiceCrypto = retrofit.create(ServiceCrypto::class.java)




}