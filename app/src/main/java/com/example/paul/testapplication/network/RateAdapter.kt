package com.example.paul.testapplication.network

import com.example.paul.testapplication.model.RatesResponse
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface RateAdapter {

    @GET("/latest")
    fun getRatesAsync(@Query("base") baseCurrency: String): Deferred<Response<RatesResponse>>
}