package com.dungnguyen.qdcore.input

import androidx.fragment.app.DialogFragment

open class InputDialogFragment: DialogFragment() {

    var listener: OnSetInterface? = null

    interface OnSetInterface {

        fun onPickerSet(sender: InputDialogFragment, value: Any)

    }

    open fun select(data : Any?) {

    }

}