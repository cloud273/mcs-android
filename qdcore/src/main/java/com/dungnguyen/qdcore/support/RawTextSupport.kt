package com.dungnguyen.qdcore.support

import android.app.Application

class RawTextSupport {

    companion object {

        fun loadRawText(application: Application, filename: String): String? {
            val id = application.resources.getIdentifier(filename, "raw", application.packageName)
            return application.resources.openRawResource(id).bufferedReader().use { it.readText() }
        }
    }

}