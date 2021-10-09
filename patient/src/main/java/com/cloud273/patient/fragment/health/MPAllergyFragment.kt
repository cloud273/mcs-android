package com.cloud273.patient.fragment.health

import android.os.Bundle
import android.view.*
import com.cloud273.backend.api.patient.health.allergy.McsPatientAllergyCreateApi
import com.cloud273.backend.api.patient.health.allergy.McsPatientAllergyDeleteApi
import com.cloud273.backend.api.patient.health.allergy.McsPatientAllergyUpdateApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.cloud273.patient.center.MPDatabase
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.patient.McsAllergy
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.textController.McsEditTextControllerAllergy
import com.cloud273.mcs.textController.McsEditTextControllerNote
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import kotlinx.android.synthetic.main.fragment_mp_allergy.*

class MPAllergyFragment : McsFragment() {

    companion object {
        const val allergyKey = "allergy"
    }

    private lateinit var nameEditTextController: McsEditTextControllerAllergy
    private lateinit var noteEditTextController: McsEditTextControllerNote

    private var allergy: McsAllergy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(allergyKey)?.also {
            allergy = SupportCenter.instance.pop(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_mp_allergy, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Allergy"
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        updateBtn.setOnClickListener {
            update()
            return@setOnClickListener
        }
        nameEditTextController = McsEditTextControllerAllergy(nameLayout)
        noteEditTextController = McsEditTextControllerNote(noteLayout, false)
        reloadView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        allergy?.also {
            inflater.inflate(R.menu.delete_menu, menu)
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var result = false
        when (item.itemId) {
            R.id.delete -> {
                delete()
                result = true
            }
        }
        return result
    }

    private fun reloadView() {
        allergy?.also {
            nameTV.setText(it.name)
            noteTV.setText(it.note)
        }
    }

    private fun update() {
        dismissFocusIfNeed()
        nameEditTextController.showErrorIfNeed()
        noteEditTextController.showErrorIfNeed()
        if (nameEditTextController.isValid && noteEditTextController.isValid) {
            instanceDatabase.token?.also { token ->
                val name = nameEditTextController.value as String
                val note = noteEditTextController.value as String
                if (this.allergy == null) {
                    val allergy = McsAllergy.create(name, note)
                    // Error description
                    // 403 Invalid/Expired token
                    McsPatientAllergyCreateApi(token, allergy).run { success, id, code ->
                        if (success) {
                            allergy.id = id
                            MPDatabase.instance.add(allergy)
                            context?.showAlert("Successful".localized, null, "Close".localized) {
                                popBack()
                            }
                        } else if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                } else {
                    val allergy = McsAllergy.update(this.allergy!!.id!!, name, note)
                    // Error description
                    // 403 Invalid/Expired token
                    // 404 Not found
                    McsPatientAllergyUpdateApi(token, allergy).run { success, _, code ->
                        if (success) {
                            MPDatabase.instance.update(allergy)
                            context?.showAlert("Successful".localized, null, "Close".localized) {
                                popBack()
                            }
                        } else {
                            if (code == 404) {
                                MPDatabase.instance.deleteAllergy(this.allergy!!.id!!)
                                popBack()
                            } else if (code != 403) {
                                context?.generalErrorAlert()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun delete() {
        dismissFocusIfNeed()
        instanceDatabase.token?.also { token ->
            // Error description
            // 403 Invalid/Expired token
            // 404 Not found
            McsPatientAllergyDeleteApi(token, allergy!!.id!!).run { success, _, code ->
                if (success) {
                    MPDatabase.instance.deleteAllergy(this.allergy!!.id!!)
                    context?.showAlert("Successful".localized, null, "Close".localized) {
                        popBack()
                    }
                } else {
                    if (code == 404) {
                        MPDatabase.instance.deleteAllergy(this.allergy!!.id!!)
                        popBack()
                    } else if (code != 403) {
                        context?.generalErrorAlert()
                    }
                }
            }
        }
    }



}
