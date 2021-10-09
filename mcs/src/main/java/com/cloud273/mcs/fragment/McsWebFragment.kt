package com.cloud273.mcs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.mcs.R
import kotlinx.android.synthetic.main.fragment_mcs_web.*

open class McsWebFragment : McsFragment() {

    companion object {
        const val urlKey = "url"
        const val labelKey = "label"
    }

    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        url = arguments!!.getString(urlKey)!!
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mcs_web, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity is AppCompatActivity) {
            (activity as AppCompatActivity).supportActionBar?.title = arguments!!.getString(
                labelKey
            )
        } else {
            activity?.actionBar?.title = arguments!!.getString(labelKey)
        }
        refresh()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun refresh() {
        webView.loadUrl(url)
        endRefresh()
    }

}