package com.example.paul.testapplication.screen.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.paul.testapplication.R
import kotlinx.android.synthetic.main.item_rates.view.*


class RatesAdapter(var list: ArrayList<RateItem>) : RecyclerView.Adapter<RatesAdapter.ViewHolder>() {

    private var onBind: Boolean = false

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(p0.context)
                .inflate(R.layout.item_rates, p0, false) as View
        return ViewHolder(itemView, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        val item = list[p1]
        onBind = true
        p0.run {
            layout.setOnClickListener { swapItems(p1) }
            currencyName.text = item.currencyName
            currencyValue.setText(item.amount.toString(), TextView.BufferType.EDITABLE)
        }
        onBind = false
    }

    private val listener = object : AmountListener {
        override fun amountChange(amount: Double, position: Int) {
            val rate = list[position].currencyRate
            calculateAmount(amount / rate)
        }
    }

    fun calculateAmount(amount: Double) {
        for (item in list) {
            item.amount = Math.round((amount * item.currencyRate) * 100.0) / 100.0
        }
        if (!onBind)
            notifyDataSetChanged()
    }

    fun updateRates(rates: ArrayList<RateItem>) {
        if (list.isNotEmpty()) {
            if (list.size == rates.size) {
                for (item in list.indices) {
                    if (list[item].currencyName == rates[item].currencyName) {
                        list[item].currencyRate = rates[item].currencyRate
                    }
                }
            }
        } else {
            list = rates
        }
        notifyDataSetChanged()
    }

    private fun swapItems(position: Int) {
        val firstItem = list[0]
        val targetItem = list[position]
        list.run {
            remove(firstItem)
            remove(targetItem)

            add(0, targetItem)
            add(position, firstItem)
        }

        notifyDataSetChanged()
    }


    class ViewHolder(itemView: View, listener: AmountListener) : RecyclerView.ViewHolder(itemView) {

        val layout: ConstraintLayout = itemView.layout
        val currencyName: AppCompatTextView = itemView.item_currency_name
        val currencyValue: AppCompatEditText = itemView.item_currency_value

        init {
            currencyValue.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty())
                        listener.amountChange(s.toString().toDouble(), adapterPosition)
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        }


    }

    interface AmountListener {

        fun amountChange(amount: Double, position: Int)

    }


}