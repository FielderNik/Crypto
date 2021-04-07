package com.example.crypto

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.crypto.databinding.ActivityMainBinding
import com.example.crypto.viewmodel.ViewModelThreads
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

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

    lateinit var unusualSymbols: DecimalFormatSymbols
    lateinit var myFormatter: DecimalFormat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

//        val viewModelCrypto = ViewModelProvider(this).get(ViewModelCrypto::class.java)
        val viewModelThreads = ViewModelProvider(this).get(ViewModelThreads::class.java)

        unusualSymbols = DecimalFormatSymbols()
        unusualSymbols.setGroupingSeparator(' ')
        myFormatter = DecimalFormat("###,### P", unusualSymbols)

        viewModelThreads.coinLiveData.observe(this, Observer {
            it?.let {
                price = it.price
                binding.tvPrice.text = String.format("%.2f $", price)
                difficulty = it.difficulty
                networkHash = it.network_hashrate
                rewardBlock = it.reward_block
                Log.d("milk", "it: $it")

            }
        })

        viewModelThreads.booleanLiveData.observe(this, Observer {
            if (it == true){
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
            }
        })

/*        viewModelCrypto.getExchangeLiveData()
        viewModelCrypto.exchangeLiveData.observe(this, Observer {
            it?.let {
                exchangeRate = it.rates.RUB
            }
        })*/

/*        viewModelCrypto.zipLiveData.observe(this, Observer {
            price = it.first!!
            exchangeRate = it.second!!
            priceRub = price * exchangeRate
            Log.d("milk", "exchange: $exchangeRate / price: $price")

        })*/

        viewModelThreads.getCourse()
        viewModelThreads.coinLD.observe(this, Observer {
            priceRub = it
//            binding.tvPriceRub.text = String.format("%.0f P", priceRub)
            binding.tvPriceRub.text = myFormatter.format(priceRub)

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


//            unusualSymbols.setDecimalSeparator('|')

            blockPerHour = 3600 / blockTime
            coinPerHour = rewardBlock * userRatio * blockPerHour

            binding.tvCoinHour.text = String.format("%.6f E", coinPerHour)
            binding.tvDollarHour.text = String.format("%.2f $", (coinPerHour * price))
//            binding.tvRubHour.text = String.format("% .0f P", (coinPerHour * priceRub))
            binding.tvRubHour.text = myFormatter.format(coinPerHour * priceRub)


            blockPerDay = 3600 * 24 / blockTime
            coinPerDay = rewardBlock * userRatio * blockPerDay

            binding.tvCoinDay.text = String.format("%.6f E", coinPerDay)
            binding.tvDollarDay.text = String.format("%.2f $", coinPerDay * price)
//            binding.tvRubDay.text = String.format("% .0f P", coinPerDay * priceRub)
            binding.tvRubDay.text = myFormatter.format(coinPerDay * priceRub)


            blockPerWeek = 3600 * 24 *7 / blockTime
            coinPerWeek = rewardBlock * userRatio * blockPerWeek

            binding.tvCoinWeek.text = String.format("%.6f E", coinPerWeek)
            binding.tvDollarWeek.text = String.format("%.2f $", coinPerWeek * price)
//            binding.tvRubWeek.text = String.format("% .0f P", coinPerWeek * priceRub)
            binding.tvRubWeek.text = myFormatter.format(coinPerWeek * priceRub)


            blockPerMonth = 3600 * 24 *30 / blockTime
            coinPerMonth = rewardBlock * userRatio * blockPerMonth

            binding.tvCoinMonth.text = String.format("%.6f E", coinPerMonth)
            binding.tvDollarMonth.text = String.format("%.2f $", coinPerMonth * price)
//            binding.tvRubMonth.text = String.format("%,.0f P", coinPerMonth * priceRub)
            binding.tvRubMonth.text = myFormatter.format(coinPerMonth * priceRub)


        }

    }

}