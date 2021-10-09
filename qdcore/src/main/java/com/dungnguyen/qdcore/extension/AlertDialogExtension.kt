package com.dungnguyen.qdcore.extension

import android.content.Context
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.dungnguyen.qdcore.R
import com.dungnguyen.qdcore.model.TextInterface
import com.google.android.material.textfield.TextInputEditText

fun Context.showAlert(title: String?, message: String?, close: String = getString(R.string.cancel), action: (() -> Unit)? = null) {
    val builder = AlertDialog.Builder(this)
        .setNegativeButton(close) { _, _ ->
            action?.invoke()
        }
    if (title != null) {
        builder.setTitle(title)
    }
    if (message != null) {
        builder.setMessage(message)
    }
    builder.create().show()
}


fun Context.showAlert(title: String?, message: String?, no: String = getString(R.string.cancel), yes: String, action: (() -> Unit)?) {
    val builder = AlertDialog.Builder(this)
        .setNegativeButton(no, null)
        .setPositiveButton(yes) { _, _ ->
            action?.invoke()
        }
    if (title != null) {
        builder.setTitle(title)
    }
    if (message != null) {
        builder.setMessage(message)
    }
    builder.create().show()
}


fun Context.showAlert(title: String?, message: String?, no: String = getString(R.string.cancel), yes: String, inputSetup: (TextInputEditText) -> Unit , action: ((String) -> Unit)?) {
    val layout = LinearLayout(this)
    layout.orientation = LinearLayout.VERTICAL
    val input = TextInputEditText(this)
    inputSetup(input)
    val d = density()
    val h = (d * 18).toInt()
    val v = (d * 8).toInt()
    val linearLayout = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    linearLayout.setMargins(h, v, h, v)
    layout.addView(input, linearLayout)
    val builder = AlertDialog.Builder(this)
        .setNegativeButton(no, null)
        .setPositiveButton(yes) { _, _ ->
            action?.invoke(input.text.toString())
        }
        .setView(layout)
    if (title != null) {
        builder.setTitle(title)
    }
    if (message != null) {
        builder.setMessage(message)
    }
    val dialog = builder.create()
    dialog.setCancelable(false)
    dialog.show()
}

fun Context.showAlert(title: String?, objs: List<TextInterface>, cancel: String = getString(R.string.cancel), action: ((TextInterface) -> Unit)?) {
    val builder = AlertDialog.Builder(this)
    builder.setTitle(title)
    val items = mutableListOf<String>()
    for (obj in objs) {
        items.add(obj.getText())
    }
    builder.setItems(items.toTypedArray()) { _, which ->
        action?.invoke(objs[which])
    }
    builder.setNegativeButton(cancel, null)
    builder.create().show()
}
