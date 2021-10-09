package com.cloud273.mcs.textController

import androidx.fragment.app.Fragment
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.onTapListener
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.input.DatePickerDialogFragment
import com.dungnguyen.qdcore.input.InputDialogFragment
import com.dungnguyen.qdcore.input.TextPickerDialogFragment
import com.dungnguyen.qdcore.input.TimePickerDialogFragment
import com.dungnguyen.qdcore.model.TextInterface
import com.google.android.material.textfield.TextInputLayout
import java.util.*

open class McsEditTextControllerCustomKeyboard(inputLayout: TextInputLayout, isRequired: Boolean, protected val fragment: Fragment, protected val listener: OnSetInterface? = null): McsEditTextControllerDefault(inputLayout, isRequired), InputDialogFragment.OnSetInterface {

    private var _value: Any? = null

    interface OnSetInterface {
        fun onSet(sender: McsEditTextControllerCustomKeyboard, value: Any)
    }

    private fun updateValue(value: Any?) {
        _value = value
        updateTextField()
    }

    override var value: Any?
        get() = _value
        set(value) {
            updateValue(value)
            dialogFragment.select(value)
        }

    private lateinit var dialogFragment: InputDialogFragment

    private fun setupDialog(dialogFragment: InputDialogFragment) {
        this.dialogFragment = dialogFragment
        inputLayout.editText!!.onTapListener {
            fragment.dismissFocusIfNeed()
            dialogFragment.show(fragment.fragmentManager!!, this.toString())
        }
    }

    protected fun createDateKeyboard(title: String, minDate: Date?, maxDate: Date?) {
        val dialogFragment = DatePickerDialogFragment()
        dialogFragment.setup(title, minDate, maxDate, Locale(instanceDatabase.language.value))
        dialogFragment.listener = this
        setupDialog(dialogFragment)
    }

    protected fun createTimeKeyboard(title: String) {
        val dialogFragment = TimePickerDialogFragment()
        dialogFragment.setup(title, Locale(instanceDatabase.language.value))
        dialogFragment.listener = this
        setupDialog(dialogFragment)
    }

    protected fun createTextInterfaceKeyboard(title: String, cancel: String, options: List<TextInterface>) {
        val dialogFragment = TextPickerDialogFragment()
        dialogFragment.setup(title, cancel, options)
        dialogFragment.listener = this
        setupDialog(dialogFragment)
    }

    protected fun createTextKeyboard(title: String, cancel: String, options: List<String>) {
        val dialogFragment = TextPickerDialogFragment()
        dialogFragment.setupListString(title, cancel, options)
        dialogFragment.listener = this
        setupDialog(dialogFragment)
    }

    protected open fun updateTextField() {
        if (value != null) {
            if (value is String) {
                inputLayout.editText!!.setText(value as String)
            } else if (value is TextInterface) {
                inputLayout.editText!!.setText((value as TextInterface).getText())
            }
        } else {
            inputLayout.editText!!.setText("")
        }
    }

    override fun onPickerSet(sender: InputDialogFragment, value: Any) {
        updateValue(value)
        showErrorIfNeed()
        listener?.onSet(this, this.value!!)
    }

}