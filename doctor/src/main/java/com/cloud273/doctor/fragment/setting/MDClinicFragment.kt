package com.cloud273.doctor.fragment.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.cloud273.doctor.R
import com.cloud273.doctor.center.MDDatabase
import com.cloud273.doctor.model.MDRecyclerRender
import com.cloud273.doctor.view.MDCertificateInfoCell
import com.cloud273.mcs.fragment.McsCertificateFragment
import com.cloud273.mcs.fragment.McsFragment
import com.cloud273.mcs.model.getString
import com.cloud273.mcs.util.titleHighlightContentCell
import com.cloud273.localization.lTitle
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.fragment.ImagePreviewDialogFragment
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.adapter.RecyclerSectionAdapter
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.view.RecyclerImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import kotlinx.android.synthetic.main.fragment_md_clinic.*

class MDClinicFragment: McsFragment(), RecyclerCellInterface, RecyclerImageCell.OnActionInterface {
    private lateinit var adapter: RecyclerSectionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lTitle = "Clinic"

        adapter = RecyclerSectionAdapter(MDRecyclerRender(), this)
        return inflater.inflate(R.layout.fragment_md_clinic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    override fun swipeRefreshLayout(): SwipeRefreshLayout? = swipeRefresh

    private fun reloadView() {
        val sections = mutableListOf<RecyclerSectionData>()
        val certificateCells = mutableListOf<RecyclerCellData>()
        val informationCells = mutableListOf<RecyclerCellData>()
        val imageCells = mutableListOf<RecyclerCellData>()

        MDDatabase.instance.clinic?.also { clinic ->
            imageCells.add(RecyclerCellData(id = "image", data = ImageModel(clinic.image, R.mipmap.no_image_icon), resource = RecyclerImageCell.cellId))
            sections.add(RecyclerSectionData(null, imageCells, null))

            informationCells.add(RecyclerCellData(data = TextDetailModel("Name".localized, clinic.name), resource = RecyclerTextDetailCell.titleHighlightContentCell))
            informationCells.add(RecyclerCellData(data = TextDetailModel("Phone".localized, clinic.phone), resource = RecyclerTextDetailCell.titleHighlightContentCell))
            clinic.workPhone?.also {
                informationCells.add(RecyclerCellData(data = TextDetailModel("Work_phone".localized, it), resource = RecyclerTextDetailCell.titleHighlightContentCell))
            }

            informationCells.add(RecyclerCellData(data = TextDetailModel("Email".localized, clinic.email), resource = RecyclerTextDetailCell.titleHighlightContentCell))
            informationCells.add(RecyclerCellData(data = TextDetailModel("Address".localized, clinic.address.getString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
            sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Basic_information".localized), informationCells, null))

            clinic.certificates?.forEach { cert ->
                certificateCells.add(RecyclerCellData(id = "cert", data = cert, resource = MDCertificateInfoCell.cellId))
            }
            sections.add(RecyclerSectionData(RecyclerHeaderFooterData("Certificate".localized), certificateCells, null))
        }
        adapter.setData(sections)
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        if (id == "cert") {
            val certCell = cell as MDCertificateInfoCell
            certCell.listener = { _, _, certificate ->
                val bundle = Bundle()
                bundle.putString(McsCertificateFragment.certificateKey, SupportCenter.instance.push(certificate))
                navigate(R.id.mcsCertificateFragment, bundle)
            }
        }
        if (id == "image") {
            (cell as RecyclerImageCell).listener = this
        }
    }

    override fun onImageClick(cell: RecyclerImageCell, id: Any?) {
        ImagePreviewDialogFragment.show(this, ImageModel(MDDatabase.instance.clinic?.image, R.mipmap.no_image_icon))
    }
}