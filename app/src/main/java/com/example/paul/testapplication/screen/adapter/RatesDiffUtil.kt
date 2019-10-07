package com.example.paul.testapplication.screen.adapter

import android.support.v7.util.DiffUtil

class RatesDiffUtil : DiffUtil.ItemCallback<RateItem>() {

    override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.currencyName == newItem.currencyName
    }

    override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        val isNameTheSame = oldItem.currencyName == newItem.currencyName
        val isCurrencyTheSame = oldItem.currencyRate == newItem.currencyRate
        val isAmountTheSame = oldItem.amount == newItem.amount
        return isNameTheSame && isCurrencyTheSame && isAmountTheSame
    }
}