package com.cloud273.mcs.extension

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

fun EditText.onTapListener(listener: () -> Unit) {
    setOnClickListener {
        listener()
    }
    setOnFocusChangeListener { _, hasFocus ->
        if (hasFocus) {
            listener()
        }
    }
}

fun EditText.onTextChangedListener(listener: (text: CharSequence?) -> Unit) {
    addTextChangedListener(object: TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            listener(p0)
        }
    })
}