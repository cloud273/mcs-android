package com.dungnguyen.qdcore.extension

import android.app.Application

val Application.versionName: String
    get() = this.packageManager.getPackageInfo(this.packageName, 0).versionName

val Application.versionCode: Int
    get() = this.packageManager.getPackageInfo(this.packageName, 0).versionCode
