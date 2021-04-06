package com.example.crypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.crypto.databinding.ActivityMainBinding

import com.example.crypto.viewmodel.ViewModelCrypto
import java.lang.Exception
import java.math.RoundingMode
import java.text.DecimalFormat

import kotlin.math.round
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    var difficulty: Double = 0.0
    var networkHash: Double = 0.0
    var blockTime: Double = 0.0
    var userHashRate: Double = 0.0
    var userRatio: Double = 0.0
    var blockPerHour: Double = 0.0
    var rewardBlock: Double = 0.0
    var coinPerHour: Double = 0.0
    var price: Double = 0.0
    var profit: Double = 0.0
    var exchangeRate: Double = 0.0
    var priceRub: Double = 0.0
    var blockPerDay: Double = 0.0
    var coinPerDay: Double = 0.0
    var blockPerWeek: Double = 0.0
    var coinPerWeek: Double = 0.0
    var blockPerMonth: Double = 0.0
    var coinPerMonth: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val viewModelCrypto = ViewModelProvider(this).get(ViewModelCrypto::class.java)


        //userhashrate
        //network_hashrate
        //difficulty
        //reward_block
        //"
        //blocktime=diificulty/nethash"
        //userratio=userhashrate/nethashrate
        //blockperhour=3600/blocktime
        //coinhour=reward*userratio*blockperhour

        viewModelCrypto.coinLiveData.observe(this, Observer {
            it?.let {
                price = it.price
                binding.tvPrice.text = String.format("%.2f $", price)
                difficulty = it.difficulty
                networkHash = it.network_hashrate
                rewardBlock = it.reward_block
            }
        })

        viewModelCrypto.getExchangeLiveData()
        viewModelCrypto.exchangeLiveData.observe(this, Observer {
            it?.let {
                exchangeRate = it.rates.RUB
            }
        })

        viewModelCrypto.zipLiveData.observe(this, Observer {
            price = it.first!!
            exchangeRate = it.second!!
            priceRub = price * exchangeRate
            Log.d("milk", "exchange: $exchangeRate / price: $price")
            binding.tvPriceRub.text = String.format("%.0f P", priceRub)
        })


        binding.btnShowProfit.setOnClickListener{
            blockTime = difficulty / networkHash

            Log.d("milk", "block time: $blockTime")

            try {
                val userHashRateString = binding.etHashrate.text.toString()
                userHashRate = userHashRateString.toDouble() * 1000000.0
                binding.tvUserHashrate.text = String.format("%.2f Mh/sec", userHashRate / 1000000.0)
            } catch (e: Exception) {
                Toast.makeText(this, "Enter your hashrate", Toast.LENGTH_SHORT).show()
            }
            userRatio = userHashRate / networkHash


            blockPerHour = 3600 / blockTime
            coinPerHour = rewardBlock * userRatio * blockPerHour

            binding.tvDollarHour.text = String.format("%.2f $", (coinPerHour * exchangeRate))
            binding.tvRubHour.text = String.format("%.0f P", (coinPerHour * priceRub))


            blockPerDay = 3600 * 24 / blockTime
            coinPerDay = rewardBlock * userRatio * blockPerDay

            binding.tvDollarDay.text = String.format("%.2f $", coinPerDay * exchangeRate)
            binding.tvRubDay.text = String.format("%.0f P", coinPerDay * priceRub)


            blockPerWeek = 3600 * 24 *7 / blockTime
            coinPerWeek = rewardBlock * userRatio * blockPerWeek

            binding.tvDollarWeek.text = String.format("%.2f $", coinPerWeek * exchangeRate)
            binding.tvRubWeek.text = String.format("%.0f P", coinPerWeek * priceRub)


            blockPerMonth = 3600 * 24 *30 / blockTime
            coinPerMonth = rewardBlock * userRatio * blockPerMonth

            binding.tvDollarMonth.text = String.format("%.2f $", coinPerMonth * exchangeRate)
            binding.tvRubMonth.text = String.format("%.0f P", coinPerMonth * priceRub)


        }

    }

}