package com.cloud273.mcs.util

import com.cloud273.mcs.R
import com.dungnguyen.qdcore.recycler.view.RecyclerButtonCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextCell
import com.dungnguyen.qdcore.recycler.view.RecyclerTextDetailCell

val RecyclerTextCell.Companion.addTextCell: Int
    get() = R.layout.cell_mcs_add_text

val RecyclerTextCell.Companion.refreshCell: Int
    get() = R.layout.cell_mcs_refresh_text

val RecyclerTextCell.Companion.blueTextCell: Int
    get() = R.layout.cell_mcs_blue_text

val RecyclerTextDetailCell.Companion.titleContentCell: Int
    get() = R.layout.cell_mcs_title_content

val RecyclerTextDetailCell.Companion.titleContentHighlightCell: Int
    get() = R.layout.cell_mcs_title_content_highlight

val RecyclerTextDetailCell.Companion.titleHighlightContentCell: Int
    get() = R.layout.cell_mcs_title_highlight_content

val RecyclerButtonCell.Companion.redButtonCell: Int
    get() = R.layout.cell_mcs_red_button

val RecyclerButtonCell.Companion.greenButtonCell: Int
    get() = R.layout.cell_mcs_green_button

val RecyclerButtonCell.Companion.grayButtonCell: Int
    get() = R.layout.cell_mcs_gray_button