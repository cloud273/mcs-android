package com.cloud273.doctor.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.backend.api.doctor.account.McsDoctorDetailApi
import com.cloud273.backend.model.doctor.McsDoctor
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.view.MDCertificateInfoCell
import com.cloud273.mcs.fragment.McsCertificateFragment
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.fullName
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.model.specialtiesString
import com.cloud273.mcs.util.titleContentCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.recycler.common.unDrawDividerAt
import com.dungnguyen.qdcore.extension.yearOld
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.common.RecyclerCell
import com.dungnguyen.qdcore.recycler.common.RecyclerCellData
import com.dungnguyen.qdcore.recycler.common.RecyclerCellInterface
import com.dungnguyen.qdcore.recycler.view.RecyclerImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_doctor.*

class MDDoctorFragment: McsFragment(), RecyclerCellInterface {
    private lateinit var adapter: RecyclerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Doctor"

        adapter = RecyclerAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_doctor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        refresh()
    }

    override fun refresh() {
        McsDoctorDetailApi(MDDatabase.instance.token!!).run{ success, doctor, _ ->
            if (success) {
                MDDatabase.instance.updateAccount(doctor!!)
                reloadView()
            }
            endRefresh()
        }
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout?  = swipeRefresh

    private fun reloadView() {
        val doctor = MDDatabase.instance.account as McsDoctor?
        doctor?.also {
            val cells = mutableListOf<RecyclerCellData>()
            cells.add(RecyclerCellData(data = ImageModel(doctor.image, R.mipmap.doctor_icon), resource = RecyclerImageCell.cellId))
            doctor.profile?.also {
                val text = "${"Doctor".localized}: ${it.fullName}" +
                        "\n${it.gender.getString()} / ${it.dob.yearOld()}${"Year_old".localized}"
                cells.add(RecyclerCellData(data = TextDetailModel("Basic_information", text), resource = RecyclerTextDetailCell.titleContentCell))
            }
            doctor.office?.also {
                cells.add(RecyclerCellData(data = TextDetailModel("Office".localized, it), resource = RecyclerTextDetailCell.titleContentCell))
            }
            cells.add(RecyclerCellData(data = TextDetailModel("Specialty".localized, doctor.specialtiesString(null)), resource = RecyclerTextDetailCell.titleContentCell))
            doctor.startWork?.also {
                val text = "${it.yearOld()} ${"years".localized}"
                cells.add(RecyclerCellData(data = TextDetailModel("Experience".localized, text), resource = RecyclerTextDetailCell.titleContentCell))
            }
            doctor.biography?.also {
                cells.add(RecyclerCellData(data = TextDetailModel("Biography".localized, it), resource = RecyclerTextDetailCell.titleContentCell))
            }
            doctor.certificates?.also {
                for (item in it) {
                    cells.add(RecyclerCellData(id = "cert", data = item, resource = MDCertificateInfoCell.cellId))
                }
            }
            recyclerView.unDrawDividerAt(0, cells.size - 1)
            adapter.setData(cells)
        }
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        super.onDrawCell(cell, id, data)
        if (id == "cert") {
            val certificateCell = cell as MDCertificateInfoCell
            certificateCell.listener = { _, _, certificate ->
                val bundle = Bundle()
                bundle.putString(McsCertificateFragment.certificateKey, SupportCenter.instance.push(certificate))
                navigate(R.id.mcsCertificateFragment, bundle)
            }
        }
    }
}