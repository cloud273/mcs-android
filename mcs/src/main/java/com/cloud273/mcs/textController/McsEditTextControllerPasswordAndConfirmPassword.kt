package com.cloud273.mcs.textController

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerPasswordAndConfirmPassword(private val inputLayout: TextInputLayout, private val reInputLayout: TextInputLayout) {

    private var currentText = inputLayout.editText?.text.toString()
    private var reCurrentText = reInputLayout.editText?.text.toString()

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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
        reInputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        reInputLayout.editText!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString() != reCurrentText) {
                    reCurrentText = p0.toString()
                    showErrorIfNeed()
                }
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }

    var value: String
        get() = inputLayout.editText!!.text.toString()
        set(value) {
            inputLayout.editText!!.setText(value)
        }

    val isValid: Boolean
        get() {
            return inputLayout.error == null && reInputLayout.error == null
        }

    fun showErrorIfNeed() {
        inputLayout.error = if (value.isEmpty()) {
            "Required_asterisk".localized
        } else {
            if (value.length < 6) {
                "Too_short_password_message".localized
            } else {
                null
            }
        }

        val text = reInputLayout.editText!!.text.toString()
        reInputLayout.error = if (text.isEmpty()) {
            "Required_asterisk".localized
        } else {
            if (text != value) {
                "Mismatch_password".localized
            } else {
                null
            }
        }

    }

}