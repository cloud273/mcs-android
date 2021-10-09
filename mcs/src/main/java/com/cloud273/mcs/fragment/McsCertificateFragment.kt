package com.cloud273.mcs.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cloud273.mcs.R
import com.cloud273.mcs.model.McsRecyclerRender
import com.cloud273.mcs.util.toAppDateString
import com.dungnguyen.qdcore.recycler.common.listLayout
import com.dungnguyen.qdcore.recycler.common.unDrawDividerAt
import com.dungnguyen.qdcore.fragment.ImagePreviewDialogFragment
import com.dungnguyen.qdcore.model.ImageModel
import com.dungnguyen.qdcore.model.TextDetailModel
import com.dungnguyen.qdcore.recycler.common.*
import com.dungnguyen.qdcore.recycler.adapter.RecyclerAdapter
import com.dungnguyen.qdcore.recycler.view.RecyclerImageCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell
import com.dungnguyen.qdcore.support.SupportCenter
import com.cloud273.backend.model.common.McsCertificate
import com.cloud273.localization.lTitle
import com.cloud273.mcs.model.title
import com.cloud273.mcs.util.titleHighlightContentCell
import com.cloud273.localization.localized
import kotlinx.android.synthetic.main.fragment_mcs_certificate.*

class McsCertificateFragment : McsFragment(), RecyclerCellInterface, RecyclerImageCell.OnActionInterface {

    companion object {
        const val certificateKey = "certificate"
    }

    private val adapter = RecyclerAdapter(McsRecyclerRender(), this)
    private lateinit var certificate: McsCertificate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            certificate = SupportCenter.instance.pop(getString(certificateKey)!!)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_mcs_certificate, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lTitle = certificate.title
        recyclerView.listLayout()
        recyclerView.adapter = adapter
        reloadView()
    }

    private fun reloadView() {
        val cells = mutableListOf<RecyclerCellData>()
        cells.add(RecyclerCellData("image", ImageModel(certificate.image, R.mipmap.no_image_icon), RecyclerImageCell.cellId))
        cells.add(RecyclerCellData(data = TextDetailModel("Number".localized, certificate.code), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        cells.add(RecyclerCellData(data = TextDetailModel("Name".localized, certificate.name), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        cells.add(RecyclerCellData(data = TextDetailModel("Issuer".localized, certificate.issuer), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        cells.add(RecyclerCellData(data = TextDetailModel("Issue_date".localized, certificate.issueDate.toAppDateString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        certificate.expDate?.also {
            cells.add(RecyclerCellData(data = TextDetailModel("Expired_date".localized, it.toAppDateString()), resource = RecyclerTextDetailCell.titleHighlightContentCell))
        }
        recyclerView.unDrawDividerAt(0, cells.size - 1)
        adapter.setData(cells)
    }

    override fun onDrawCell(cell: RecyclerCell, id: Any?, data: Any) {
        if (id == "image") {
            (cell as RecyclerImageCell).listener = this
        }
    }

    override fun onImageClick(cell: RecyclerImageCell, id: Any?) {
        ImagePreviewDialogFragment.show(this, ImageModel(certificate.image, R.mipmap.no_image_icon))
    }

}