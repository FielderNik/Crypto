package com.example.crypto.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crypto.api.Api
import com.example.crypto.api.ApiExchange
import com.example.crypto.model.CoinItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class ViewModelThreads(application: Application) : AndroidViewModel(application) {

    val coinLD: MutableLiveData<Double>
    val coinLiveData: MutableLiveData<CoinItem>
    val api: Api
    val apiExchange: ApiExchange
    var coinPrice: Double
    var usdExchange: Double
    var priceRub: Double
    var booleanLiveData: MutableLiveData<Boolean>

    init {
        booleanLiveData = MutableLiveData()
        booleanLiveData.value = false
        coinLiveData = MutableLiveData()
        coinLD = MutableLiveData()
        api = Api()
        apiExchange = ApiExchange()
        coinPrice = 1.0
        usdExchange = 1.0
        priceRub = 0.0
    }


    fun getCourse(){
        var coinItem: CoinItem? = null
        CoroutineScope(Dispatchers.IO).launch {

            try {
                coinItem = api.coinService.getCoinInfo("ETH").execute().body()?.get(0)
                coinPrice = coinItem?.price!!
                usdExchange = apiExchange.exchangeService.getExchangeData().execute().body()?.rates?.RUB!!
                priceRub = coinPrice * usdExchange
            } catch (e: Exception){
                withContext(Dispatchers.Main){
                    booleanLiveData.value = true
                }

            }

            withContext(Dispatchers.Main){
                coinLD.value = priceRub
                coinLiveData.value = coinItem
            }

        }

    }
}