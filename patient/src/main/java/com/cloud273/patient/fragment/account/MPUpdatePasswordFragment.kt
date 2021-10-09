package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.account.McsAccountUpdatePasswordApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerPassword
import com.cloud273.mcs.textController.McsEditTextControllerPasswordAndConfirmPassword
import com.cloud273.patient.R
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import kotlinx.android.synthetic.main.fragment_mp_update_password.*

class MPUpdatePasswordFragment : McsFragment() {

    private lateinit var passwordEditTextController: McsEditTextControllerPassword
    private lateinit var newPasswordEditTextController: McsEditTextControllerPasswordAndConfirmPassword

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_update_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Change_password"
        passwordEditTextController = McsEditTextControllerPassword(passwordLayout)
        newPasswordEditTextController = McsEditTextControllerPasswordAndConfirmPassword(newPasswordLayout, reNewPasswordLayout)
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                newPasswordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                reNewPasswordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                newPasswordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                reNewPasswordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
        sendBtn.setOnClickListener {
            updatePassword()
            return@setOnClickListener
        }
    }

    private fun updatePassword() {
        dismissFocusIfNeed()
        passwordEditTextController.showErrorIfNeed()
        newPasswordEditTextController.showErrorIfNeed()
        if (passwordEditTextController.isValid && newPasswordEditTextController.isValid) {
            instanceDatabase.token?.also { oldToken ->
                val password = passwordEditTextController.value as String
                val newPassword = newPasswordEditTextController.value
                McsAccountUpdatePasswordApi(McsAccountType.patient, oldToken, password, newPassword).run { success, token, code ->
                    if (success) {
                        instanceDatabase.updateAccount(token!!)
                        context?.showAlert("Successful".localized, null, "Close".localized) {
                            popBack()
                        }
                    } else {
                        // Error description
                        // 403 Invalid/Expired token
                        // 404 Invalid password
                        if (code == 404) {
                            context?.showAlert("Error".localized, "Wrong_password_message".localized, "Close".localized)
                        } else if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

}
