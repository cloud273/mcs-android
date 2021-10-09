package com.cloud273.mcs.textController

import androidx.fragment.app.Fragment
import com.cloud273.backend.model.common.McsCity
import com.cloud273.backend.model.common.McsState
import com.cloud273.mcs.model.name
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.model.TextInterface
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerCity(inputLayout: TextInputLayout, private val title: String, state: McsState, fragment: Fragment, listener: OnSetInterface? = null): McsEditTextControllerCustomKeyboard(inputLayout, true, fragment, listener) {

    private class Model(val city: McsCity): TextInterface {
        override fun getText(): String {
            return city.name
        }
    }

    private var currentState: McsState? = null

    init {
        setState(state)
    }

    fun setState(state: McsState) {
        if (state.code != this.currentState?.code) {
            this.currentState = state
            val models = mutableListOf<Model>()
            for (item in state.cities) {
                models.add(Model(item))
            }
            createTextInterfaceKeyboard(title, "Cancel".localized, models)
        }
    }

    override var value: Any?
        get() = (super.value as Model).city
        set(value) {
            super.value = Model(value as McsCity)
        }

    override fun updateTextField() {
        val model = super.value as Model
        inputLayout.editText!!.setText(model.getText())
    }
}