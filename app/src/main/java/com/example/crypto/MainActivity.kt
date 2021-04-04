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
                binding.tvCoin.text = it.coin
                binding.tvDifficulty.text = String.format("%.0f", (it.difficulty))
                binding.tvNetworkHashrate.text = String.format("%.0f", (it.network_hashrate))
                binding.tvPrice.text = String.format("%.2f", (it.price))
                binding.tvRewardBlock.text = String.format("%.6f", (it.reward_block))

                difficulty = it.difficulty
                networkHash = it.network_hashrate
                rewardBlock = it.reward_block
                price = it.price
            }
        })


        binding.btnShowProfit.setOnClickListener{
            blockTime = difficulty / networkHash
            binding.tvBlockTime.text = blockTime.toString()
            Log.d("milk", "block time: $blockTime")

            try {
                val userHashRateString = binding.etHashrate.text.toString()
                userHashRate = userHashRateString.toDouble() * 1000000.0
                binding.tvUserHashrate.text = (userHashRate / 1000000.0).toString()
            } catch (e: Exception) {
                Toast.makeText(this, "Enter your hashrate", Toast.LENGTH_SHORT).show()
            }



            userRatio = userHashRate / networkHash

            blockPerHour = 3600 / blockTime
            binding.tvBlockPerHour.text = blockPerHour.toString()

            coinPerHour = rewardBlock * userRatio * blockPerHour
//            tvCoinPerHour.text = coinPerHour.toString()
            binding.tvCoinPerHour.text = String.format("%.12f", coinPerHour)

            profit = price * coinPerHour
            binding.tvProfit.text = String.format("%.12f", profit)
        }

    }

}