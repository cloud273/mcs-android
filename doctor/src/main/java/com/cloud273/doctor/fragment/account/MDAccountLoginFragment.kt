package com.cloud273.doctor.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.account.McsAccountLoginApi
import com.cloud273.backend.api.doctor.account.McsDoctorClinicInfoApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerEmailPhone
import com.cloud273.mcs.textController.McsEditTextControllerPassword
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import kotlinx.android.synthetic.main.fragment_md_account_login.*

class MDAccountLoginFragment: McsFragment() {

    private lateinit var usernameEditTextController: McsEditTextControllerEmailPhone
    private lateinit var passwordEditTextController: McsEditTextControllerPassword

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_md_account_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lTitle = "Login"

        usernameEditTextController = McsEditTextControllerEmailPhone(usernameLayout)
        passwordEditTextController = McsEditTextControllerPassword(passwordLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
//        passwordTV.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                attemptLogin()
//            }
//            return@setOnEditorActionListener false
//        }
        loginBtn.setOnClickListener {
            attemptLogin()
            return@setOnClickListener
        }

        forgotBtn.setOnClickListener {
            forgotPassword()
            return@setOnClickListener
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun forgotPassword() {
        navigate(R.id.mdAccountResetPasswordRequestFragment)
    }

    private fun attemptLogin() {
        dismissFocusIfNeed()
        usernameEditTextController.showErrorIfNeed()
        passwordEditTextController.showErrorIfNeed()
        if (usernameEditTextController.isValid && passwordEditTextController.isValid) {
            val username = usernameEditTextController.value as String
            val password = passwordEditTextController.value as String
            login(username, password)
        }
    }

    private fun login(username: String, password: String) {
        McsAccountLoginApi(McsAccountType.doctor, username, password, null).run {lSuccess, token, _ ->
            if (lSuccess) {
                McsDoctorClinicInfoApi(token!!).run { dSuccess, doctor, clinic, _ ->
                    if (dSuccess) {
                        MDDatabase.instance.setAccount(doctor!!, clinic!!, token)
                        activity!!.finish()
                    }
                }
            }
        }
    }
}