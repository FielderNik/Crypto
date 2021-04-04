package com.example.crypto

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

import com.example.crypto.viewmodel.ViewModelCrypto
import java.math.RoundingMode
import java.text.DecimalFormat

import kotlin.math.round
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity() {
    lateinit var tvCoin: TextView
    lateinit var tvDifficulty: TextView
    lateinit var tvNetworkHashrate: TextView
    lateinit var tvPrice: TextView
    lateinit var tvRewardBlock: TextView
    lateinit var btnShowProfit: Button
    lateinit var tvBlockTime: TextView
    lateinit var etUserHashRate: EditText
    lateinit var tvUserHashRate: TextView
    lateinit var tvBlockPerHour: TextView
    lateinit var tvCoinPerHour: TextView
    lateinit var tvProfit: TextView
    var difficulty: Double = 0.0
    var networkHash: Double = 0.0
    var blockTime: Double = 0.0
    var userHashRate: Double = 100.0
    var userRatio: Double = 0.0
    var blockPerHour: Double = 0.0
    var rewardBlock: Double = 0.0
    var coinPerHour: Double = 0.0
    var price: Double = 0.0
    var profit: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvCoin = findViewById(R.id.tvCoin)
        tvDifficulty = findViewById(R.id.tvDifficulty)
        tvNetworkHashrate = findViewById(R.id.tvNetworkHashrate)
        tvPrice = findViewById(R.id.tvPrice)
        tvRewardBlock = findViewById(R.id.tvRewardBlock)
        btnShowProfit = findViewById(R.id.btnShowProfit)
        tvBlockTime =findViewById(R.id.tvBlockTime)
        etUserHashRate = findViewById(R.id.etHashrate)
        tvUserHashRate = findViewById(R.id.tvUserHashrate)
        tvBlockPerHour = findViewById(R.id.tvBlockPerHour)
        tvCoinPerHour = findViewById(R.id.tvCoinPerHour)
        tvProfit = findViewById(R.id.tvProfit)

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
                tvCoin.text = it.coin
                tvDifficulty.text = String.format("%.0f", (it.difficulty))
                tvNetworkHashrate.text = String.format("%.0f", (it.network_hashrate))
                tvPrice.text = String.format("%.2f", (it.price))
                tvRewardBlock.text = String.format("%.6f", (it.reward_block))
                difficulty = it.difficulty
                networkHash = it.network_hashrate
                rewardBlock = it.reward_block
                price = it.price
            }
        })


        btnShowProfit.setOnClickListener{
            blockTime = difficulty / networkHash
            tvBlockTime.text = blockTime.toString()
            Log.d("milk", "block time: $blockTime")

            val userHashRateString = etUserHashRate.text.toString()
            userHashRate = userHashRateString.toDouble() * 1000000.0
            tvUserHashRate.text = (userHashRate / 1000000.0).toString()

            userRatio = userHashRate / networkHash

            blockPerHour = 3600 / blockTime
            tvBlockPerHour.text = blockPerHour.toString()

            coinPerHour = rewardBlock * userRatio * blockPerHour
//            tvCoinPerHour.text = coinPerHour.toString()
            tvCoinPerHour.text = String.format("%.12f", coinPerHour)

            profit = price * coinPerHour
            tvProfit.text = String.format("%.12f", profit)
//            tvProfit.text = profit.toString()


        }

    }

}