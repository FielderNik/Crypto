package com.example.crypto.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.crypto.api.Api
import com.example.crypto.model.Coin
import com.example.crypto.model.CoinItem
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModelCrypto(application: Application) : AndroidViewModel(application) {
    val coinLiveData: LiveData<CoinItem>
    val api: Api
    private val privateCoinLiveData: MutableLiveData<CoinItem>

    init {
        api = Api()
        privateCoinLiveData = MutableLiveData<CoinItem>()
        coinLiveData = privateCoinLiveData
        getCoinLiveData("ETH")
    }



    fun getCoinLiveData(coin: String) {
        api.coinService.getCoinInfo(coin).enqueue(object : Callback<Coin>{
            override fun onResponse(call: Call<Coin>, response: Response<Coin>) {
                privateCoinLiveData.value = response.body()?.get(0)
                Log.d("milk", "response: ${response.body()}")
            }

            override fun onFailure(call: Call<Coin>, t: Throwable) {
                Log.d("milk", "error: $t")

//                Toast.makeText(getApplication(), "nre", Toast.LENGTH_SHORT).show()
            }

        })
    }
}