package com.cloud273.mcs.extension

import android.content.Context
import com.cloud273.localization.localized
import com.dungnguyen.qdcore.extension.showAlert

fun Context.generalErrorAlert() {
    showAlert("Error".localized, "Backend_connect_fail_message".localized, "Close".localized)
}