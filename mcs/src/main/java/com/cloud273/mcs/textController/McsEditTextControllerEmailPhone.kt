package com.cloud273.mcs.textController

import android.text.InputType
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.getVnMobileOrEmail
import com.dungnguyen.qdcore.extension.isEmail
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerEmailPhone(inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }

    override var value: Any?
        get() = (super.value as String).getVnMobileOrEmail()
        set(value) {
            super.value = value
        }

    override fun getErrorText(): String? {
        val text = super.value as String?
        return if (text == null || text.isEmpty()) {
            super.getErrorText()
        } else {
            if (text.isEmail() || text.getVnMobileOrEmail() != null) {
                null
            } else {
                "Invalid_phone_email".localized
            }
        }
    }
}