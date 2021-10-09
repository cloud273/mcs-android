package com.cloud273.doctor.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.cloud273.backend.api.account.McsAccountResetPasswordApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.backend.model.common.McsNotifyType
import com.cloud273.doctor.R
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.textController.McsEditTextControllerCode
import com.cloud273.mcs.textController.McsEditTextControllerPasswordAndConfirmPassword
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_account_reset_password.*

class MDAccountResetPasswordFragment : McsFragment() {

    companion object {
        const val usernameKey = "username"
        const val notifyTypeKey = "_type"
    }

    private lateinit var passwordEditTextController: McsEditTextControllerPasswordAndConfirmPassword
    private lateinit var codeEditTextController: McsEditTextControllerCode
    private lateinit var username: String
    private lateinit var notificationType: McsNotifyType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            username = SupportCenter.instance.pop(it.getString(usernameKey)!!)
            notificationType = SupportCenter.instance.pop(it.getString(notifyTypeKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Reset_password"

        return inflater.inflate(R.layout.fragment_md_account_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        codeTextView.text = String.format("Input_reset_password_code_message".localized, notificationType.getString(), username)

        passwordEditTextController = McsEditTextControllerPasswordAndConfirmPassword(passwordLayout, passwordConfirmLayout)
        codeEditTextController = McsEditTextControllerCode(codeLayout)
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordConfirmTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordConfirmTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        sendBtn.setOnClickListener {
            attemptResetPassword()
            return@setOnClickListener
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun attemptResetPassword() {
        dismissFocusIfNeed()
        passwordEditTextController.showErrorIfNeed()
        codeEditTextController.showErrorIfNeed()
        if (passwordEditTextController.isValid && codeEditTextController.isValid) {
            resetPassword(passwordEditTextController.value, codeEditTextController.value as String)
        }
    }

    private fun resetPassword(password: String, verifyCode: String) {
        McsAccountResetPasswordApi(McsAccountType.doctor, username, password, verifyCode).run { success, _, code ->
            if (success) {
                requireContext().showAlert(title = "Successful".localized, message = "Password_change_success_message".localized, close = "Close".localized) {
                    findNavController().popBackStack(R.id.mdAccountLoginFragment, true)
                }
            } else {
                // Error description
                // 403 Invalid code
                // 404 Not found => should not happen
                // 406 Expired code
                if (code == 403) {
                    requireContext().showAlert(title = "Error".localized, message = "Invalid_activation_code_message".localized, close = "Close".localized)
                } else if (code == 406) {
                    requireContext().showAlert(title = "Error".localized, message = "Expired_activation_code_message".localized, close = "Close".localized) {
                        popBack()
                    }
                }
            }
        }
    }

}