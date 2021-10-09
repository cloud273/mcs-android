package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.patient.account.McsPatientRegisterApi
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerEmailPhone
import com.cloud273.mcs.textController.McsEditTextControllerPasswordAndConfirmPassword
import com.cloud273.patient.R
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_register.*

class MPRegisterFragment : McsFragment() {

    private lateinit var usernameEditTextController: McsEditTextControllerEmailPhone
    private lateinit var passwordEditTextController: McsEditTextControllerPasswordAndConfirmPassword

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Register"
        usernameEditTextController = McsEditTextControllerEmailPhone(usernameLayout)
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
        registerBtn.setOnClickListener {
            register()
            return@setOnClickListener
        }
    }

    private fun register() {
        dismissFocusIfNeed()
        usernameEditTextController.showErrorIfNeed()
        passwordEditTextController.showErrorIfNeed()
        if (usernameEditTextController.isValid && passwordEditTextController.isValid) {
            val username = usernameEditTextController.value as String
            val password = passwordEditTextController.value
            McsPatientRegisterApi(username, password, instanceDatabase.language).run { success, type, code ->
                if (success) {
                    val bundle = Bundle()
                    bundle.putString(MPActivateFragment.usernameKey, SupportCenter.instance.push(username))
                    bundle.putString(MPActivateFragment.notifyTypeKey, SupportCenter.instance.push(type!!))
                    navigate(R.id.activateFragment, bundle)
                } else {
                    // Error description
                    // 409 Existed account
                    if (code == 409) {
                        context?.showAlert("Error".localized, "Existed_username".localized, "Close".localized)
                    } else {
                        context?.generalErrorAlert()
                    }
                }
            }
        }
    }

}
