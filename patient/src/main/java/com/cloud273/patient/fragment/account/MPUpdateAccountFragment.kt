package com.cloud273.patient.fragment.account

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.cloud273.backend.api.common.McsUploadImageApi
import com.cloud273.backend.api.patient.account.McsPatientPartialUpdateApi
import com.cloud273.backend.api.patient.account.McsPatientUpdateApi
import com.cloud273.backend.model.common.McsCity
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.center.MPDatabase
import com.dungnguyen.qdcore.fragment.ImagePickerDialogFragment
import com.cloud273.backend.model.common.McsGender
import com.cloud273.backend.model.common.McsState
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.mcs.center.accountInfoDidChangeNotification
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.center.minDob
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.model.*
import com.cloud273.mcs.textController.*
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_mp_update_account.*
import java.util.*

class MPUpdateAccountFragment : McsFragment(), McsEditTextControllerCustomKeyboard.OnSetInterface {

    private lateinit var nameEditTextController: McsEditTextControllerName
    private lateinit var dobEditTextController: McsEditTextControllerDate
    private lateinit var stateEditTextController: McsEditTextControllerState
    private lateinit var cityEditTextController: McsEditTextControllerCity
    private lateinit var addressEditTextController: McsEditTextControllerAddress

    private var account: McsPatient = MPDatabase.instance.account as McsPatient
    private var dob = account.profile!!.dob
    private val country = account.address!!.country
    private var state = account.address!!.state
    private var city = account.address!!.city

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_update_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Your_information"
        val imgSize = (requireContext().screenSize().width.toFloat()/2.5).toInt()
        imageView.layoutParams.width = imgSize
        imageView.layoutParams.height = imgSize
        nameEditTextController = McsEditTextControllerName(nameLayout)
        dobEditTextController = McsEditTextControllerDate(dobLayout, true, "Date_of_birth".localized, minDob, Date(), this, this)
        stateEditTextController = McsEditTextControllerState(stateLayout, "State".localized, country, this, this)
        cityEditTextController = McsEditTextControllerCity(cityLayout, "City".localized, state, this, this)
        addressEditTextController = McsEditTextControllerAddress(addressLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        imageView.setOnClickListener {
            updateImage()
        }
        updateBtn.setOnClickListener {
            update()
            return@setOnClickListener
        }
        reloadView()
    }

    override fun onSet(sender: McsEditTextControllerCustomKeyboard, value: Any) {
        when (sender) {
            dobEditTextController -> {
                focus(stateTV, false)
            }
            stateEditTextController -> {
                val state = value as McsState
                if (state.code != this.state.code) {
                    cityEditTextController.setState(state)
                    cityEditTextController.value = state.cities.first()
                }
                focus(cityTV, false)
            }
            cityEditTextController -> {
                focus(addressTV, true)
            }
        }
    }

    private fun reloadView() {
        Picasso.get().load(account.image).placeholder(R.mipmap.profile_icon).into(imageView)
        nameEditTextController.value = account.profile!!.fullName
        if (account.profile!!.gender == McsGender.female) {
            genderRB.check(R.id.femaleRB)
        } else {
            genderRB.check(R.id.maleRB)
        }
        dobEditTextController.value = dob
        stateEditTextController.value = state
        cityEditTextController.value = city
        addressEditTextController.value = account.address!!.line
    }

    private fun updateImage() {
        dismissFocusIfNeed()
        instanceDatabase.token?.also { token ->
            ImagePickerDialogFragment.show(this, "Take_image_title".localized, "Camera".localized, "Photo_album".localized, "Cancel".localized, true) { success, image ->
                if (success) {
                    McsUploadImageApi(image!!.toByteArray()).run { success1, imageName, _ ->
                        if (success1) {
                            val patient = McsPatient.partialUpdate(imageName, null)
                            McsPatientPartialUpdateApi(token, patient).run { success2, _, code ->
                                if (success2) {
                                    (instanceDatabase.account as McsPatient).imageName = imageName
                                    imageView.setImageBitmap(image)
                                    LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(Intent(accountInfoDidChangeNotification))
                                } else if (code != 403) {
                                    context?.generalErrorAlert()
                                }
                            }
                        } else {
                            context?.generalErrorAlert()
                        }
                    }
                } else {
                    context?.generalErrorAlert()
                }
            }
        }
    }

    private fun update() {
        nameEditTextController.showErrorIfNeed()
        dobEditTextController.showErrorIfNeed()
        stateEditTextController.showErrorIfNeed()
        cityEditTextController.showErrorIfNeed()
        addressEditTextController.showErrorIfNeed()
        if (nameEditTextController.isValid && dobEditTextController.isValid && stateEditTextController.isValid && cityEditTextController.isValid && addressEditTextController.isValid) {
            instanceDatabase.token?.also { token ->
                val fullName = nameEditTextController.value as String
                val dob = dobEditTextController.value as Date
                val gender = if (genderRB.checkedRadioButtonId == R.id.maleRB) McsGender.male else McsGender.female
                val state = stateEditTextController.value as McsState
                val city = cityEditTextController.value as McsCity
                val address = addressEditTextController.value as String
                val patient = McsPatient.update(fullName, "", gender, dob, country.code, state.code, city.code, address, null, null)
                McsPatientUpdateApi(token, patient).run { success, _, code ->
                    if (success) {
                        MPDatabase.instance.updateAccount(patient.profile!!, patient.address!!)
                        context?.showAlert("Successful".localized, null, "Close".localized) {
                            popBack()
                        }
                    } else {
                        if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                }
            }
        }
    }

}
