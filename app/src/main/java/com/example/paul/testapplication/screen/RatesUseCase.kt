package com.example.paul.testapplication.screen

import android.util.Log
import com.example.paul.testapplication.model.RatesResponse
import com.example.paul.testapplication.network.NetworkService
import com.example.paul.testapplication.screen.adapter.RateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext


class RatesUseCase {

    suspend fun getRates(): List<RateItem> =
        withContext(Dispatchers.IO) {
            var list: List<RateItem> = emptyList()
            var count = Int.MAX_VALUE
            repeat(count) {
                val request = NetworkService.getRatesAsync("EUR")
                try {
                    val response = request.await()
                    if (response.isSuccessful) {
                        val rateResponse = response.body()
                        rateResponse?.let {
                            list = rearrangeRates(it)
                        }
                        Log.d(TAG, "Success")
                    } else {
                        Log.e(TAG, "Failure")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Error $e")
                }
                count--
                delay(1000L)
            }
            list
        }

    private fun rearrangeRates(it: RatesResponse): List<RateItem> {
        val list = ArrayList<RateItem>()
        for ((key, value) in it.rates) {
            list.add(RateItem(currencyName = key, currencyRate = value))
        }
        if (list.isNotEmpty()) {
            val initAmount = 1.0
            val initRate = list[0].currencyRate
            for (i in 0 until list.size) {
                if (i == 0) {
                    list[i].amount = initAmount
                } else {
                    val amount = (initAmount / initRate) * (list[i].currencyRate)
                    list[i].amount = Math.round(amount * 100.0) / 100.0
                }

            }
        }
        return list
    }

    companion object{
        const val TAG = "TAG"
    }


}