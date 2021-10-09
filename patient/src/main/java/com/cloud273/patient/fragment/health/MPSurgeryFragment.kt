package com.cloud273.patient.fragment.health

import android.os.Bundle
import android.view.*
import com.cloud273.backend.api.patient.health.surgery.McsPatientSurgeryCreateApi
import com.cloud273.backend.api.patient.health.surgery.McsPatientSurgeryDeleteApi
import com.cloud273.backend.api.patient.health.surgery.McsPatientSurgeryUpdateApi
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.patient.R
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.patient.McsSurgery
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.center.minDob
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.textController.McsEditTextControllerCustomKeyboard
import com.cloud273.mcs.textController.McsEditTextControllerDate
import com.cloud273.mcs.textController.McsEditTextControllerNote
import com.cloud273.mcs.textController.McsEditTextControllerSurgery
import com.cloud273.patient.center.MPDatabase
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.dismissFocusIfNeed
import com.dungnguyen.qdcore.extension.showAlert
import kotlinx.android.synthetic.main.fragment_mp_surgery.*
import java.util.*

class MPSurgeryFragment : McsFragment(), McsEditTextControllerCustomKeyboard.OnSetInterface {

    companion object {
        const val surgeryKey = "surgery"
    }

    private lateinit var nameEditTextController: McsEditTextControllerSurgery
    private lateinit var dateEditTextController: McsEditTextControllerDate
    private lateinit var noteEditTextController: McsEditTextControllerNote

    private var surgery: McsSurgery? = null
    private var date: Date? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString(surgeryKey)?.also {
            surgery = SupportCenter.instance.pop(it)
            date = surgery!!.date
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_mp_surgery, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Surgery"
        scrollView.setOnTouchListener { _, _ ->
            dismissFocusIfNeed()
            return@setOnTouchListener false
        }
        updateBtn.setOnClickListener {
            update()
            return@setOnClickListener
        }
        nameEditTextController = McsEditTextControllerSurgery(nameLayout)
        dateEditTextController = McsEditTextControllerDate(dateLayout, true, "Time".localized, minDob, Date(), this, this)
        noteEditTextController = McsEditTextControllerNote(noteLayout, false)
        reloadView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        surgery?.also {
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

    override fun onSet(sender: McsEditTextControllerCustomKeyboard, value: Any) {
        if (sender == dateEditTextController) {
            focus(noteTV, false)
        }
    }

    private fun reloadView() {
        surgery?.also {
            nameEditTextController.value = it.name
            dateEditTextController.value = it.date
            noteEditTextController.value = it.note
        }
    }

    private fun update() {
        dismissFocusIfNeed()
        nameEditTextController.showErrorIfNeed()
        dateEditTextController.showErrorIfNeed()
        noteEditTextController.showErrorIfNeed()
        if (nameEditTextController.isValid && dateEditTextController.isValid && noteEditTextController.isValid) {
            instanceDatabase.token?.also { token ->
                val name = nameEditTextController.value as String
                val date = dateEditTextController.value as Date
                val note = noteEditTextController.value as String
                if (surgery == null) {
                    val surgery = McsSurgery.create(name, date, note)
                    // Error description
                    // 403 Invalid/Expired token
                    McsPatientSurgeryCreateApi(token, surgery).run { success, id, code ->
                        if (success) {
                            surgery.id = id
                            MPDatabase.instance.add(surgery)
                            context?.showAlert("Successful".localized, null, "Close".localized) {
                                popBack()
                            }
                        } else if (code != 403) {
                            context?.generalErrorAlert()
                        }
                    }
                } else {
                    val surgery = McsSurgery.update(this.surgery!!.id!!, name, date, note)
                    // Error description
                    // 403 Invalid/Expired token
                    // 404 Not found
                    McsPatientSurgeryUpdateApi(token, surgery).run { success, _, code ->
                        if (success) {
                            MPDatabase.instance.update(surgery)
                            context?.showAlert("Successful".localized, null, "Close".localized) {
                                popBack()
                            }
                        } else {
                            if (code == 404) {
                                MPDatabase.instance.deleteAllergy(this.surgery!!.id!!)
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
            McsPatientSurgeryDeleteApi(token, surgery!!.id!!).run { success, _, code ->
                if (success) {
                    MPDatabase.instance.deleteSurgery(this.surgery!!.id!!)
                    context?.showAlert("Successful".localized, null, "Close".localized) {
                        popBack()
                    }
                } else {
                    if (code == 404) {
                        MPDatabase.instance.deleteAllergy(this.surgery!!.id!!)
                        popBack()
                    } else if (code != 403) {
                        context?.generalErrorAlert()
                    }
                }
            }
        }
    }


}
