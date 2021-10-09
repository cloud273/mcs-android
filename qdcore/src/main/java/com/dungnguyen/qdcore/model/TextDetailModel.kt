package com.dungnguyen.qdcore.model

open class TextDetailModel(val title: String, val detail: String) : TextDetailInterface {

    override fun getText(): String {
        return title
    }

    override fun getDetailText(): String? {
        return detail
    }


}