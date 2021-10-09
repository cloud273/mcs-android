package com.cloud273.patient.fragment.setting

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.cloud273.patient.R
import com.cloud273.patient.model.McsRecyclerRender
import com.cloud273.patient.view.MPPatientCell
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.patient.account.McsPatientDetailApi
import com.cloud273.mcs.center.accountDidSetNotification
import com.cloud273.localization.localized
import com.cloud273.mcs.center.accountInfoDidChangeNotification
import com.cloud273.mcs.center.instanceDatabase
import com.cloud273.mcs.extension.generalErrorAlert
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.fragment.McsWebFragment
import com.cloud273.patient.center.*
import com.cloud273.localization.lTitle
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.extension.showAlert
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import kotlinx.android.synthetic.main.fragment_mp_setting.*
import java.util.*

class MPSettingFragment : McsFragment(), RecyclerCellInterface {

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (isResumed) {
                reloadView()
            }
        }
    }

    private val adapter = RecyclerSectionAdapter(McsRecyclerRender(), this)

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(accountInfoDidChangeNotification))
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(receiver, IntentFilter(accountDidSetNotification))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mp_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = "Setting"
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? {
        return swipeRefresh
    }

    override fun refresh() {
        instanceDatabase.token?.also { token ->
            McsPatientDetailApi(token).run { success, patient, _ ->
                if (success) {
                    instanceDatabase.updateAccount(patient!!)
                }
                endRefresh()
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun reloadView() {
        val accountCells = mutableListOf<RecyclerCellData>().apply {
            val account = MPDatabase.instance.account
            if (account != null) {
                add(RecyclerCellData("account", account, MPPatientCell.cellId))
            }
            add(RecyclerCellData("changePassword", "Change_password".localized, RecyclerTextCell.cellId))
            add(RecyclerCellData("logout", "Logout".localized, RecyclerTextCell.cellId))
        }
        val supportCells = mutableListOf<RecyclerCellData>().apply {
            add(RecyclerCellData("aboutUs", "About_us".localized, RecyclerTextCell.cellId))
            add(RecyclerCellData("faq", "FAQ".localized, RecyclerTextCell.cellId))
            add(RecyclerCellData("termCondition", "Term_condition".localized, RecyclerTextCell.cellId))
            add(RecyclerCellData("contact", "Contact".localized, RecyclerTextCell.cellId))
        }
        val sections = mutableListOf<RecyclerSectionData>().apply {
            add(RecyclerSectionData(RecyclerHeaderFooterData("Account".localized.uppercase()), accountCells, null))
            add(RecyclerSectionData(RecyclerHeaderFooterData("Support".localized.uppercase()), supportCells, null))
        }
        adapter.setData(sections)
    }

    private fun openWeb(title: String, url: String) {
        val bundle = Bundle()
        bundle.putString(McsWebFragment.urlKey, url)
        bundle.putString(McsWebFragment.labelKey, title)
        navigate(R.id.webFragment, bundle)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        when (id) {
            "changePassword" -> {
                navigate(R.id.updatePasswordFragment)
            }
            "logout" -> {
                context?.showAlert(null, "Logout_message".localized, "Cancel".localized, "Ok".localized) {
                    instanceDatabase.clear()
                }
            }
            "aboutUs" -> {
                openWeb(data as String, aboutUsUrl)
            }
            "faq" -> {
                openWeb(data as String, faqUrl)
            }
            "termCondition" -> {
                openWeb(data as String, termUrl)
            }
            "contact" -> {
                openWeb(data as String, contactUrl)
            }
            else -> {
                instanceDatabase.token?.also { token ->
                    McsPatientDetailApi(token).run { success, patient, code ->
                        if (success) {
                            instanceDatabase.updateAccount(patient!!)
                            navigate(R.id.updateAccountFragment)
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

}
