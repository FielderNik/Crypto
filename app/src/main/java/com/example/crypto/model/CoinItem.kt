package com.example.crypto.model

data class CoinItem(
    val algorithm: String,
    val coin: String,
    val difficulty: Double,
    val id: String,
    val name: String,
    val network_hashrate: Double,
    val price: Double,
    val reward: Double,
    val reward_block: Double,
    val reward_unit: String,
    val type: String,
    val updated: Int,
    val volume: Double
)