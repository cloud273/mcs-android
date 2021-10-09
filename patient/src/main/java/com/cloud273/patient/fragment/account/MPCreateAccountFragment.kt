package com.cloud273.patient.fragment.account

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.backend.api.common.McsUploadImageApi
import com.cloud273.backend.api.patient.account.McsPatientCreateApi
import com.cloud273.backend.api.patient.account.McsPatientDetailApi
import com.cloud273.backend.model.common.McsCity
import com.cloud273.backend.model.common.McsGender
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.dungnguyen.qdcore.fragment.ImagePickerDialogFragment
import com.cloud273.backend.model.common.McsState
import com.cloud273.backend.model.patient.McsPatient
import com.cloud273.mcs.center.McsAppConfiguration
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.center.minDob
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.textController.*
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.*
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_mp_create_account.*
import kotlinx.android.synthetic.main.fragment_mp_create_account.addressLayout
import kotlinx.android.synthetic.main.fragment_mp_create_account.addressTV
import kotlinx.android.synthetic.main.fragment_mp_create_account.cityLayout
import kotlinx.android.synthetic.main.fragment_mp_create_account.cityTV
import kotlinx.android.synthetic.main.fragment_mp_create_account.dobLayout
import kotlinx.android.synthetic.main.fragment_mp_create_account.genderRB
import kotlinx.android.synthetic.main.fragment_mp_create_account.imageView
import kotlinx.android.synthetic.main.fragment_mp_create_account.nameLayout
import kotlinx.android.synthetic.main.fragment_mp_create_account.scrollView
import kotlinx.android.synthetic.main.fragment_mp_create_account.stateLayout
import kotlinx.android.synthetic.main.fragment_mp_create_account.stateTV
import kotlinx.android.synthetic.main.fragment_mp_create_account.updateBtn
import kotlinx.android.synthetic.main.fragment_mp_update_account.*
import java.util.*

class MPCreateAccountFragment : McsFragment(), McsEditTextControllerCustomKeyboard.OnSetInterface {

    companion object {
        const val tokenKey = "token"
        const val usernameKey = "username"
    }

    private lateinit var nameEditTextController: McsEditTextControllerName
    private lateinit var dobEditTextController: McsEditTextControllerDate
    private lateinit var stateEditTextController: McsEditTextControllerState
    private lateinit var cityEditTextController: McsEditTextControllerCity
    private lateinit var addressEditTextController: McsEditTextControllerAddress

    private lateinit var token: String
    private lateinit var username: String
    private val country = McsAppConfiguration.instance.countries.first()
    private val state = country.states.first()
    private var image: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.also {
            token = SupportCenter.instance.pop(it.getString(tokenKey)!!)
            username = SupportCenter.instance.pop(it.getString(usernameKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_create_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Your_information"
        val imgSize = (requireContext().screenSize().width.toFloat()/2.5).toInt()
        imageView.layoutParams.width = imgSize
        imageView.layoutParams.height = imgSize
        nameEditTextController = McsEditTextControllerName(nameLayout)
        dobEditTextController = McsEditTextControllerDate(dobLayout, true, "Date_of_birth".localized, minDob, Date(), this, this)
        dobEditTextController.value = Date().add(year = -30)
        stateEditTextController = McsEditTextControllerState(stateLayout, "State".localized, country, this, this)
        stateEditTextController.value = state
        cityEditTextController = McsEditTextControllerCity(cityLayout, "City".localized, state, this, this)
        cityEditTextController.value = state.cities.first()
        addressEditTextController = McsEditTextControllerAddress(addressLayout)

        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        imageView.setOnClickListener {
            changeImage()
        }
        updateBtn.setOnClickListener {
            create()
            return@setOnClickListener
        }
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

    private fun changeImage() {
        dismissFocusIfNeed()
        ImagePickerDialogFragment.show(this, "Take_image_title".localized, "Camera".localized, "Photo_album".localized, "Cancel".localized, true) { success, image ->
            if (success) {
                this.image = image
                imageView.setImageBitmap(image!!)
            }
        }
    }

    private fun uploadImageIfNeed(completion: (success: Boolean, image: String?) -> Unit) {
        if (image != null) {
            McsUploadImageApi(image!!.toByteArray()).run { success, image, _ ->
                completion(success, image)
            }
        } else {
            completion(true, null)
        }
    }

    private fun create() {
        dismissFocusIfNeed()
        nameEditTextController.showErrorIfNeed()
        dobEditTextController.showErrorIfNeed()
        stateEditTextController.showErrorIfNeed()
        cityEditTextController.showErrorIfNeed()
        addressEditTextController.showErrorIfNeed()
        if (nameEditTextController.isValid && dobEditTextController.isValid && stateEditTextController.isValid && cityEditTextController.isValid && addressEditTextController.isValid) {
            uploadImageIfNeed { success, image ->
                if (success) {
                    val fullName = nameEditTextController.value as String
                    val dob = dobEditTextController.value as Date
                    val gender = if (genderRB.checkedRadioButtonId == R.id.maleRB) McsGender.male else McsGender.female
                    val state = stateEditTextController.value as McsState
                    val city = cityEditTextController.value as McsCity
                    val address = addressEditTextController.value as String
                    val patient = McsPatient.create(username, image, instanceDatabase.language, fullName, "", gender, dob, country.code, state.code, city.code, address, null, null)
                    McsPatientCreateApi(token, patient).run { success1, _, code ->
                        if (success1) {
                            McsPatientDetailApi(token).run { success2, patient, _ ->
                                if (success2) {
                                    instanceDatabase.setAccount(patient!!, token)
                                    activity!!.finish()
                                } else {
                                    context?.showAlert("Error".localized, "Login_again".localized, "Close".localized) {
                                        popBack()
                                    }
                                }
                            }
                        } else {
                            if (code == 409) {
                                context?.showAlert("Error".localized, "Existed_account_please_login".localized, "Close".localized) {
                                    popBack()
                                }
                            } else if (code != 403) {
                                context?.generalErrorAlert()
                            }
                        }
                    }
                } else {
                    context?.generalErrorAlert()
                }
            }
        }
    }

}
