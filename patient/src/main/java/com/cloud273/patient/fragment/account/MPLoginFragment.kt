package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.account.McsAccountLoginApi
import com.cloud273.backend.api.patient.account.McsPatientDetailApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.localization.lTitle
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerEmailPhone
import com.cloud273.mcs.textController.McsEditTextControllerPassword
import com.cloud273.patient.R
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_login.*

class MPLoginFragment : McsFragment() {

    private lateinit var usernameEditTextController: McsEditTextControllerEmailPhone
    private lateinit var passwordEditTextController: McsEditTextControllerPassword

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Login"
        usernameEditTextController = McsEditTextControllerEmailPhone(usernameLayout)
        passwordEditTextController = McsEditTextControllerPassword(passwordLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                passwordTV.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                passwordTV.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
        }
//        passwordTV.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                login()
//            }
//            return@setOnEditorActionListener false
//        }
        loginBtn.setOnClickListener {
            login()
            return@setOnClickListener
        }
        forgotBtn.setOnClickListener {
            navigate(R.id.resetPasswordRequestFragment)
            return@setOnClickListener
        }
        registerBtn.setOnClickListener {
            navigate(R.id.registerFragment)
            return@setOnClickListener
        }
        activeBtn.setOnClickListener {
            navigate(R.id.activateRequestFragment)
            return@setOnClickListener
        }
    }

    private fun login() {
        dismissFocusIfNeed()
        usernameEditTextController.showErrorIfNeed()
        passwordEditTextController.showErrorIfNeed()
        if (usernameEditTextController.isValid && passwordEditTextController.isValid) {
            val username = usernameEditTextController.value as String
            val password = passwordEditTextController.value as String
            McsAccountLoginApi(McsAccountType.patient, username, password, null).run { success, token, code ->
                if (success) {
                    if (code == 202) {
                        val bundle = Bundle()
                        bundle.putString(MPCreateAccountFragment.tokenKey, SupportCenter.instance.push(token!!))
                        bundle.putString(MPCreateAccountFragment.usernameKey, SupportCenter.instance.push(username))
                        navigate(R.id.createAccountFragment, bundle)
                    } else {
                        McsPatientDetailApi(token!!).run { success1, patient, _ ->
                            if (success1) {
                                instanceDatabase.setAccount(patient!!, token)
                                requireActivity().finish()
                            } else {
                                context?.generalErrorAlert()
                            }
                        }
                    }
                } else {
                    // Error description
                    // 401 Invalid password
                    // 403 Inactivated account
                    // 404 Not found
                    // 423 Account is locked
                    when (code) {
                        401 -> {
                            context?.showAlert("Error".localized, "Wrong_login_info".localized, "Close".localized)
                        }
                        403 -> {
                            context?.showAlert("Error".localized, "Inactive_account_message".localized, "Close".localized)
                        }
                        404 -> {
                            context?.showAlert("Error".localized, "Wrong_login_info".localized, "Close".localized)
                        }
                        423 -> {
                            context?.showAlert("Error".localized, "Locked_account_message".localized, "Close".localized)
                        }
                        else -> {
                            context?.generalErrorAlert()
                        }
                    }

                }
            }
        }
    }

}
