package com.cloud273.patient.fragment.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.patient.account.McsPatientActivateRequestApi
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.textController.McsEditTextControllerEmailPhone
import com.cloud273.patient.R
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_activate_request.*

class MPActivateRequestFragment: McsFragment() {

    private lateinit var usernameEditTextController: McsEditTextControllerEmailPhone

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_activate_request, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Activation"
        usernameEditTextController = McsEditTextControllerEmailPhone(usernameLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        sendBtn.setOnClickListener {
            activateRequest()
            return@setOnClickListener
        }
    }

    private fun activateRequest() {
        dismissFocusIfNeed()
        usernameEditTextController.showErrorIfNeed()
        if (usernameEditTextController.isValid) {
            val username = usernameEditTextController.value as String
            McsPatientActivateRequestApi(username, instanceDatabase.language).run { success, type, code ->
                if (success) {
                    val bundle = Bundle()
                    bundle.putString(MPActivateFragment.usernameKey, SupportCenter.instance.push(username))
                    bundle.putString(MPActivateFragment.notifyTypeKey, SupportCenter.instance.push(type!!))
                    navigate(R.id.activateFragment, bundle)
                } else {
                    // Error description
                    // 404 Not found
                    if (code == 404) {
                        context?.showAlert("Error".localized, "Not_found_account_message".localized, "Close".localized)
                    } else {
                        context?.generalErrorAlert()
                    }
                }
            }
        }
    }

}
