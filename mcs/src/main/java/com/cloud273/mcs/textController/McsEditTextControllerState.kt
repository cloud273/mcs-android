package com.cloud273.mcs.textController

import androidx.fragment.app.Fragment
import com.cloud273.backend.model.common.McsCountry
import com.cloud273.backend.model.common.McsState
import com.cloud273.mcs.model.name
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.model.TextInterface
import com.google.android.material.textfield.TextInputLayout

class McsEditTextControllerState(inputLayout: TextInputLayout, private val title: String, country: McsCountry, fragment: Fragment, listener: OnSetInterface? = null): McsEditTextControllerCustomKeyboard(inputLayout, true, fragment, listener) {

    private class Model(val state: McsState): TextInterface {
        override fun getText(): String {
            return state.name
        }
    }

    private var currentCountry: McsCountry? = null

    init {
        setCountry(country)
    }

    fun setCountry(country: McsCountry) {
        if (country.code != this.currentCountry?.code) {
            this.currentCountry = country
            val models = mutableListOf<Model>()
            for (item in country.states) {
                models.add(Model(item))
            }
            createTextInterfaceKeyboard(title, "Cancel".localized, models)
        }
    }

    override var value: Any?
        get() = (super.value as Model).state
        set(value) {
            super.value = Model(value as McsState)
        }

    override fun updateTextField() {
        val model = super.value as Model
        inputLayout.editText!!.setText(model.getText())
    }
}