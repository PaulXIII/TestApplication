package com.example.paul.testapplication.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RatesResponse(
    @Json(name = "base")
    val base: String,
    @Json(name = "date")
    val date: String,
    @Json(name = "rates")
    val rates: Map<String, Double>
)