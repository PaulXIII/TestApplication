package com.example.paul.testapplication.screen

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        presenter = MainPresenter(this)
        val recyclerView = recycler
        adapter = RatesAdapter(ArrayList())
        recyclerView.adapter = adapter
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        presenter.loadRates()

    }

    override fun showRates(list: ArrayList<RateItem>) {
        adapter?.updateRates(list)
    }
}
