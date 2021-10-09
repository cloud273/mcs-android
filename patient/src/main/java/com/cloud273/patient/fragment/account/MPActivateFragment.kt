package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import com.cloud273.backend.api.patient.account.McsPatientActivateApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.common.McsNotifyType
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.textController.McsEditTextControllerCode
import com.cloud273.localization.lText
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import kotlinx.android.synthetic.main.fragment_mp_activate.*

class MPActivateFragment: McsFragment() {

    companion object {
        const val usernameKey = "username"
        const val notifyTypeKey = "notifyType"
    }

    private lateinit var codeEditTextController: McsEditTextControllerCode
    private lateinit var username: String
    private lateinit var notifyType: McsNotifyType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            username = SupportCenter.instance.pop(it.getString(usernameKey)!!)
            notifyType = SupportCenter.instance.pop(it.getString(notifyTypeKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_activate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Activation"
        titleTV.lText = "Input_activation_code_message".localized.replace("__notify_type__", notifyType.getString())
        codeEditTextController = McsEditTextControllerCode(codeLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        sendBtn.setOnClickListener {
            activate()
            return@setOnClickListener
        }

    }

    private fun activate() {
        dismissFocusIfNeed()
        codeEditTextController.showErrorIfNeed()
        if (codeEditTextController.isValid) {
            val activationCode = codeEditTextController.value as String
            McsPatientActivateApi(username, activationCode).run { success, _, code ->
                if (success) {
                    context?.showAlert("Successful".localized, "Activated_success_message".localized, "Close".localized) {
                        hide()
                    }
                } else {
                    // Error description
                    // 403 Invalid code
                    // 404 Not found => should not happen
                    // 406 Expired code
                    when (code) {
                        403 -> {
                            context?.showAlert("Error".localized, "Invalid_activation_code_message".localized, "Close".localized) {
                                hide()
                            }
                        }
                        404 -> {
                            context?.showAlert("Error".localized, "Not_found_account_message".localized, "Close".localized) {
                                hide()
                            }
                        }
                        406 -> {
                            context?.showAlert("Error".localized, "Expired_activation_code_message".localized, "Close".localized) {
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
