package com.cloud273.mcs.textController

import android.text.InputType
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerCode(inputLayout: TextInputLayout) : McsEditTextControllerDefault(inputLayout, true) {

    init {
        inputLayout.editText!!.inputType = InputType.TYPE_CLASS_NUMBER
        maxTextLength = 6
    }

}