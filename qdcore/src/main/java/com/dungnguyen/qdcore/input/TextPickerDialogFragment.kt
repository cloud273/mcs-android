package com.dungnguyen.qdcore.input

import androidx.appcompat.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.dungnguyen.qdcore.model.TextInterface
import com.dungnguyen.qdcore.model.TextModel

class TextPickerDialogFragment() : InputDialogFragment() {

    private var isString: Boolean = false

    private lateinit var list: List<TextInterface>

    private var selected: TextInterface? = null

    private var title: String? = null

    private lateinit var cancel: String

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(context!!)
        builder.setTitle(title)
        val items = mutableListOf<String>()
        for (obj in list) {
            items.add(obj.getText())
        }
        builder.setItems(items.toTypedArray()) { _, which ->
            if (isString) {
                listener?.onPickerSet(this, list[which].getText())
            } else {
                listener?.onPickerSet(this, list[which])
            }

        }
        builder.setNegativeButton(cancel, null)
        return builder.create()
    }

    fun setup(title: String? = null, cancel: String, list: List<TextInterface>) {
        this.title = title
        this.list = list
        this.cancel = cancel
        isString = false
    }

    fun setupListString(title: String? = null, cancel: String, list: List<String>) {
        val objects = mutableListOf<TextModel>()
        for (item in list) {
            objects.add(TextModel(item))
        }
        this.title = title
        this.list = objects
        this.cancel = cancel
        isString = true
    }

    override fun select(data: Any?) {
        selected = if (data != null) {
            if (data is String) {
                TextModel(data)
            } else {
                data as TextInterface
            }
        } else {
            null
        }

    }

}