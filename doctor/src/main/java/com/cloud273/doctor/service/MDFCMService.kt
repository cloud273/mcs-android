package com.cloud273.doctor.service

import com.google.firebase.messaging.FirebaseMessagingService

class MDFCMService: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        println("onNewToken = $p0")
    }
}