package com.dungnguyen.qdcore.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.extension.addView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


open class ActionSheetDialogFragment : BottomSheetDialogFragment() {

    private data class ItemModel (val text: String, val res: Int?, val listener: () -> Unit)

    private var items = mutableListOf<ItemModel>()

    private var title: String? = null
    private var isLeft = false
    private var cancel: String? = null
    private var dismissOnClick = true

    fun setTitle(text: String): ActionSheetDialogFragment {
        this.title = text
        return this
    }

    fun aligment(isLeft: Boolean): ActionSheetDialogFragment {
        this.isLeft = isLeft
        return this
    }

    fun dismissOnClick(dismissOnClick: Boolean): ActionSheetDialogFragment {
        this.dismissOnClick = dismissOnClick
        return this
    }

    fun setDestructive(text: String) : ActionSheetDialogFragment {
        this.cancel = text
        return this
    }

    fun add(text: String, res: Int, listener: () -> Unit): ActionSheetDialogFragment {
        items.add(
            ItemModel(
                text,
                res,
                listener
            )
        )
        return this
    }

    fun add(text: String, listener: () -> Unit) : ActionSheetDialogFragment {
        items.add(
            ItemModel(
                text,
                null,
                listener
            )
        )
        return this
    }

    fun show(fragment: Fragment) {
        this.show(fragment.fragmentManager!!, null)
    }

    fun show(activity: AppCompatActivity) {
        this.show(activity.supportFragmentManager, null)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_action_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val itemsLayout = view.findViewById<LinearLayout>(R.id.itemsLayout)

        if (title != null) {
            val textView = textView(title!!, 14.toFloat(), Color.DKGRAY)
            itemsLayout.addView(textView, 0, 12, 0, 16)
        }
        for (item in items) {
            val textView = textView(item.text, 17.toFloat(), Color.BLACK)
            textView.setOnClickListener {
                if (dismissOnClick) {
                    dismiss()
                }
                item.listener()
            }
            itemsLayout.addView(textView, 0, 0, 0, 16)
        }
        if (cancel != null) {
            val divider = dividerView()
            itemsLayout.addView(divider, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1))

            val textView = textView(cancel!!, 17.toFloat(), Color.RED)
            textView.setOnClickListener {
                dismiss()
            }
            itemsLayout.addView(textView,0, 12, 0, 12)
        }

    }

    private fun dividerView() : View {
        val view = View(activity!!)
        view.setBackgroundColor(Color.LTGRAY)
        return view
    }

    private fun textView(text: String, size: Float, color: Int) : TextView {
        val textView = TextView(activity!!)
        textView.text = text
        textView.textSize = size
        textView.setTextColor(color)
        if (isLeft) {
            textView.gravity = Gravity.START
        } else {
            textView.gravity = Gravity.CENTER
        }
        return textView
    }


}