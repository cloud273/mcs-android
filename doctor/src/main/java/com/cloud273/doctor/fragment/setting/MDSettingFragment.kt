package com.cloud273.doctor.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.doctor.R
import com.cloud273.doctor.center.*
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.view.MDDoctorCell
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.localization.localized
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.fragment.McsWebFragment
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.model.TextModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_setting.*

class MDSettingFragment: McsFragment(), RecyclerCellInterface {
    private lateinit var adapter: RecyclerSectionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Setting"

        adapter = RecyclerSectionAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout?  = swipeRefresh

    private fun reloadView() {
        val sections = mutableListOf<RecyclerSectionData>()
        val accountCells = mutableListOf<RecyclerCellData>()
        val informationCells = mutableListOf<RecyclerCellData>()
        val supportCells = mutableListOf<RecyclerCellData>()

        MDDatabase.instance.account?.also {
            accountCells.add(RecyclerCellData("account", it, MDDoctorCell.cellId))
            accountCells.add(RecyclerCellData("changePwd", "Change_password".localized, RecyclerTextCell.cellId))
            accountCells.add(RecyclerCellData("logout", "Logout".localized, RecyclerTextCell.cellId))
            sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Account".localized), accountCells, null))
        }

        informationCells.add(RecyclerCellData("clinic", "Clinic".localized, RecyclerTextCell.cellId))
        informationCells.add(RecyclerCellData("package", "Package".localized, RecyclerTextCell.cellId))
        informationCells.add(RecyclerCellData("schedule", "Schedule".localized, RecyclerTextCell.cellId))

        supportCells.add(RecyclerCellData("about", "About_us".localized, RecyclerTextCell.cellId))
        supportCells.add(RecyclerCellData("faq", "FAQ".localized, RecyclerTextCell.cellId))
        supportCells.add(RecyclerCellData("term", "Term_condition".localized, RecyclerTextCell.cellId))
        supportCells.add(RecyclerCellData("contact", "Contact".localized, RecyclerTextCell.cellId))

        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Other_information".localized), informationCells, null))
        sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Support".localized), supportCells, null))
        adapter.setData(sections)
    }

    private fun openWeb(title: String, url: String) {
        val bundle = Bundle()
        bundle.putString(McsWebFragment.urlKey, url)
        bundle.putString(McsWebFragment.labelKey, title)
        navigate(R.id.mcsWebFragment, bundle)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        when (id) {
            "account" -> {
                navigate(R.id.mdDoctorFragment)
            }
            "changePwd" -> {
                navigate(R.id.mdAccountUpdatePasswordFragment)
            }
            "logout" -> {
                requireContext().showAlert(title = null, message = "Logout_message".localized, no = "Cancel".localized, yes = "Ok".localized) {
                    instanceDatabase.clear()
                }
            }
            "clinic" -> {
                navigate(R.id.mdClinicFragment)
            }
            "package" -> {
                navigate(R.id.mdListPackageFragment)
            }
            "schedule" -> {
                MDWorkingTimeHelper.reload { success, from, to, workingTime, packages, _ ->
                    if (success) {
                        val bundle = Bundle()
                        bundle.putString(MDScheduleFragment.workingTimesKey, SupportCenter.instance.push(workingTime!!))
                        bundle.putString(MDScheduleFragment.packagesKey, SupportCenter.instance.push(packages!!))
                        bundle.putString(MDScheduleFragment.fromKey, SupportCenter.instance.push(from))
                        bundle.putString(MDScheduleFragment.toKey, SupportCenter.instance.push(to))
                        navigate(R.id.mdScheduleFragment, bundle)
                    } else {
                        requireContext().showAlert(title = "Error".localized, message = "No_package_contact_clinic_admin_message".localized)
                    }
                }
            }
            "about" -> {
                openWeb(data as String, aboutUsUrl)
            }
            "faq" -> {
                openWeb(data as String, faqUrl)
            }
            "term" -> {
                openWeb(data as String, termUrl)
            }
            "contact" -> {
                openWeb(data as String, contactUrl)
            }
            else -> {}
        }
    }
}
