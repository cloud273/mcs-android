package com.cloud273.mcs.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.mcs.extension.onTapListener
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.support.NavConst

abstract class McsFragment: Fragment() {

    override fun onStop() {
        super.onStop()
        dismissFocusIfNeed()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSwipeRefreshLayout()
    }

    private fun setupSwipeRefreshLayout() {
        swipeRefreshLayout()?.apply {
            setOnRefreshListener {
                refresh()
            }
        }
    }

    protected open fun swipeRefreshLayout(): SwipeRefreshLayout? = null

    protected open fun refresh() = endRefresh()

    protected fun endRefresh() {
        swipeRefreshLayout()?.apply {
            if (isRefreshing) {
                isRefreshing = false
            }
        }
    }

    protected fun setupPicker(field: EditText, listener: () -> Unit) {
        field.onTapListener {
            dismissFocusIfNeed()
            listener()
        }
    }

    protected fun focus(field: EditText, showKb: Boolean = true) {
        field.requestFocus()
        if (showKb) {
            val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
        }
    }

    protected open fun popBack() = NavHostFragment.findNavController(this).popBackStack()

    protected fun navigate(resId: Int, args: Bundle? = null, anim: Boolean = true) {
        var navOptions: NavOptions? = null
        if (anim) {
            navOptions = NavConst.anim
        }
        NavHostFragment.findNavController(this).navigate(resId, args, navOptions)
    }

}