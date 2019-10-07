package com.example.paul.testapplication.screen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.paul.testapplication.R
import com.example.paul.testapplication.screen.adapter.RateItem
import com.example.paul.testapplication.screen.adapter.RatesAdapter
import kotlinx.android.synthetic.main.fragment_main.*


class MainFragment : Fragment(), MainView {

    private lateinit var presenter: MainPresenter
    var adapter: RatesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = MainPresenter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView = recycler
        adapter = RatesAdapter { item ->

        }
        recyclerView.adapter = adapter
        if (recyclerView.layoutManager == null) {
            val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            recyclerView.layoutManager = layoutManager
        }
        if (savedInstanceState == null)
            presenter.loadRates()
    }

    override fun onResume() {
        super.onResume()
        presenter.loadRates()
    }

    override fun onStop() {
        super.onStop()
        presenter.cancelLoading()
    }

    override fun showRates(list: ArrayList<RateItem>) {
        adapter?.submitList(list)
    }

    override fun showError(message: String) {
        Log.e("TAG", "Error message: $message")
    }
}
