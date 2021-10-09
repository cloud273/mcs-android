package com.cloud273.doctor.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.doctor.`package`.McsDoctorListPackageApi
import com.cloud273.backend.model.doctor.McsPackage
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.view.MDPackageCell
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.model.TextModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.dungnguyen.qdcore.recycler.common.RecyclerCellData
import com.dungnguyen.qdcore.recycler.common.RecyclerCellInterface
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCenterCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_list_package.*

class MDListPackageFragment: McsFragment(), RecyclerCellInterface {
    private lateinit var adapter: RecyclerAdapter
    private var packages = mutableListOf<McsPackage>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Package"

        adapter = RecyclerAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    override fun refresh() {
        this.packages.clear()
        val token = MDDatabase.instance.token
        if (token != null) {
            McsDoctorListPackageApi(token).run { success, packages, _ ->
                if (success) {
                    this.packages.addAll(packages!!)
                    reloadView()
                }
                endRefresh()
            }
        } else {
            endRefresh()
        }
    }

    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()

        if (packages.isNotEmpty()) {
            packages.forEach { p ->
                cells.add(RecyclerCellData(data = p, resource = MDPackageCell.cellId))
            }
        } else {
            cells.add(RecyclerCellData(data = TextModel("No_package_contact_clinic_admin_message".localized), resource = RecyclerTextCenterCell.cellId))
        }

        adapter.setData(cells)
    }

    override fun onSelect(cell: RecyclerCell, id: Any?, data: Any) {
        val p = data as McsPackage
        val bundle = Bundle()
        bundle.putString(MDPackageDetailFragment.packageKey, SupportCenter.instance.push(p))
        navigate(R.id.mdPackageDetailFragment, bundle)
    }
}