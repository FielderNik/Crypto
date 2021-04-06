package com.example.crypto.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.crypto.api.Api
import com.example.crypto.api.ApiExchange
import com.example.crypto.model.Coin
import com.example.crypto.model.CoinItem
import com.example.crypto.model.ExchangeUsdRub
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCrypto(application: Application) : AndroidViewModel(application) {
    val coinLiveData: LiveData<CoinItem>
    val exchangeLiveData: LiveData<ExchangeUsdRub>
    val api: Api
    val apiExchange: ApiExchange
    private val privateCoinLiveData: MutableLiveData<CoinItem>
    private val privateExchangeLiveData : MutableLiveData<ExchangeUsdRub>
    val exValue: MutableLiveData<Double>
    val coinValue: MutableLiveData<Double>
    val mediatorLiveData: MediatorLiveData<Pair<Double?, String?>>

    var zipLiveData: LiveData<Pair<Double, Double>>


    init {
        api = Api()
        apiExchange = ApiExchange()
        privateCoinLiveData = MutableLiveData<CoinItem>()
        coinLiveData = privateCoinLiveData
        getCoinLiveData("ETH")

        privateExchangeLiveData = MutableLiveData()
        exchangeLiveData = privateExchangeLiveData

        exValue = MutableLiveData<Double>()
        coinValue = MutableLiveData()
        mediatorLiveData = MediatorLiveData()

        zipLiveData = MutableLiveData()
        zipLiveData = zipLiveData(exValue, coinValue)


        getExchangeLiveData()



//        mediatorLiveData.addSource(exValue) { value -> mediatorLiveData.value = combineLatestData(exValue, coinValue) }
//        mediatorLiveData.addSource(coinValue) {value -> mediatorLiveData.value = combineLatestData(exValue, coinValue)}



    }

    private fun combineLatestData(
            fistElement: LiveData<Double>,
            secondElement: LiveData<String>
    ): Pair<Double?, String?> {

        val fistElement = fistElement.value
        val secondElement = secondElement.value


        return Pair(first = fistElement, second = secondElement)
    }

    fun <A, B> zipLiveData(a: LiveData<A>, b: LiveData<B>): LiveData<Pair<A, B>>{
        return MediatorLiveData<Pair<A, B>>().apply {
            var lastA: A? = null
            var lastB: B? = null

            fun update() {
                val localLastA = lastA
                val localLastB = lastB
                if (localLastA != null && localLastB != null)
                    this.value = Pair(localLastA, localLastB)
            }
            addSource(a) {
                lastA = it
                update()
            }
            addSource(b) {
                lastB = it
                update()
            }

            }
        }



    fun getCoinLiveData(coin: String) {
        api.coinService.getCoinInfo(coin).enqueue(object : Callback<Coin> {
            override fun onResponse(call: Call<Coin>, response: Response<Coin>) {
                privateCoinLiveData.value = response.body()?.get(0)
                coinValue.value = response.body()?.get(0)?.price

                Log.d("milk", "response: ${response.body()}")
            }

            override fun onFailure(call: Call<Coin>, t: Throwable) {
                Log.d("milk", "error: $t")
//                Toast.makeText(getApplication(), "nre", Toast.LENGTH_SHORT).show()
            }

        })
    }



    fun getExchangeLiveData(){
        apiExchange.exchangeService.getExchangeData().enqueue(object : Callback<ExchangeUsdRub> {
            override fun onResponse(call: Call<ExchangeUsdRub>, response: Response<ExchangeUsdRub>) {
                privateExchangeLiveData.value = response.body()
                exValue.value = response.body()?.rates?.RUB
                Log.d("milk", "response: ${response.body()}")
//                Log.d("milk", "response: ${response.body()?.quote?.RUB}")
            }

            override fun onFailure(call: Call<ExchangeUsdRub>, t: Throwable) {
                Log.d("milk", "error: $t")
            }

        })
    }
}