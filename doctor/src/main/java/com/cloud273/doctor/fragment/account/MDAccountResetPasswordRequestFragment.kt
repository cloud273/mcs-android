package com.cloud273.doctor.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.account.McsAccountResetPasswordRequestApi
import com.cloud273.backend.model.common.McsAccountType
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerEmailPhone
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_account_reset_password_request.*

class MDAccountResetPasswordRequestFragment : McsFragment() {

    private lateinit var usernameEditTextController: McsEditTextControllerEmailPhone

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Reset_password"

        return inflater.inflate(R.layout.fragment_md_account_reset_password_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        usernameEditTextController = McsEditTextControllerEmailPhone(usernameLayout)
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }

        sendBtn.setOnClickListener {
            attemptResetPasswordRequest()
            return@setOnClickListener
        }
        super.onViewCreated(view, savedInstanceState)
    }

    private fun attemptResetPasswordRequest() {
        dismissFocusIfNeed()
        usernameEditTextController.showErrorIfNeed()
        if (usernameEditTextController.isValid) {
            val username = usernameEditTextController.value as String
            resetPasswordRequest(username)
        }
    }

    private fun resetPasswordRequest(username: String) {
        McsAccountResetPasswordRequestApi(McsAccountType.doctor, username, MDDatabase.instance.language).run { success, type, code ->
            if (success) {
                val bundle = Bundle()
                bundle.putString(MDAccountResetPasswordFragment.usernameKey, SupportCenter.instance.push(username))
                bundle.putString(MDAccountResetPasswordFragment.notifyTypeKey, SupportCenter.instance.push(type!!))
                navigate(R.id.mdAccountResetPasswordFragment, bundle)
            } else {
                // Error description
                // 404 Not found
                if (code == 404) {
                    requireContext().showAlert(title = "Error".localized, message = "Not_found_account_message".localized, close = "Close".localized)
                }
            }
        }
    }

}