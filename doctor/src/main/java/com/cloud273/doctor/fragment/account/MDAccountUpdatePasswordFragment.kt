package com.cloud273.doctor.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.cloud273.backend.api.account.McsAccountUpdatePasswordApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerPassword
import com.cloud273.mcs.textController.McsEditTextControllerPasswordAndConfirmPassword
import com.cloud273.localization.localized
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import kotlinx.android.synthetic.main.fragment_md_account_update_password.*

class MDAccountUpdatePasswordFragment: McsFragment() {
    private lateinit var passwordEditTextController: McsEditTextControllerPassword
    private lateinit var newPasswordEditTextController: McsEditTextControllerPasswordAndConfirmPassword

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Update_password"

        return inflater.inflate(R.layout.fragment_md_account_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        passwordEditTextController = McsEditTextControllerPassword(passwordLayout)
        newPasswordEditTextController = McsEditTextControllerPasswordAndConfirmPassword(newPasswordLayout, newPasswordConfirmLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                newPasswordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                newPasswordConfirmTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                newPasswordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                newPasswordConfirmTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }

        sendBtn.setOnClickListener {
            attemptUpdatePassword()
            return@setOnClickListener
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun attemptUpdatePassword() {
        dismissFocusIfNeed()
        passwordEditTextController.showErrorIfNeed()
        newPasswordEditTextController.showErrorIfNeed()
        if (passwordEditTextController.isValid && newPasswordEditTextController.isValid) {
            updatePassword(passwordEditTextController.value as String, newPasswordEditTextController.value)
        }
    }

    private fun updatePassword(password: String, newPassword: String) {
        val t = MDDatabase.instance.token
        t?.also { token ->
            McsAccountUpdatePasswordApi(McsAccountType.doctor, token, password, newPassword).run { success, newToken, code ->
                if (success) {
                    MDDatabase.instance.updateAccount(newToken!!)
                    requireContext().showAlert(title = "Successful".localized, message = null, close = "Close".localized) {
                        popBack()
                    }
                } else {
                    // Error description
                    // 403 Invalid/Expired token
                    // 404 Invalid password
                    if (code == 404) {
                        requireContext().showAlert(title = "Error".localized, message = "Wrong_password_message".localized, close = "Close".localized)
                    }
                }
            }
        }
    }
}