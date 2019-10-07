package com.example.paul.testapplication.screen.adapter

import android.support.constraint.ConstraintLayout
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.paul.testapplication.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_rates.view.*


class RatesAdapter(private val callback: (item: RateItem) -> Unit) :
    ListAdapter<RateItem, RatesAdapter.ViewHolder>(RatesDiffUtil()) {

    override fun onCreateViewHolder(group: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(group.context)
            .inflate(R.layout.item_rates, group, false) as View
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(getItem(position), callback)
    }

//    private val listener = object : AmountListener {
//        override fun amountChange(amount: Double, position: Int) {
//            val rate = list[position].currencyRate
//            calculateAmount(amount / rate)
//        }
//    }

//    fun calculateAmount(amount: Double) {
//        for (item in list) {
//            item.amount = Math.round((amount * item.currencyRate) * 100.0) / 100.0
//        }
//        if (!onBind)
//            notifyDataSetChanged()
//    }

    class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        val layout: ConstraintLayout = itemView.layout
        private val currencyName: AppCompatTextView = itemView.tvItemCurrencyName
        private val currencyValue: AppCompatEditText = itemView.etItemCurrencyValue

        init {
//            item_cu.addTextChangedListener(object : TextWatcher {
//                override fun afterTextChanged(TAG: Editable?) {
//                    if (TAG.toString().isNotEmpty())
//                        listener.amountChange(TAG.toString().toDouble(), adapterPosition)
//                }
//
//                override fun beforeTextChanged(
//                    TAG: CharSequence?,
//                    start: Int,
//                    count: Int,
//                    after: Int
//                ) {
//                }
//
//                override fun onTextChanged(TAG: CharSequence?, start: Int, before: Int, count: Int) {
//
//                }
//            })
        }

        fun bind(item: RateItem, callback: (item: RateItem) -> Unit) {
            currencyName.text = item.currencyName
            currencyValue.setText(item.amount.toString(), TextView.BufferType.EDITABLE)
        }


    }

}