package com.dungnguyen.qdcore.model

open class TextModel(val string: String): TextInterface {

    override fun getText(): String {
        return string
    }

}