package com.example.paul.testapplication.screen

import com.example.paul.testapplication.screen.adapter.RateItem


interface MainView {

    fun showRates(list: ArrayList<RateItem>)

    fun showError(message: String)
}