package com.cloud273.mcs.textController

import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.add
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class McsEditTextControllerTimeDuration(private val fromInputLayout: TextInputLayout, private val toInputLayout: TextInputLayout, fragment: Fragment, private val minimumDurationInterval: Int = 15): McsEditTextControllerCustomKeyboard.OnSetInterface {

    private val fromController = McsEditTextControllerTime(fromInputLayout, false, "From".localized, fragment, this)
    private val toController = McsEditTextControllerTime(toInputLayout, false, "To".localized, fragment, this)

    var fromValue: Date?
        get() = fromController.value as Date?
        set(value) {
            fromController.value = value
        }

    var toValue: Date?
        get() = toController.value as Date?
        set(value) {
            toController.value = value
        }

    val isValid: Boolean
        get() {
            return fromController.isValid && toController.isValid
        }

    fun getClearableFromInput(): RelativeLayout = fromController.getAsClearableInput {
        if (toValue != null) {
            toInputLayout.error = "Invalid".localized
        } else {
            toInputLayout.error = null
        }
    }

    fun getClearableToInput(): RelativeLayout = toController.getAsClearableInput {
        if (fromValue != null) {
            toInputLayout.error = "Invalid".localized
        } else {
            toInputLayout.error = null
        }
    }

    override fun onSet(sender: McsEditTextControllerCustomKeyboard, value: Any) {
        when (sender) {
            fromController -> {
                val fromTime = value as Date
                val toTime = toController.value as Date?
                if (toTime != null) {
                    if (fromTime.add(minute = minimumDurationInterval).after(toTime)) {
                        toInputLayout.error = "Invalid".localized
                    } else {
                        toInputLayout.error = null
                    }
                } else {
                    toInputLayout.error = "Invalid".localized
                }
            }
            toController -> {
                val toTime = value as Date
                val fromTime = fromController.value as Date?
                if (fromTime != null) {
                    if (toTime.before(fromTime.add(minute = minimumDurationInterval))) {
                        toInputLayout.error = "Invalid".localized
                    } else {
                        toInputLayout.error = null
                    }
                } else {
                    fromInputLayout.error = "Invalid".localized
                }
            }
        }
    }

    fun showErrorIfNeed() {
        fromController.showErrorIfNeed()
        toController.showErrorIfNeed()
        val fromTime = fromController.value as Date?
        val toTime = toController.value as Date?
        if (fromTime != null && toTime != null && (fromTime.add(minute = minimumDurationInterval).after(toTime) || toTime.before(fromTime.add(minute = minimumDurationInterval)))) {
            fromInputLayout.error = "Invalid".localized
            toInputLayout.error = "Invalid".localized
        }
    }

}