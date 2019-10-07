package com.example.paul.testapplication.screen

import com.example.paul.testapplication.screen.adapter.RateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class MainPresenter(private val view: MainView) {

    private val ratesUseCase: RatesUseCase by lazy {
        return@lazy RatesUseCase()
    }

    private var job: Job

    init {
        job = CoroutineScope(Dispatchers.Main).launch {
            val data: List<RateItem> = ratesUseCase.getRates()
            if (data.isNotEmpty()) {
                view.showRates(ArrayList(data))
            } else {
                view.showError("Message")
            }
        }
    }

    fun loadRates() {
        if (job.isActive.not()) {
            job = CoroutineScope(Dispatchers.Main).launch {
                val data: List<RateItem> = ratesUseCase.getRates()
                if (data.isNotEmpty()) {
                    view.showRates(ArrayList(data))
                } else {
                    view.showError("Message")
                }
            }
        }
    }


    fun cancelLoading() {
        job?.let {
            it.cancel()
        }
    }


}