package com.cloud273.mcs.textController

import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout


open class McsEditTextControllerDefault(protected val inputLayout: TextInputLayout, protected val isRequired: Boolean) {

    private var currentText = inputLayout.editText?.text.toString()
    init {
        inputLayout.editText!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != currentText) {
                    currentText = p0.toString()
                    showErrorIfNeed()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }


    open var value: Any?
        get() = inputLayout.editText!!.text.toString()
        set(value) {
            if (value == null) {
                inputLayout.editText!!.text = null
            } else {
                if (value is String) {
                    inputLayout.editText!!.setText(value)
                }
            }
        }

    private var _maxTextLength: Int? = null
    var maxTextLength: Int?
        get() = _maxTextLength
        set(value) {
            _maxTextLength = value
            if (value != null) {
                inputLayout.editText!!.filters = arrayOf<InputFilter>(LengthFilter(value))
            } else {
                inputLayout.editText!!.filters = null
            }
        }

    val isValid: Boolean
        get() {
            return inputLayout.error == null
        }

    fun showErrorIfNeed() {
        inputLayout.error = getErrorText()
    }

    open fun getErrorText(): String? {
        return if (isRequired && (value == null || (value is String && (value as String).isEmpty()))) {
            "Required_asterisk".localized
        } else {
            null
        }
    }

}