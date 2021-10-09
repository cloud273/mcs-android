package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.cloud273.backend.api.account.McsAccountResetPasswordApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.backend.model.common.McsNotifyType
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.textController.McsEditTextControllerCode
import com.cloud273.mcs.textController.McsEditTextControllerPasswordAndConfirmPassword
import com.cloud273.patient.R
import com.cloud273.localization.lText
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_reset_password.*

class MPResetPasswordFragment: McsFragment() {

    companion object {
        const val usernameKey = "username"
        const val notifyTypeKey = "notifyType"
    }

    private lateinit var username: String
    private lateinit var notifyType: McsNotifyType

    private lateinit var codeEditTextController: McsEditTextControllerCode
    private lateinit var passwordEditTextController: McsEditTextControllerPasswordAndConfirmPassword

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            username = SupportCenter.instance.pop(it.getString(usernameKey)!!)
            notifyType = SupportCenter.instance.pop(it.getString(MPActivateFragment.notifyTypeKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Reset_password"
        titleTV.lText = "Input_reset_password_code_message".localized.replace("__notify_type__", notifyType.getString())
        codeEditTextController = McsEditTextControllerCode(codeLayout)
        passwordEditTextController = McsEditTextControllerPasswordAndConfirmPassword(passwordLayout, rePasswordLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                rePasswordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                rePasswordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        sendBtn.setOnClickListener {
            resetPassword()
            return@setOnClickListener
        }
    }

    private fun resetPassword() {
        dismissFocusIfNeed()
        codeEditTextController.showErrorIfNeed()
        passwordEditTextController.showErrorIfNeed()
        if (codeEditTextController.isValid && passwordEditTextController.isValid) {
            val resetCode = codeEditTextController.value as String
            val password = passwordEditTextController.value
            McsAccountResetPasswordApi(McsAccountType.patient, username, password, resetCode).run { success, _, code ->
                if (success) {
                    context?.showAlert("Successful".localized, "Password_change_success_message".localized, "Close".localized) {
                        hide()
                    }
                } else {
                    // Error description
                    // 403 Invalid code
                    // 404 Not found => should not happen
                    // 406 Expired code
                    when (code) {
                        403 -> {
                            context?.showAlert("Error".localized, "Invalid_reset_password_code_message".localized, "Close".localized) {
                                hide()
                            }
                        }
                        404 -> {
                            context?.showAlert("Error".localized, "Not_found_account_message".localized, "Close".localized) {
                                hide()
                            }
                        }
                        406 -> {
                            context?.showAlert("Error".localized, "Expired_reset_password_code_message".localized, "Close".localized) {
                                hide()
                            }
                        }
                        else -> {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

    private fun hide() = NavHostFragment.findNavController(this).popBackStack(R.id.loginFragment, false)

    override fun popBack() = hide()

}
