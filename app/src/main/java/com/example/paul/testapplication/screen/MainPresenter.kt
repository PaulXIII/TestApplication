package com.example.paul.testapplication.screen

import android.util.Log
import com.example.paul.testapplication.network.NetworkService
import com.example.paul.testapplication.screen.adapter.RateItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MainPresenter(val view: MainView) {

    fun loadRates() {
        var count = Int.MAX_VALUE
        GlobalScope.launch(Dispatchers.Main) {
            repeat(count) {
                val request = NetworkService.getRates("EUR")
                val response = request.await()
                if (response.isSuccessful) {
                    val rateResponse = response.body()
                    rateResponse?.let { it ->
                        val list = ArrayList<RateItem>()
                        for ((key, value) in it.rates) {
                            list.add(RateItem(key, value))
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

                        view.showRates(list)
                    }
                    Log.d("TAG", "Success")
                } else {
                    Log.e("TAG", "Failure")
                }
                count--
                delay(1000L)
            }
        }
    }
}