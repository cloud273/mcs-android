package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerPassword(inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
    }

    override fun getErrorText(): String? {
        val text = value as String
        return if (text.isEmpty()) {
            super.getErrorText()
        } else {
            if (text.length < 6) {
                "Too_short_password_message".localized
            } else {
                null
            }
        }
    }
}