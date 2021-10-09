package com.cloud273.mcs.textController

import android.view.View
import android.widget.ImageButton
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.cloud273.mcs.R
import com.cloud273.mcs.extension.onTextChangedListener
import com.cloud273.mcs.util.toAppTimeString
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class McsEditTextControllerTime(inputLayout: TextInputLayout, isRequired: Boolean, title: String, fragment: Fragment, listener: OnSetInterface? = null): McsEditTextControllerCustomKeyboard(inputLayout, isRequired, fragment, listener) {

    init {
        createTimeKeyboard(title)
    }

    override fun updateTextField() {
        val date = value as Date?
        inputLayout.editText!!.setText(date?.toAppTimeString())
    }

    fun getAsClearableInput(onClearListener: (() -> Unit)? = null): RelativeLayout {
        val container = RelativeLayout(fragment.requireContext())
        val buttonLp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            addRule(RelativeLayout.CENTER_VERTICAL)
            addRule(RelativeLayout.ALIGN_PARENT_END)
            marginEnd = fragment.resources.getDimension(R.dimen.margin).toInt()
        }
        val inputLp = RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.MATCH_PARENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
        ).apply { addRule(RelativeLayout.CENTER_IN_PARENT) }
        val imageButton = ImageButton(fragment.requireContext()).apply {
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            setImageResource(R.drawable.ic_cancel)
            setOnClickListener {
                value = null
                inputLayout.editText?.clearFocus()
                inputLayout.clearFocus()
                onClearListener?.invoke()
            }
            visibility = if (value == null) View.INVISIBLE else View.VISIBLE
        }
        inputLayout.editText?.apply {
            onTextChangedListener { text ->
                imageButton.visibility = if (text.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
            }
        }

        container.addView(inputLayout, inputLp)
        container.addView(imageButton, buttonLp)
        return container
    }
}